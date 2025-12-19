/**
 * 查询用户列表入参
 */
export interface UserQueryDto {

    /**
     * 检索文本
     */
    searchText: string;

    /**
     * 用户状态
     */
    enabled?: boolean | null;

    /**
     * 当前页
     */
    current: number;

    /**
     * 当前页条数
     */
    size: number;
}
