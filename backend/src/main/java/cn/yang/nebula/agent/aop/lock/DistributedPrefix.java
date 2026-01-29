package cn.yang.nebula.agent.aop.lock;

/**
 * 幂等 Key 前缀
 *
 * @author : QingHai
 */
public interface DistributedPrefix {

    /**
     * 默认前缀
     */
    String DEFAULT_PREFIX = "lock:distributed";

    /**
     * 兑换码兑换(重复请求锁)
     */
    String COUPON_REDEEM_CODE = "lock:coupon-redeem-code";

    /**
     * 订单过期事件处理(未支付)
     */
    String ORDER_EXPIRE_HANDLE = "lock:order:expire-handle";

    /**
     * 订单过期 定时任务锁
     */
    String ORDER_EXPIRE_JOB_LOCK = "lock:job:order-expire";

    /**
     * 订单支付 定时任务锁
     */
    String ORDER_PAY_LOCK = "lock:job:order-pay";

    /**
     * 订单支付
     */
    String PAY_ORDER_HANDLE = "lock:pay-order";
}
