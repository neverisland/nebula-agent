/**
 * 文件库文件上传响应VO
 */
export interface FileLibraryUploadVo {

    /**
     * 文件ID
     */
    id: string;

    /**
     * 文件名
     */
    name: string;

    /**
     * 文件访问URL
     */
    url: string;

    /**
     * 缩略图URL
     */
    thumbnailsUrl?: string | null;

    /**
     * MIME类型
     */
    mimeType?: string;
}
