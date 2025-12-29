package cn.yang.nebula.agent.business.file.library.entity;

import cn.yang.common.data.structure.entity.BaseEntity;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件库实体
 *
 * @author QingHai
 */
@Data
public class FileLibrary extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -6600478611028997602L;

    /**
     * 主键id
     */
    private String id;

    /**
     * 空间id
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

    public FileLibrary() {
    }

    public FileLibrary(String name, String mimeType, Long size, String path) {
        this.name = name;
        this.mimeType = mimeType;
        this.size = size;
        this.path = path;
    }

    public FileLibrary(String spaceId, String name, String mimeType, Long size, String path) {
        this.spaceId = spaceId;
        this.name = name;
        this.mimeType = mimeType;
        this.size = size;
        this.path = path;
    }
}

