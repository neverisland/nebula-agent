package cn.yang.nebula.agent.business.user.dto.role;

import cn.yang.nebula.agent.business.user.dto.permission.PermissionDto;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 角色详情
 *
 * @author : QingHai
 */
@Data
public class RoleDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 378797854677835007L;

    /**
     * 角色id
     */
    private String id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 角色类型 0 内置角色 1 自定义角色
     */
    private Integer type;

    /**
     * 权限列表
     */
    private List<PermissionDto> permissionList;

}
