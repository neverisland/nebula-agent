package cn.yang.nebula.agent.business.sliding.verification.dto;

import cn.yang.nebula.agent.utils.sliding.verification.code.dto.SlidingVerificationCodeBaseReturn;

/**
 * 滑动验证码出参,渲染展示
 *
 * @author : QingHai
 */
public class SlidingVerificationCodeDto extends SlidingVerificationCodeBaseReturn {

    /**
     * 滑动验证码的校验code
     */
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "SlidingVerificationCodeDto{" +
                "code='" + code + '\'' +
                "} " + super.toString();
    }

}
