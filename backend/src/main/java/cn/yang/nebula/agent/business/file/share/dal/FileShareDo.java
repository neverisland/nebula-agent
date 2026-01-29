package cn.yang.nebula.agent.business.file.share.dal;

import cn.yang.nebula.agent.vo.entity.BaseEntity;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * 分享记录 DO
 *
 * @author QingHai
 */
@Data
public class FileShareDo extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -6557570460704220159L;

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
    private Boolean enablePassword;

    /**
     * 分享过期时间，可为空
     */
    private LocalDate expireTime;

    /**
     * 是否开启过期时间
     */
    private Boolean enableExpire;

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
}
