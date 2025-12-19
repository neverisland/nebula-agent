/**
 * 主页统计数据 VO
 */
export interface HomeStatisticsVo {
    /**
     * 文件数量
     */
    fileCount: number;

    /**
     * 空间数量
     */
    spaceCount: number;

    /**
     * 存储大小（字节）
     */
    storageUsed: number;
}
