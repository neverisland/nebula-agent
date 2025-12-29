import type { FileSpaceVo } from "@/type/filespace/vo/FileSpaceVo";

/**
 * 分享公开信息 VO
 */
export interface FileSharePublicVo {
    /**
     * 分享ID
     */
    id: string;

    /**
     * 分享类型: 1-文件, 2-空间
     */
    shareType: number;

    /**
     * 分享名称
     */
    name: string;

    /**
     * 是否开启密码
     */
    enablePassword: boolean;

    /**
     * 是否开启过期
     */
    enableExpire: boolean;

    /**
     * 过期时间 (yyyy-MM-dd)
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
     * 关联文件数
     */
    fileCount: number;

    /**
     * 空间信息 (如果是空间分享)
     */
    fileSpaceVo?: FileSpaceVo;

    // --- 前端扩展字段 (Mock 用) ---
    /**
     * 是否被密码锁定
     */
    locked?: boolean;
}
