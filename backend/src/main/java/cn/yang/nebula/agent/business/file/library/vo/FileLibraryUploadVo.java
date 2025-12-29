package cn.yang.nebula.agent.business.file.library.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件上传响应
 *
 * @author QingHai
 */
@Data
public class FileLibraryUploadVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 6122979486995686258L;

    /**
     * 文件id
     */
    private String id;

    /**
     * 文件名称
     */
    private String name;

    /**
     * 文件下载/预览地址
     */
    private String url;

    /**
     * 缩略图地址
     */
    private String thumbnailsUrl;
}

