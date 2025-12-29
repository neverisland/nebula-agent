package cn.yang.nebula.agent.business.file.library.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文件库分页数据
 *
 * @author QingHai
 */
@Data
public class FileLibraryPageVo implements Serializable {

    @Serial
    private static final long serialVersionUID = -2314187177276958338L;

    /**
     * 主键id
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
     * 文件访问
     */
    private String url;

    /**
     * 缩略图访问路径
     */
    private String thumbnailsUrl;

    /**
     * 上传时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh-mm-ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 空间id
     */
    private String spaceId;
}

