package cn.yang.nebula.agent.business.sliding.verification.controller;

import cn.yang.nebula.agent.business.sliding.verification.dto.SlidingVerificationCodeDto;
import cn.yang.nebula.agent.business.sliding.verification.dto.SlidingVerificationCodeObtainDto;
import cn.yang.nebula.agent.business.sliding.verification.enums.SlidingVerificationCodeBusinessEnum;
import cn.yang.nebula.agent.business.sliding.verification.facade.SlidingVerificationCodeFacade;
import cn.yang.nebula.agent.enums.ErrorStatusCodeEnum;
import cn.yang.nebula.agent.enums.StatusCodeEnum;
import cn.yang.nebula.agent.exception.BusinessException;
import cn.yang.nebula.agent.exception.NullDataException;
import cn.yang.nebula.agent.vo.result.ResultFactory;
import cn.yang.nebula.agent.vo.result.ResultVo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

/**
 * 滑动验证码业务统一接口
 *
 * @author : QingHai
 */
@RestController
@RequestMapping("/sliding/verification/code")
public class SlidingVerificationCodeController {

    @Resource
    private SlidingVerificationCodeFacade slidingVerificationCodeFacade;


    /**
     * 获取滑动验证码
     *
     * @param slidingVerificationCodeObtainDto 获取滑动验证码入参
     * @return 滑动验证码数据
     */
    @PostMapping("/obtain")
    public ResultVo<SlidingVerificationCodeDto> getSlidingVerificationCode(@RequestBody SlidingVerificationCodeObtainDto slidingVerificationCodeObtainDto) {
        try {
            if (slidingVerificationCodeObtainDto.getBackgroundHeight() == null || slidingVerificationCodeObtainDto.getBackgroundWidth() == null ||
                    slidingVerificationCodeObtainDto.getVerifyHeight() == null || slidingVerificationCodeObtainDto.getVerifyWidth() == null || slidingVerificationCodeObtainDto.getVerifyRadius() == null) {
                throw new BusinessException(ErrorStatusCodeEnum.PARAMETER_VERIFICATION_EXCEPTION, "滑动验证码参数异常");
            }
            SlidingVerificationCodeBusinessEnum codeBusinessEnum = SlidingVerificationCodeBusinessEnum.selectBusinessEnumByBusiness(slidingVerificationCodeObtainDto.getBusiness());
            SlidingVerificationCodeDto slidingVerificationCodeDto = slidingVerificationCodeFacade.slideCaptchaGen(slidingVerificationCodeObtainDto, codeBusinessEnum);
            return ResultFactory.success(StatusCodeEnum.SUCCESS, null, slidingVerificationCodeDto);
        } catch (NullDataException e) {
            throw new BusinessException(ErrorStatusCodeEnum.PARAMETER_VERIFICATION_EXCEPTION, "业务标识异常");
        }
    }

    /**
     * 校验滑动验证码是否正确
     *
     * @param userSlideDistance            用户滑动的距离
     * @param cacheSlidingVerificationCode 缓存中的滑动验证码code
     * @param business                     滑动验证码业务
     * @return 是否成功
     */
    @GetMapping("/slideCaptchaCheck")
    public ResultVo<Boolean> slideCaptchaCheck(@RequestParam("userSlideDistance") Integer userSlideDistance,
                                               @RequestParam("cacheSlidingVerificationCode") String cacheSlidingVerificationCode,
                                               @RequestParam("business") String business) {
        try {
            SlidingVerificationCodeBusinessEnum businessEnum = SlidingVerificationCodeBusinessEnum.selectBusinessEnumByBusiness(business);
            return ResultFactory.success(StatusCodeEnum.SUCCESS, "滑动验证码校验完成", slidingVerificationCodeFacade.slideCaptchaCheck(userSlideDistance, cacheSlidingVerificationCode, businessEnum));
        } catch (NullDataException e) {
            throw new BusinessException(ErrorStatusCodeEnum.PARAMETER_VERIFICATION_EXCEPTION, "业务标识异常");
        }
    }
}
