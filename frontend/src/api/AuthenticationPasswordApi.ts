import request from "../utils/request";
import {ResultVo} from "../type/ResultVo.ts";
import {AxiosResponse} from "axios";
import {PasswordLoginDto} from "@/type/authentication/password/PasswordLoginDto.ts";
import {ResetPasswordDto} from "@/type/authentication/password/ResetPasswordDto.ts";

type ConfigType<T = ResultVo> = Promise<AxiosResponse<T>>

/**
 * 账号密码登录
 * @param data 登录入参
 */
export const authenticationPasswordLogin = (data: PasswordLoginDto): ConfigType<ResultVo<string>> => {
    return request({
        url: "/authentication/password/login",
        method: 'POST',
        data: data
    });
}

/**
 * 重置密码
 * @param data 入参
 */
export const resetPassword = (data: ResetPasswordDto): ConfigType<ResultVo<null>> => {
    return request({
        url: "/authentication/password/resetPassword",
        method: 'POST',
        data: data
    });
}