/**
 * 角色新增入参
 */
export interface SysRoleAddDto {

    /**
     * 角色名称
     */
    name: string;

    /**
     * 角色描述
     */
    description: string;

    /**
     * 权限列表
     */
    permissionIdList: string[];
}