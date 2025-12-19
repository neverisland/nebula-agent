import { SysPermissionVo } from "@/type/permission/SysPermissionVo";

/**
 * 角色详情出参
 */
export interface SysRoleVo {

    /**
     * 角色id
     */
    id: string;

    /**
     * 角色名称
     */
    name: string;

    /**
     * 角色描述
     */
    description: string;

    /**
     * 角色类型 0 内置角色 1 自定义角色
     */
    type: number;

    /**
     * 权限列表
     */
    permissionList: SysPermissionVo[];
}
