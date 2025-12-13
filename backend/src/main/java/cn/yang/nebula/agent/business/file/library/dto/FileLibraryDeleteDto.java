package cn.yang.nebula.agent.business.file.library.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件删除入参
 *
 * @author 未见清海
 */
@Data
public class FileLibraryDeleteDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1690703169080915792L;

    /**
     * 主键id
     */
    private String id;
}

