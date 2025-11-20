import request from "../utils/request";
import {ResultVo} from "../type/ResultVo.ts";
import {AxiosResponse} from "axios";
import {SlidingVerificationCodeDto} from "@/type/slider-verify/SlidingVerificationCodeDto.ts";


type ConfigType<T = ResultVo> = Promise<AxiosResponse<T>>

/**
 * 获取滑动验证码
 * @param data 生成入参
 */
export const obtainSliderVerifyCode = (data: SlidingVerificationCodeObtainDto): ConfigType<ResultVo<SlidingVerificationCodeDto>> => {
    return request({
        url: "/sliding/verification/code/obtain",
        method: 'POST',
        data: data
    });
}