/**
 * 角色新增入参
 */
export interface SysRoleAddDto {

    /**
     * 角色新增
     */
    name: string;

    /**
     * 角色描述
     */
    description: string;

    /**
     * 权限id列表
     */
    permissionIdList: string[];
}