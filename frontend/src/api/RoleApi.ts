import request from "../utils/request";
import {ResultVo} from "../type/ResultVo.ts";
import {AxiosResponse} from "axios";
import {SysRolePageDto} from "../type/role/SysRolePageDto.ts";
import {SysRoleQueryDto} from "../type/role/SysRoleQueryDto.ts";
import {PageResult} from "../type/PageResult.ts";
import {SysRoleAddDto} from "../type/role/SysRoleAddDto.ts";
import {SysRoleDto} from "../type/role/SysRoleDto.ts";
import {SysRoleUpdateDto} from "../type/role/SysRoleUpdateDto.ts";


type ConfigType<T = ResultVo> = Promise<AxiosResponse<T>>

/**
 * 查询角色分页列表接口
 * @param data 查询入参
 */
export const selectRoleList = (data: SysRoleQueryDto): ConfigType<ResultVo<PageResult<SysRolePageDto[]>>> => {
    return request({
        url: "/role/page",
        method: 'POST',
        data: data
    });
}

/**
 * 新增角色
 * @param data 新增角色数据
 */
export const insertRole = (data: SysRoleAddDto): ConfigType<ResultVo<null>> => {
    return request({
        url: "/role/insert",
        method: 'POST',
        data: data
    });
}

/**
 * 查询角色详情
 * @param data 角色数据id
 */
export const selectRoleById = (data: string): ConfigType<ResultVo<SysRoleDto>> => {
    return request({
        url: "/role/selectById?id=" + data,
        method: 'GET'
    });
}

/**
 * 修改角色
 * @param data 修改角色数据
 */
export const updateRole = (data: SysRoleUpdateDto): ConfigType<ResultVo<null>> => {
    return request({
        url: "/role/update",
        method: 'POST',
        data: data
    });
}


/**
 * 删除角色根据角色id
 * @param id 角色id
 */
export const deleteRoleById = (id: string): ConfigType<ResultVo<null>> => {
    return request({
        url: "/role/deleteById?id=" + id,
        method: 'GET'
    });
}