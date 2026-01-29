package cn.yang.nebula.agent.aop.idempotent;

/**
 * 幂等 Redis Key 前缀
 *
 * @author : QingHai
 */
public interface IdempotentPrefix {

    /**
     * 创建订单
     */
    String CREATE_ORDER = "idempotent:create-order";

    /**
     * 创建支付
     */
    String CREATE_PAY = "idempotent:create-pay";

    /**
     * 取消订单
     */
    String CANCEL_ORDER = "idempotent:cancel-order";
}
