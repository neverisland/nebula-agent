import {UserInfoRoleDto} from "@/type/authorization/UserInfoRoleDto.ts";
import {UserInfoDto} from "@/type/authorization/UserInfoDto.ts";

/**
 * 获取当前登录用户的信息出参
 */
export interface CurrentUserInfoDto {

    /**
     * 当前登录用户信息
     */
    userInfo: UserInfoDto;

    /**
     * 权限标识列表
     */
    permissions: string[];

    /**
     * 当前登录身份角色列表
     */
    roles: UserInfoRoleDto[];

}