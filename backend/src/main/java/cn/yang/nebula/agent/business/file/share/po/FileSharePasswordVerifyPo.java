package cn.yang.nebula.agent.business.file.share.po;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分享密码验证入参
 *
 * @author QingHai
 */
@Data
public class FileSharePasswordVerifyPo implements Serializable {

    @Serial
    private static final long serialVersionUID = 5493403812822275187L;

    /**
     * 分享ID
     */
    @NotBlank(message = "分享ID不能为空")
    private String shareId;

    /**
     * 访问密码
     */
    private String password;
}

