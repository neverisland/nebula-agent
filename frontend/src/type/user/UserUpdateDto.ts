/**
 * 修改用户入参
 */
export interface UserUpdateDto {

    /**
     * 用户id
     */
    id: string;

    /**
     * 用户名
     */
    username: string;

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
    email: string;

    /**
     * 角色ID列表
     */
    roleIdList: string[];
}