import request from "../utils/request";
import {ResultVo} from "../type/ResultVo.ts";
import {AxiosResponse} from "axios";
import {CurrentUserInfoDto} from "../type/authorization/CurrentUserInfoDto.ts";

type ConfigType<T = ResultVo> = Promise<AxiosResponse<T>>

/**
 * 获取当前登录用户信息
 */
export const getCurrentUserInfo = (): ConfigType<ResultVo<CurrentUserInfoDto>> => {
    return request({
        url: "/authentication/getCurrentUserInfo",
        method: 'GET'
    });
}

/**
 * 退出登录
 */
export const logout = (): ConfigType<ResultVo<null>> => {
    return request({
        url: "/authentication/logout",
        method: 'GET'
    })
}

/**
 * 校验当前用户是否已经认证
 */
export const verifyWhetherAuthenticated = (): ConfigType<ResultVo<boolean>> => {
    return request({
        url: "/authentication/verifyWhetherAuthenticated",
        method: 'GET'
    })
}