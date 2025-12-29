package cn.yang.nebula.agent.business.file.library.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 移除文件空间归属 入参
 *
 * @author : QingHai
 */
@Data
public class FileLibraryRemoveSpaceDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -8152347629845316852L;

    /**
     * 文件id列表
     */
    @NotEmpty(message = "文件id列表不能为空")
    private List<String> fileIds;
}
