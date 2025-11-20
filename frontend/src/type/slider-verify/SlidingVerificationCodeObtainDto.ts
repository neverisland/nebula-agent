/**
 * 滑动验证码生成入参
 */
export interface SlidingVerificationCodeObtainDto {

    /**
     * 背景图片宽
     */
    backgroundWidth: number;

    /**
     * 背景图片高
     */
    backgroundHeight: number;

    /**
     * 验证图片宽
     */
    verifyWidth: number;

    /**
     * 验证图片高
     */
    verifyHeight: number;

    /**
     * 验证图片突出块半径
     */
    verifyRadius: number;

    /**
     * 业务标识
     */
    business: string;
}