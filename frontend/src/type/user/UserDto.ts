/**
 * 用户详情
 */
export interface UserDto {

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
     * 头像
     */
    avatar: string;

    /**
     * 用户状态 true 正常 false 冻结
     */
    enabled: boolean;

    /**
     * 是否未锁定 false 锁定 true 解锁
     */
    accountNonLocked: boolean;

    /**
     * 注册时间
     */
    createTime: string;
}