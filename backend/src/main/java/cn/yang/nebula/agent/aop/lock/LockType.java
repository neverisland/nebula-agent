package cn.yang.nebula.agent.aop.lock;

/**
 * 分布式锁类型枚举。
 * <p>
 * 用于决定通过 RedissonClient 获取哪种锁对象：
 * <ul>
 *   <li>REENTRANT：普通可重入锁（RLock）</li>
 *   <li>FAIR：公平锁（RLock，按请求顺序）</li>
 *   <li>READ：读锁（RReadWriteLock.readLock）</li>
 *   <li>WRITE：写锁（RReadWriteLock.writeLock）</li>
 * </ul>
 *
 * @author : QingHai
 */
public enum LockType {
    REENTRANT,
    FAIR,
    READ,
    WRITE
}
