/**
 * 文件库分页查询响应VO
 */
export interface FileLibraryPageVo {

    /**
     * 文件ID
     */
    id: string;

    /**
     * 文件名
     */
    name: string;

    /**
     * MIME类型
     */
    mimeType: string;

    /**
     * 文件大小（字节）
     */
    size: number;

    /**
     * 文件访问URL
     */
    url: string;

    /**
     * 缩略图URL
     */
    thumbnailsUrl?: string | null;

    /**
     * 创建时间
     */
    createTime?: string;

    /**
     * 空间ID
     */
    spaceId?: string;
}
