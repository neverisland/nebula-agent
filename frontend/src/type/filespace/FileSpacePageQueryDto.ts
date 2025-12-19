/**
 * 分页查询文件空间入参
 */
export interface FileSpacePageQueryDto {
    /**
     * 当前页
     */
    current: number;

    /**
     * 当前页条数
     */
    size: number;

    /**
     * 空间名称
     */
    name?: string;

    /**
     * 创建人ID
     */
    createUserId?: string;
}
