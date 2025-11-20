/**
 * 查询角色列表入参
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
     * 当前页条数
     */
    size: number;
}
