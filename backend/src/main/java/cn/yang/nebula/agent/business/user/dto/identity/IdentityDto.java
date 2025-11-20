package cn.yang.nebula.agent.business.user.dto.identity;

import cn.yang.nebula.agent.enums.EnabledEnum;
import cn.yang.nebula.agent.business.user.dto.role.RoleDto;
import cn.yang.nebula.agent.business.user.enums.IdentityTypeEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 身份出参
 *
 * @author : 未见清海
 */
@Data
public class IdentityDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 6359493929628870038L;

    /**
     * 身份id
     */
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 身份类型
     * <br>
     * 参见 {@link IdentityTypeEnum}
     */
    private Integer type;

    /**
     * 是否启用 0 禁用 1 启用
     * <br>
     * 参见 {@link EnabledEnum}
     */
    private Boolean enabled;

    /**
     * 身份的角色列表
     */
    private List<RoleDto> roleList;

}
