import request from "../utils/request";
import {ResultVo} from "../type/ResultVo";
import {AxiosResponse} from "axios";
import {SysPermissionDto} from "@/type/permission/SysPermissionDto.ts";

type ConfigType<T = ResultVo> = Promise<AxiosResponse<T>>

/**
 * 获取所有的权限列表
 */
export const selectAllAuthorityList = (): ConfigType<ResultVo<SysPermissionDto[]>> => {
    return request({
        url: "/permission/selectAllData",
        method: 'GET'
    });
}
