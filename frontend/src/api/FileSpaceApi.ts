import request from "@/utils/request";
import { ResultVo } from "@/type/ResultVo.ts";
import { PageResult } from "@/type/PageResult.ts";
import { FileSpaceInsertDto } from "@/type/filespace/FileSpaceInsertDto.ts";
import { FileSpaceUpdateDto } from "@/type/filespace/FileSpaceUpdateDto.ts";
import { FileSpacePageQueryDto } from "@/type/filespace/FileSpacePageQueryDto.ts";
import { FileSpaceVo } from "@/type/filespace/FileSpaceVo.ts";
import { FileSpaceAllocateDto } from "@/type/filespace/FileSpaceAllocateDto.ts";
import { FileSpaceStatisticsVo } from "@/type/filespace/FileSpaceStatisticsVo.ts";

/**
 * 个人空间 API
 */

/**
 * 新增个人空间
 * @param data
 */
export function addFileSpace(data: FileSpaceInsertDto) {
    return request.post<ResultVo<string>>("/file-space/add", data);
}

/**
 * 更新个人空间
 * @param data
 */
export function updateFileSpace(data: FileSpaceUpdateDto) {
    return request.post<ResultVo<null>>("/file-space/update", data);
}

/**
 * 分页查询个人空间
 * @param query
 */
export function getFileSpacePage(query: FileSpacePageQueryDto) {
    return request.post<ResultVo<PageResult<FileSpaceVo[]>>>("/file-space/page", query);
}

/**
 * 删除个人空间
 * @param id
 */
export function deleteFileSpace(id: string) {
    return request.get<ResultVo<null>>(`/file-space/delete?id=${id}`);
}

/**
 * 获取当前用户的所有的文件空间列表
 */
export function selectFileSpaces() {
    return request.get<ResultVo<any[]>>("/file-space/selectFileSpaces");
}

/**
 * 分配文件至个人空间
 * @param data
 */
export function allocateFilesToSpace(data: FileSpaceAllocateDto) {
    return request.post<ResultVo<null>>("/file-space/allocate", data);
}

/**
 * 获取空间统计数据
 * @returns Promise<AxiosResponse<ResultVo<FileSpaceStatisticsVo>>> 统计结果
 */
export function getFileSpaceStatistics() {
    return request.get<ResultVo<FileSpaceStatisticsVo>>("/file-space/statistics");
}
