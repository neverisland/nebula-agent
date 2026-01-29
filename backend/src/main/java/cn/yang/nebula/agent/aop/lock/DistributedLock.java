package cn.yang.nebula.agent.aop.lock;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁注解。
 * <p>
 * 将该注解标注在方法上，通过 AOP 在方法执行前后自动加/解锁。
 * <p>
 * 推荐 key 使用 SpEL 从入参提取业务维度，避免锁粒度过大，例如：
 * <pre>
 *   &#064;DistributedLock(prefix="order:pay",  key="#orderId")
 *   &#064;DistributedLock(prefix="order:pay",  key="#req.userId + ':' + #req.orderId")
 * </pre>
 *
 * <p><b>leaseTime <= 0</b> 时使用 Redisson watchdog 自动续期（推荐），
 * 可避免业务执行时间不可控导致锁提前过期。
 *
 * @author : QingHai
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {

    /**
     * 锁 Key 前缀（建议按业务模块划分）。
     * 例如：order:pay、inventory:deduct
     */
    String prefix() default DistributedPrefix.DEFAULT_PREFIX;

    /**
     * SpEL 表达式，用于生成锁的动态部分。
     * <p>
     * 支持：
     * <ul>
     *   <li>#p0/#a0：按参数下标取值</li>
     *   <li>#orderId：按参数名取值（需编译保留参数名 -parameters）</li>
     *   <li>#req.userId：对象属性访问</li>
     * </ul>
     */
    String key() default "'global'";

    /**
     * 等待获取锁的最大时间。
     * <p>
     * waitTime <= 0 表示不等待（立即尝试一次）。
     */
    long waitTime() default 0;

    /**
     * 锁租期（持有时间）。
     * <p>
     * - leaseTime <= 0：启用 watchdog 自动续期（推荐）
     * <p>
     * - leaseTime  > 0：固定租期，到期自动释放（注意超时风险）
     */
    long leaseTime() default -1;

    /**
     * 时间单位（waitTime/leaseTime 使用）。
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 锁类型，默认普通可重入锁。
     */
    LockType lockType() default LockType.REENTRANT;

    /**
     * 获取锁失败策略，默认抛异常。
     */
    FailStrategy failStrategy() default FailStrategy.THROW;

    /**
     * 获取锁失败时的异常提示信息（FailStrategy.THROW 时生效）。
     */
    String message() default "Failed to acquire distributed lock";

}
