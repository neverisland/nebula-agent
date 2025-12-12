package cn.yang.nebula.agent.business.filelibrary.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件重命名入参
 *
 * @author 未见清海
 */
@Data
public class FileLibraryRenameDto implements Serializable {

    @Serial
    private static final long serialVersionUID = -1677990642258655401L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 新文件名称
     */
    private String name;
}

