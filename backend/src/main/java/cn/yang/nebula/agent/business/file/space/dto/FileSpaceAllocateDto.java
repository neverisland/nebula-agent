package cn.yang.nebula.agent.business.file.space.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 分配文件至空间 入参
 *
 * @author : QingHai
 */
@Data
public class FileSpaceAllocateDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -7849998486916459072L;
    
    /**
     * 空间id
     */
    private String spaceId;

    /**
     * 文件id列表
     */
    @NotEmpty(message = "文件id列表不能为空")
    private List<String> fileIds;
}
