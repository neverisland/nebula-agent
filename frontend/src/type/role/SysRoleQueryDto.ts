/**
 * 角色分页查询入参
 */
export interface SysRoleQueryDto {

    /**
     * 检索文本
     */
    searchText: string;

    /**
     * 当前页
     */
    current: number;

    /**
     * 当前页大小
     */
    size: number;
}
