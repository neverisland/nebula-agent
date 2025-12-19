import { UserInfoRoleVo } from "@/type/authorization/UserInfoRoleVo.ts";
import { UserInfoVo } from "@/type/authorization/UserInfoVo.ts";

/**
 * 获取当前登录用户的信息出参
 */
export interface CurrentUserInfoVo {

    /**
     * 当前登录用户信息
     */
    userInfo: UserInfoVo;

    /**
     * 权限标识列表
     */
    permissions: string[];

    /**
     * 当前登录身份角色列表
     */
    roles: UserInfoRoleVo[];
}
