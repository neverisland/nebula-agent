package cn.yang.nebula.agent.business.file.space.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 新增个人空间入参
 *
 * @author : 未见清海
 */
@Data
public class FileSpaceInsertDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -8203639084695542913L;
    
    /**
     * 个人空间名称
     */
    @NotBlank(message = "个人空间名称不能为空")
    private String name;

    /**
     * 备注
     */
    private String remark;
}
