/**
 * 滑动验证码出参
 */
export interface SlidingVerificationCodeVo {

    /**
     * 背景图片base64
     */
    backgroundImg: string;

    /**
     * 验证图片base64
     */
    verifyImg: string;

    /**
     * 验证图片所处底部高度
     */
    height: number;

    /**
     * 滑动验证码的校验code
     */
    code: string;
}
