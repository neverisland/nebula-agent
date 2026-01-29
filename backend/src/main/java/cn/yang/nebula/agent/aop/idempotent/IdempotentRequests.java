package cn.yang.nebula.agent.aop.idempotent;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 防止重复请求基础
 *
 * @author : QingHai
 */
@Data
public class IdempotentRequests implements Serializable {

    @Serial
    private static final long serialVersionUID = -8073110669518133548L;

    /**
     * 幂等Key(UUID)
     */
    @NotBlank(message = "幂等不能为空")
    private String idempotentKey;
}
