package cn.yang.nebula.agent.business.file.share.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * 创建分享入参
 *
 * @author 未见清海
 */
@Data
public class FileShareCreatePo implements Serializable {

    @Serial
    private static final long serialVersionUID = -6765307141040916008L;

    /**
     * 分享类型: 1-个人文件, 2-个人空间
     */
    @NotNull(message = "分享类型不能为空")
    private Integer shareType;

    /**
     * 分享名称
     */
    @NotBlank(message = "分享名称不能为空")
    private String name;

    /**
     * 是否开启密码访问
     */
    @NotNull(message = "是否开启密码访问不能为空")
    private Boolean enablePassword;

    /**
     * 访问密码
     */
    private String password;

    /**
     * 是否开启过期时间
     */
    @NotNull(message = "是否开启过期时间不能为空")
    private Boolean enableExpire;

    /**
     * 分享过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expireTime;

    /**
     * 关联的文件ID列表
     */
    private List<String> fileIds;

    /**
     * 关联的空间ID
     */
    private String spaceId;

}
