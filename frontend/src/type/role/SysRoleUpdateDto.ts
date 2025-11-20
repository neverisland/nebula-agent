/**
 * 角色修改入参
 */
export interface SysRoleUpdateDto {

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
     * 权限id列表
     */
    permissionIdList: string[];
}