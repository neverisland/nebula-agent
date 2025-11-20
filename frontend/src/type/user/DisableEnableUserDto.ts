/**
 * 启用 / 禁用 用户 入参
 */
export interface DisableEnableUserDto {

    /**
     * 用户id
     */
    userId: string;

    /**
     * 禁用状态
     */
    enabled: string;
}