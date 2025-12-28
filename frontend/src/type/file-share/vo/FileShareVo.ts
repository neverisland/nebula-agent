/**
 * 分享详情视图对象
 */
export interface FileShareVo {
    /**
     * 主键ID
     */
    id: string;

    /**
     * 分享类型（1:文件 2:空间）
     */
    shareType: number;

    /**
     * 分享名称
     */
    name: string;

    /**
     * 分享链接
     */
    shareUrl: string;

    /**
     * 是否启用密码
     */
    enablePassword: boolean;

    /**
     * 访问密码（脱敏）
     */
    password?: string;

    /**
     * 是否启用过期
     */
    enableExpire: boolean;

    /**
     * 过期时间
     */
    expireTime?: string;

    /**
     * 是否已过期
     */
    isExpired: boolean;

    /**
     * 访问次数
     */
    visitCount: number;

    /**
     * 下载次数
     */
    downloadCount: number;

    /**
     * 创建时间
     */
    createTime: string;

    /**
     * 文件数量
     */
    fileCount: number;

    /**
     * 文件列表（仅文件类型分享有值）
     */
    fileList?: any[];

    /**
     * 空间对象（仅空间类型分享有值）
     */
    spaceObj?: any;
}
