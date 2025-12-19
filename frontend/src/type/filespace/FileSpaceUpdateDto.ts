/**
 * 修改文件空间入参
 */
export interface FileSpaceUpdateDto {
    /**
     * 空间ID
     */
    id: string;

    /**
     * 空间名称
     */
    name?: string;

    /**
     * 备注
     */
    remark?: string;
}
