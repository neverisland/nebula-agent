package cn.yang.nebula.agent.business.file.library.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件重命名入参
 *
 * @author QingHai
 */
@Data
public class FileLibraryRenameDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -1677990642258655401L;

    /**
     * 主键id
     */
    @NotBlank(message = "id不能为空")
    private String id;

    /**
     * 新文件名称
     */
    @NotBlank(message = "名称不能为空")
    private String name;
}

