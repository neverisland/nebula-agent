package cn.yang.nebula.agent.aop.lock;

/**
 * 获取锁失败时的处理策略。
 * <p>
 * 建议默认使用 THROW，避免静默吞掉并发问题。
 *
 * @author : QingHai
 */
public enum FailStrategy {

    /**
     * 获取失败直接抛异常
     */
    THROW,

    /**
     * 获取失败返回 null（谨慎：基本类型返回会有 NPE/拆箱问题）
     */
    RETURN_NULL,

    /**
     * 获取失败跳过执行（通常用于 void 方法或定时任务）
     */
    SKIP
}
