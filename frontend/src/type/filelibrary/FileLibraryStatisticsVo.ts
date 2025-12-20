/**
 * 文件库统计 VO
 */
export interface FileLibraryStatisticsVo {
    /**
     * 文件数量
     */
    fileCount: number;

    /**
     * 存储大小（字节）
     */
    storageUsed: number;
}
