import request from "@/utils/request";
import type { ResultVo } from "@/type/ResultVo";
import type { PageResult } from "@/type/PageResult";
import type { FileShareCreatePo } from '@/type/file-share/po/FileShareCreatePo';
import type { FileShareUpdatePo } from '@/type/file-share/po/FileShareUpdatePo';
import type { FileSharePageQueryPo } from '@/type/file-share/po/FileSharePageQueryPo';
import type { FileShareVo } from '@/type/file-share/vo/FileShareVo';
import { AxiosResponse } from "axios";

/**
 * 新增分享
 * @param data 新增分享入参
 * @returns 分享ID
 */
export const createShare = (data: FileShareCreatePo): Promise<AxiosResponse<ResultVo<string>>> => {
    return request({
        url: '/file-share/create',
        method: 'post',
        data
    });
}

/**
 * 修改分享
 * @param data 修改分享入参
 * @returns 无
 */
export const updateShare = (data: FileShareUpdatePo): Promise<AxiosResponse<ResultVo<void>>> => {
    return request({
        url: '/file-share/update',
        method: 'post',
        data
    });
}

/**
 * 删除分享
 * @param id 分享ID
 * @returns 无
 */
export const deleteShare = (id: string): Promise<AxiosResponse<ResultVo<void>>> => {
    return request({
        url: '/file-share/delete',
        method: 'get',
        params: { id }
    });
}

/**
 * 获取分享详情
 * @param id 分享ID
 * @returns 分享详情Vo
 */
export const getShareDetail = (id: string): Promise<AxiosResponse<ResultVo<FileShareVo>>> => {
    return request({
        url: '/file-share/selectById',
        method: 'get',
        params: { id }
    });
}

/**
 * 分页查询分享列表
 * @param data 分页查询入参
 * @returns 分页结果
 */
export const getSharePage = (data: FileSharePageQueryPo): Promise<AxiosResponse<ResultVo<PageResult<FileShareVo[]>>>> => {
    return request({
        url: '/file-share/page',
        method: 'post',
        data
    });
}
