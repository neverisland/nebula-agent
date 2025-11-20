package cn.yang.nebula.agent.exception;

import java.io.Serial;

/**
 * 模型调用异常
 *
 * @author : 未见清海
 */
public class ModelCallException extends Exception {

    @Serial
    private static final long serialVersionUID = 5351056072821670711L;

    public ModelCallException(String message) {
        super(message);
    }

    public ModelCallException(String message, Throwable cause) {
        super(message, cause);
    }
}
