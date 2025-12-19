/**
 * 文件空间信息出参
 */
export interface FileSpaceVo {
    /**
     * 空间ID
     */
    id: string;

    /**
     * 空间名称
     */
    name: string;

    /**
     * 备注
     */
    remark: string;

    /**
     * 文件数量
     */
    fileCount: number;

    /**
     * 空间大小
     */
    totalSize: number;

    /**
     * 创建时间
     */
    createTime: string;
}
