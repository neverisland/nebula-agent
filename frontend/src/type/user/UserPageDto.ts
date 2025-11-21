/**
 * 用户数据出参
 */
export interface UserPageDto {

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
    nickName: string;

    /**
     * 手机号
     */
    phone: string;

    /**
     * 用户状态 true 正常 false 冻结
     */
    enabled: boolean;

    /**
     * 最后登录IP
     */
    loginIp: string;

    /**
     * 最后登录时间
     */
    loginDate: string;

    /**
     * 角色列表
     */
    roles?: any[];
}