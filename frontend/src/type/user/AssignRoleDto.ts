/**
 * 分配角色入参
 */
export interface AssignRoleDto {
    /**
     * 用户id
     */
    userId: string;

    /**
     * 角色id列表
     */
    roleIdList: string[];
}
