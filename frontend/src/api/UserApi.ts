import request from "../utils/request";
import { ResultVo } from "../type/ResultVo.ts";
import { AxiosResponse } from "axios";
import { UserQueryDto } from "../type/user/UserQueryDto.ts";
import { UserPageVo } from "../type/user/UserPageVo.ts";
import { PageResult } from "../type/PageResult.ts";
import { UserAddDto } from "../type/user/UserAddDto.ts";
import { UserVo } from "../type/user/UserVo.ts";
import { UserUpdateDto } from "../type/user/UserUpdateDto.ts";
import { DisableEnableUserDto } from "@/type/user/DisableEnableUserDto.ts";
import { AssignRoleDto } from "@/type/user/AssignRoleDto.ts";

type ConfigType<T = ResultVo> = Promise<AxiosResponse<T>>

/**
 * 查询用户分页列表接口
 * @param data 查询入参
 */
export const selectUserList = (data: UserQueryDto): ConfigType<ResultVo<PageResult<UserPageVo[]>>> => {
    return request({
        url: "/user/page",
        method: 'POST',
        data: data
    });
}

/**
 * 新增用户
 * @param data 新增用户数据
 */
export const insertUser = (data: UserAddDto): ConfigType<ResultVo<null>> => {
    return request({
        url: "/user/insert",
        method: 'POST',
        data: data
    });
}

/**
 * 根据id查询用户信息
 * @param id 用户id
 */
export const selectUserById = (id: string): ConfigType<ResultVo<UserVo>> => {
    return request({
        url: "/user/selectById?id=" + id,
        method: 'GET'
    });
}

/**
 * 修改用户
 * @param data 修改权限用户
 */
export const updateUser = (data: UserUpdateDto): ConfigType<ResultVo<null>> => {
    return request({
        url: "/user/update",
        method: 'POST',
        data: data
    });
}

/**
 * 删除用户根据用户id
 * @param id 用户id
 */
export const deleteUserById = (id: string): ConfigType<ResultVo<null>> => {
    return request({
        url: "/user/deleteById?id=" + id,
        method: 'GET'
    });
}

/**
 * 禁用/启用用户
 * @param data 入参
 */
export const disableEnableUser = (data: DisableEnableUserDto): ConfigType<ResultVo<null>> => {
    return request({
        url: "/user/disableEnableUser",
        method: 'POST',
        data
    });
}

/**
 * 分配角色
 * @param data 入参
 */
export const assignRole = (data: AssignRoleDto): ConfigType<ResultVo<null>> => {
    return request({
        url: "/user/assignRole",
        method: 'POST',
        data
    });
}