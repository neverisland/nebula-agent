package cn.yang.nebula.agent.business.file.library.dal;

import cn.yang.common.data.structure.entity.BaseEntity;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件库
 *
 * @author 未见清海
 */
@Data
public class FileLibraryDo extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 7760388982412330091L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 空间id（预留）
     */
    private String spaceId;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件类型
     */
    private String mimeType;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 文件存储相对路径
     */
    private String path;

    /**
     * 文件缩略图相对路径
     */
    private String thumbnails;
}

