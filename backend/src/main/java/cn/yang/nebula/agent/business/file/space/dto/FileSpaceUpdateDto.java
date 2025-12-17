package cn.yang.nebula.agent.business.file.space.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 更新个人空间入参
 *
 * @author AntiGravity
 */
@Data
public class FileSpaceUpdateDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 9119523780566702410L;

    /**
     * 个人空间 ID
     */
    @NotBlank(message = "个人空间ID不能为空")
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 备注
     */
    private String remark;
}
