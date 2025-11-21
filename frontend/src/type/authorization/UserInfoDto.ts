/**
 * 用户信息
 */
/**
 * 用户信息
 */
export interface UserInfoDto {
    /**
     * 当前登录用户id
     */
    userId: string;

    /**
     * 昵称
     */
    nickname: string;

    /**
     * 手机号
     */
    phone: string;

    /**
     * 邮箱
     */
    email?: string;

    /**
     * 角色列表
     */
    roles?: any[];
}