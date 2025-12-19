/**
 * 分配文件至空间 入参
 */
export interface FileSpaceAllocateDto {
    /**
     * 空间id
     */
    spaceId: string;
    /**
     * 文件id列表
     */
    fileIds: string[];
}
