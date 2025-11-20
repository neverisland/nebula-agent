/**
 * 数据包装接口
 */
export interface ResultVo<T = any> {

    /**
     * 状态码 0 表示成功 其他 表示失败
     */
    code: number,

    /**
     * 状态码描述
     */
    codeMsg: string,

    /**
     * 详细描述
     */
    details: string,

    /**
     * 目标数据
     */
    data: T
}
