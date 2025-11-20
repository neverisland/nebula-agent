/**
 * 重置密码入参
 */
export interface ResetPasswordDto {

    /**
     * 用户id
     */
    userId: string;

    /**
     * 重置的密码
     */
    password: string;
}
