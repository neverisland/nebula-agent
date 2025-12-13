import request from "@/utils/request";
import type { FileLibraryPageDto } from "@/type/filelibrary/FileLibraryPageDto";
import type { FileLibraryUploadVo } from "@/type/filelibrary/FileLibraryUploadVo";
import type { FileLibraryPageVo } from "@/type/filelibrary/FileLibraryPageVo";
import type { PageResult } from "@/type/PageResult";
import type { ResultVo } from "@/type/ResultVo";
import { AxiosResponse } from "axios";

/**
 * 文件库接口
 */

/**
 * 上传文件到文件库
 * @param data 包含文件的FormData对象，必须包含名为'file'的文件字段
 * @returns Promise<AxiosResponse<ResultVo<FileLibraryUploadVo>>> 上传结果，包含文件基本信息、下载地址等
 */
export const uploadFileLibrary = (data: FormData): Promise<AxiosResponse<ResultVo<FileLibraryUploadVo>>> => {
  return request({
    url: '/file-library/upload',
    method: 'post',
    headers: {
      'Content-Type': 'multipart/form-data',
    },
    data,
  });
}

/**
 * 分页查询文件库列表
 * @param data FileLibraryPageDto 分页查询参数，包含页码(current)、页大小(size)、搜索文本(searchText)等
 * @returns Promise<AxiosResponse<ResultVo<PageResult<FileLibraryPageVo>>>> 分页结果，包含文件列表和分页信息
 */
export const getFileLibraryPage = (data: FileLibraryPageDto): Promise<AxiosResponse<ResultVo<PageResult<FileLibraryPageVo>>>> => {
  return request({
    url: '/file-library/page',
    method: 'post',
    data,
  });
}

/**
 * 重命名文件
 * @param payload 重命名参数对象，包含文件ID和新文件名
 * @param payload.id string 文件ID
 * @param payload.name string 新的文件名
 * @returns Promise<AxiosResponse<ResultVo<void>>> 操作结果
 */
export const renameFileLibrary = (payload: { id: string; name: string; }): Promise<AxiosResponse<ResultVo<void>>> => {
  return request({
    url: '/file-library/rename',
    method: 'post',
    data: payload,
  });
}

/**
 * 删除文件
 * @param id string 文件ID
 * @returns Promise<AxiosResponse<ResultVo<void>>> 操作结果
 */
export const deleteFileLibrary = (id: string): Promise<AxiosResponse<ResultVo<void>>> => {
  return request({
    url: `/file-library/delete/${id}`,
    method: 'get',
  });
}

/**
 * 构建文件下载URL（已废弃，后端返回的url已经是完整路径）
 * @deprecated 后端返回的url字段已经是完整的可访问路径，无需转换
 * @param relativePath string 文件相对路径，例如 "2025/12/11/xxxxx.jpg"
 * @returns string 完整的下载URL，格式为 "/api/file/{relativePath}"
 */
export const buildFileDownloadUrl = (relativePath: string): string => {
  const path = relativePath.startsWith('/') ? relativePath.substring(1) : relativePath;
  return `/api/file/${path}`;
}

