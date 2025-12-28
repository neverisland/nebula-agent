/**
 * 创建分享入参
 */
export interface FileShareCreatePo {
    /**
     * 分享类型（1:文件 2:空间）
     */
    shareType: number;

    /**
     * 分享名称
     */
    name: string;

    /**
     * 是否启用密码
     */
    enablePassword: boolean;

    /**
     * 密码
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
     * 文件ID集合（仅文件类型有效）
     */
    fileIds: string[];

    /**
     * 空间ID（仅空间类型有效）
     */
    spaceId: string;
}
