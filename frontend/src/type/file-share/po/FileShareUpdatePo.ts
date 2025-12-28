/**
 * 更新分享入参
 */
export interface FileShareUpdatePo {
    /**
     * 分享ID
     */
    id: string;

    /**
     * 分享名称
     */
    name: string;

    /**
     * 是否启用密码
     */
    enablePassword: boolean;

    /**
     * 密码（为空则不更改）
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
     * 文件ID集合
     */
    fileIds?: string[];

    /**
     * 空间ID
     */
    spaceId?: string;
}
