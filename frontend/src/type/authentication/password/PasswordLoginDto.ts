/**
 * 账号密码登录
 */
export interface PasswordLoginDto {

    /**
     * 缓存中的滑动验证码code，缓存中的滑动验证码code
     */
    cacheSlidingVerificationCode: string;

    /**
     * 密码，密码
     */
    password: string;

    /**
     * 账号，用户名 / 密码
     */
    username: string;

    /**
     * 用户滑动的距离，用户滑动的距离
     */
    userSlideDistance: number;
}