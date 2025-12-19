import { SysRoleVo } from "@/type/role/SysRoleVo";

/**
 * 用户数据列表页出参
 */
export interface UserPageVo {

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
    roles?: SysRoleVo[];
}
