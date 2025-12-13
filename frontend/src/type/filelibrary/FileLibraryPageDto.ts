/**
 * 文件库分页查询请求DTO
 */
export interface FileLibraryPageDto {

    /**
     * 当前页码
     */
    current: number;

    /**
     * 每页条数
     */
    size: number;

    /**
     * 搜索关键词（文件名/路径）
     */
    searchText?: string;

    /**
     * MIME类型筛选
     */
    mimeType?: string;

    /**
     * 空间ID
     */
    spaceId?: string;
}
