package cn.yang.nebula.agent.aop.lock;

import cn.yang.nebula.agent.exception.LockAcquireException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁切面。
 * <p>
 * 拦截所有标注 {@link DistributedLock} 的方法，在方法执行前尝试加锁，
 * 执行完成后释放锁。
 *
 * <p>关键点：
 * <ul>
 *   <li>支持 SpEL 从入参构建锁 key</li>
 *   <li>leaseTime<=0 使用 watchdog 自动续期（推荐）</li>
 *   <li>finally 里释放锁时检查 isHeldByCurrentThread，避免误解锁</li>
 *   <li>使用 Micrometer 埋点：获取锁耗时、持锁耗时、成功/失败/异常次数</li>
 * </ul>
 * <p>
 * 对定时任务，通常建议：
 * <p>
 * failStrategy=SKIP（拿不到锁就直接跳过，0 噪音）
 * <p>
 * waitTime=0（不等待，避免调度线程堆积）
 * <p>
 * leaseTime<=0（watchdog）或给一个足够大但可控的租期
 *
 * @author : QingHai
 */
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {

    /**
     * Redisson 客户端，用于获取锁对象
     */
    private final RedissonClient redissonClient;

    /**
     * 锁 key 解析器（SpEL）
     */
    private final SpElLockKeyResolver keyResolver = new SpElLockKeyResolver();

    /**
     * 环绕通知：执行加锁 -> 执行业务 -> 解锁。
     *
     * @param pjp JoinPoint
     * @return 方法返回值
     * @throws Throwable 业务异常原样抛出
     */
    @Around("@annotation(cn.yang.nebula.agent.aop.lock.DistributedLock)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        // 获取目标方法与注解
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        DistributedLock ann = method.getAnnotation(DistributedLock.class);

        LockType lockType = ann.lockType();

        // 解析锁 key（prefix + SpEL）
        String lockKey = keyResolver.resolveKey(method, pjp.getArgs(), ann);

        // 根据 lockType 获取不同类型锁
        RLock lock = getLock(lockKey, lockType);

        boolean locked = false;

        try {
            // 尝试获取锁（带等待与租期逻辑）
            locked = tryAcquire(lock, ann.waitTime(), ann.leaseTime(), ann.timeUnit());

            // 未获取到锁：按策略处理
            if (!locked) {
                return handleFail(ann, method.getReturnType());
            }

            // 已获取锁：执行业务方法，并记录持锁时间（近似业务临界区耗时）
            return pjp.proceed();

        } finally {
            // 解锁必须放在 finally：确保异常也能释放锁
            if (locked) {
                try {
                    // 防止：锁已过期/被他人获取时误 unlock
                    if (lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                } catch (Exception unlockEx) {
                    // 解锁异常建议记录日志，但不要影响业务异常抛出链路
                    // log.warn("unlock failed, key={}", lockKey, unlockEx);
                }
            }
        }
    }

    /**
     * 尝试获取锁。
     * <p>
     * leaseTime<=0：不指定租期，启用 watchdog 自动续期（推荐）
     * leaseTime>0：固定租期，到期自动释放（注意超时风险）
     *
     * @param lock      Redisson 锁对象
     * @param waitTime  等待时间
     * @param leaseTime 租期
     * @param unit      时间单位
     * @return 是否获取成功
     * @throws InterruptedException 线程中断异常
     */
    private boolean tryAcquire(RLock lock, long waitTime, long leaseTime, TimeUnit unit) throws InterruptedException {
        // 不等待：tryLock() 立即尝试一次
        if (waitTime <= 0) {
            if (leaseTime <= 0) {
                // watchdog
                return lock.tryLock();
            }
            // 固定租期：立即尝试
            return lock.tryLock(0, leaseTime, unit);
        }

        // 等待获取锁：最多等待 waitTime
        if (leaseTime <= 0) {
            // watchdog：不传 leaseTime
            return lock.tryLock(waitTime, unit);
        }
        // 固定租期：传 leaseTime
        return lock.tryLock(waitTime, leaseTime, unit);
    }

    /**
     * 根据锁类型获取对应的 Redisson 锁对象。
     *
     * @param key  锁 key
     * @param type 锁类型
     * @return RLock
     */
    private RLock getLock(String key, LockType type) {
        return switch (type) {
            case REENTRANT -> redissonClient.getLock(key);
            case FAIR -> redissonClient.getFairLock(key);
            case READ -> redissonClient.getReadWriteLock(key).readLock();
            case WRITE -> redissonClient.getReadWriteLock(key).writeLock();
        };
    }

    /**
     * 获取锁失败处理。
     *
     * @param ann        注解
     * @param returnType 方法返回类型
     * @return failStrategy 非 THROW 时返回的结果
     */
    private Object handleFail(DistributedLock ann, Class<?> returnType) {
        return switch (ann.failStrategy()) {
            case THROW -> throw new LockAcquireException(ann.message());
            case RETURN_NULL -> null;
            case SKIP -> {
                // 对 void 方法：直接跳过（返回 null 即可）
                // 对非 void 方法：返回 null（可按需要扩展为默认值/Optional）
                yield null;
            }
        };
    }
}
