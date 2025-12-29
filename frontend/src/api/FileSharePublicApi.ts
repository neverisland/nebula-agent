import request from "@/utils/request";
import type { ResultVo } from "@/type/ResultVo";
import type { FileSharePublicVo } from "@/type/file-share/vo/FileSharePublicVo";

/**
 * 获取公开分享信息
 * @param shareId 分享ID
 * @param password 访问密码 (可选)
 */
export const getPublicShareInfo = (shareId: string, password?: string): Promise<ResultVo<FileSharePublicVo>> => {
    return request({
        url: '/file-share/public/info',
        method: 'get',
        params: {
            shareId,
            password
        }
    });
};
