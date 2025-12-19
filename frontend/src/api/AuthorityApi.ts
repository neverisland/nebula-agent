import request from "../utils/request";
import { ResultVo } from "../type/ResultVo";
import { AxiosResponse } from "axios";
import { SysPermissionVo } from "@/type/permission/SysPermissionVo.ts";

type ConfigType<T = ResultVo> = Promise<AxiosResponse<T>>

/**
 * 获取所有的权限列表
 */
export const selectAllAuthorityList = (): ConfigType<ResultVo<SysPermissionVo[]>> => {
    return request({
        url: "/permission/selectAllData",
        method: 'GET'
    });
}
