package cn.yang.nebula.agent.business.user.dal;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户角色关联实体
 *
 * @author QingHai
 */
@Data
public class UserRoleDo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 角色id
     */
    private String roleId;
}
