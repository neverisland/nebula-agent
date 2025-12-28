/**
 * 分享分页查询入参
 */
export interface FileSharePageQueryPo {
    /**
     * 当前页码
     */
    current: number;

    /**
     * 每页条数
     */
    size: number;

    /**
     * 分享名称（模糊查询）
     */
    name?: string;

    /**
     * 分享类型
     */
    shareType?: number;

    /**
     * 是否过期
     */
    isExpired?: boolean;
}
