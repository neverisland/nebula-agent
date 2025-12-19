/**
 * 用户修改入参
 */
export interface UserUpdateDto {

    /**
     * 用户id
     */
    id: string;

    /**
     * 姓名
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
     * 角色id列表
     */
    roleIdList: string[];
}