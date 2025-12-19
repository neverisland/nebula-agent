import request from "../utils/request";
import { ResultVo } from "../type/ResultVo.ts";
import { AxiosResponse } from "axios";
import { SlidingVerificationCodeVo } from "@/type/slider-verify/SlidingVerificationCodeVo.ts";
import { SlidingVerificationCodeObtainDto } from "@/type/slider-verify/SlidingVerificationCodeObtainDto.ts";


type ConfigType<T = ResultVo> = Promise<AxiosResponse<T>>

/**
 * 获取滑动验证码
 * @param data 生成入参
 */
export const obtainSliderVerifyCode = (data: SlidingVerificationCodeObtainDto): ConfigType<ResultVo<SlidingVerificationCodeVo>> => {
    return request({
        url: "/sliding/verification/code/obtain",
        method: 'POST',
        data: data
    });
}