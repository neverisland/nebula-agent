package cn.yang.nebula.agent.business.file.share.vo;

import cn.yang.nebula.agent.business.file.space.vo.FileSpaceVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 分享记录 VO
 *
 * @author 未见清海
 */
@Data
public class FileSharePublicVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 8439967187049319346L;

    /**
     * 分享记录ID
     */
    private String id;

    /**
     * 分享类型: 1-个人文件, 2-个人空间
     */
    private Integer shareType;

    /**
     * 分享名称
     */
    private String name;

    /**
     * 是否开启密码访问
     */
    private Boolean enablePassword;

    /**
     * 是否开启过期时间
     */
    private Boolean enableExpire;

    /**
     * 分享过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expireTime;

    /**
     * 是否已过期
     */
    private Boolean isExpired;

    /**
     * 访问次数
     */
    private Integer visitCount;

    /**
     * 下载次数
     */
    private Integer downloadCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 关联文件数量
     */
    private Integer fileCount = 0;

    /**
     * 文件空间信息
     */
    private FileSpaceVo fileSpaceVo;
}
