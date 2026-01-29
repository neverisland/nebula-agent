package cn.yang.nebula.agent.business.user.enums;

import cn.yang.nebula.agent.exception.NullDataException;
import lombok.Getter;

import java.util.List;

/**
 * 角色枚举
 *
 * @author : QingHai
 */
@Getter
public enum RoleEnum {

    SYSTEM_ADMINISTRATOR("system_administrator", "系统管理员", "系统管理员角色,负责系统的维护使用",
            List.of(PermissionEnum.BASIC_PERMISSIONS, PermissionEnum.MANAGEMENT_ROLE)),

    ORDINARY_USERS("ordinary_users", "普通用户", "系统普通用户", List.of(PermissionEnum.BASIC_PERMISSIONS)),
    ;

    /**
     * 角色标识
     */
    private final String mark;

    /**
     * 角色名称
     */
    private final String name;

    /**
     * 描述
     */
    private final String description;

    /**
     * 关联的权限列表
     */
    private final List<PermissionEnum> permissionList;

    RoleEnum(String mark, String name, String description, List<PermissionEnum> permissionList) {
        this.mark = mark;
        this.name = name;
        this.description = description;
        this.permissionList = permissionList;
    }

    /**
     * 根据角色标识获取角色枚举
     *
     * @param mark 角色标识
     * @return 角色枚举
     */
    public static RoleEnum getRoleEnumByMark(String mark) throws NullDataException {
        for (RoleEnum roleEnum : values()) {
            if (roleEnum.getMark().equals(mark)) {
                return roleEnum;
            }
        }
        throw new NullDataException("根据角色标识获取角色数据异常");
    }
}
