/**
 * 分页数据包装接口
 */
export interface PageResult<T = any> {

    /**
     * 当前页数
     */
    current: number,

    /**
     * 每页条数
     */
    size: number,

    /**
     * 总条数
     */
    total: number,

    /**
     * 目标数据
     */
    list?: T
}
