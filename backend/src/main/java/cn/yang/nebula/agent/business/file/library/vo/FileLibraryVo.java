package cn.yang.nebula.agent.business.file.library.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件信息
 *
 * @author : QingHai
 */
@Data
public class FileLibraryVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 8184569897373687378L;

    /**
     * 文件id
     */
    private String id;

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
     * 文件下载/预览地址
     */
    private String url;

    /**
     * 缩略图地址
     */
    private String thumbnailsUrl;
}
