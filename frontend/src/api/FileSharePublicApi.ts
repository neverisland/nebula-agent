import request from "@/utils/request";
import type { ResultVo } from "@/type/ResultVo";
import type { FileSharePublicVo } from "@/type/file-share/vo/FileSharePublicVo";
import type { FileSharePasswordVerifyPo } from "@/type/file-share/po/FileSharePasswordVerifyPo";
import type { FileLibraryPageVo } from "@/type/filelibrary/FileLibraryPageVo";

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

/**
 * 验证分享密码（公开接口）
 * @param verifyPo 验证参数
 */
export const verifySharePassword = (verifyPo: FileSharePasswordVerifyPo): Promise<ResultVo<any>> => {
    return request({
        url: '/file-share/public/verifyPassword',
        method: 'post',
        data: verifyPo
    });
};

/**
 * 增加访问次数（公开接口）
 * @param id 分享ID
 */
export const incrementVisitCount = (id: string): Promise<ResultVo<any>> => {
    return request({
        url: '/file-share/public/incrementVisit',
        method: 'get',
        params: { id }
    });
};

/**
 * 获取文件列表（公开接口）
 * @param verifyPo 查询参数
 */
export const getPublicFileList = (verifyPo: FileSharePasswordVerifyPo): Promise<ResultVo<FileLibraryPageVo[]>> => {
    return request({
        url: '/file-share/public/fileList',
        method: 'post',
        data: verifyPo
    });
};

/**
 * 下载全部文件（公开接口）
 * @param shareId 分享ID
 * @param password 密码（可选）
 */
export const downloadAllFiles = (shareId: string, password?: string): void => {
    const params = new URLSearchParams({ shareId });
    if (password) {
        params.append('password', password);
    }
    window.open(`/api/file-share/public/downloadAll?${params.toString()}`, '_blank');
};
