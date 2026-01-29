package cn.yang.nebula.agent.exception;

import java.io.Serial;

/**
 * 分布式锁获取失败异常。
 * <p>
 * 当 failStrategy=THROW 且未获取到锁时抛出。
 *
 * @author : QingHai
 */
public class LockAcquireException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3667889565534836824L;

    /**
     * 构造函数。
     *
     * @param message 异常提示信息
     */
    public LockAcquireException(String message) {
        super(message);
    }
}
