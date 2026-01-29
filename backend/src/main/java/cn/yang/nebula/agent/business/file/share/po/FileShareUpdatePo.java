package cn.yang.nebula.agent.business.file.share.po;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 更新分享入参
 *
 * @author QingHai
 */
@Data
public class FileShareUpdatePo extends FileShareCreatePo implements Serializable {

    @Serial
    private static final long serialVersionUID = 8386965385130689461L;

    /**
     * 分享记录ID
     */
    @NotBlank(message = "分享ID不能为空")
    private String id;
}
