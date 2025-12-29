package cn.yang.nebula.agent.business.user.dto.user;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 分配角色入参
 *
 * @author QingHai
 */
@Data
public class AssignRoleDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 角色id列表
     */
    private List<String> roleIdList;
}
