package cn.yang.nebula.agent.business.user.entity;

import cn.yang.nebula.agent.enums.EnabledEnum;
import cn.yang.nebula.agent.business.user.enums.IdentityTypeEnum;
import cn.yang.common.data.structure.entity.BaseEntity;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 身份实体
 *
 * @author : 未见清海
 */
@Data
public class Identity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -7526684363195336538L;

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
    private List<Role> roleList;
}
