package cn.yang.nebula.agent.business.file.share.entity;

import cn.yang.nebula.agent.vo.entity.BaseEntity;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 文件分享实体
 *
 * @author : QingHai
 */
@Data
public class FileShare extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 5471771975970085815L;

    /**
     * 分享记录ID
     */
    private String id;

    /**
     * 分享类型: 1-个人文件, 2-个人空间
     */
    private Integer shareType;

    /**
     * 分享名称或描述
     */
    private String name;

    /**
     * 访问密码，可为空
     */
    private String password;

    /**
     * 是否开启密码访问
     */
    private Boolean enablePassword = Boolean.FALSE;

    /**
     * 分享过期时间，可为空
     */
    private LocalDate expireTime;

    /**
     * 是否开启过期时间
     */
    private Boolean enableExpire = Boolean.FALSE;

    /**
     * 是否已过期
     */
    private Boolean isExpired = Boolean.FALSE;

    /**
     * 访问次数
     */
    private Integer visitCount = 0;

    /**
     * 下载次数
     */
    private Integer downloadCount = 0;

    /**
     * 关联的文件ID列表
     */
    private List<String> fileIds;

    /**
     * 关联的空间ID
     */
    private String spaceId;
}
