import {AllState} from '@/store/cache.ts'
import {ActionContext} from 'vuex'
import {
    getPermission,
    getUserInfo,
    getUserToken,
    removeAllUserInfo,
    setPermission,
    setRoleList,
    setUserInfo,
    setUserToken
} from '@/utils/utils'
import {getCurrentUserInfo, logout} from "@/api/AuthorizationApi.ts";
import {UserInfoRoleDto} from "@/type/authorization/UserInfoRoleDto.ts";
import {UserInfoDto} from "@/type/authorization/UserInfoDto.ts";

export type UserState = {
    token: string,
    roles?: UserInfoRoleDto[],
    permissions: Array<string>,
    userInfo?: UserInfoDto | null,
}

const state: UserState = {
    // token标识
    token: getUserToken(),
    // 角色(鉴权)
    roles: [],
    // 用户权限
    permissions: getPermission(),
    // 用户信息
    userInfo: getUserInfo(),
}

const user = {

    namespaced: true,

    state,

    mutations: {

        // 设置token
        setToken(state: UserState, token: string) {
            state.token = token
            setUserToken(token)
        },

        // 设置用户信息
        setUserInfo(state: UserState, info: UserState) {
            const {userInfo, permissions, roles} = info
            state.permissions = permissions;
            state.roles = roles;
            state.userInfo = userInfo;
            setPermission(permissions);
            setUserInfo(userInfo);
            setRoleList(roles);
        },

        // 清除登录状态及用户信息
        clearLoginState(state: UserState) {
            removeAllUserInfo()
            localStorage.clear()
            // 为了重新加载用户信息及路由组
            state.token = '';
            state.userInfo = null;
            state.permissions = [];
        },

        // 设置用户权限
        setPermissions(state: UserState, permissions: Array<string>) {
            state.permissions = permissions;
        },
    },

    getters: {
        getUserName: (state: UserState) => {
            if (state.userInfo) {
                return state.userInfo.nickname
            } else if (getUserInfo()) {
                const userInfo = getUserInfo()
                return userInfo.name
            } else {
                return '游客'
            }
        },

        /** 获取用户token */
        getUserToken(state: UserState) {
            if (state.token) {
                return state.token
            } else {
                return getUserToken()
            }
        },

        /**获取权限列表*/
        getPermissionList: (state: UserState) => {
            if (state.permissions.length > 0) {
                return state.permissions
            } else {
                //TODO判断权限为空时跳转登录，需考虑多个登录入口的情况
                user.mutations.setPermissions(state, getPermission())
                return getPermission()
            }
        },

        /**获取角色列表*/
        getRoleList: (state: UserState) => {
            if (state.roles && state.roles.length > 0) {
                return state.roles
            } else {
                user.mutations.setPermissions(state, getPermission())
                return getPermission()
            }
        },

        /**
         * 获取用户信息
         * */
        getUserInfo: (state: UserState) => {
            if (state.userInfo && Object.keys(state.userInfo).length !== 0) {
                return state.userInfo
            } else if (getUserInfo()){
                return getUserInfo()
            } else {
                return {}
            }
        },
    },

    actions: {
        /**
         * 登录后获取用户信息,并跳转页面
         * @param context
         */
        loginGetUserInfo(context: ActionContext<UserState, AllState>) {
            return new Promise((resolve, reject) => {
                getCurrentUserInfo().then((e: any) => {
                    const data = e.data
                    if (data.code === 0) {
                        // 存储用户信息
                        context.commit('setUserInfo', data.data)
                        resolve(data)
                    } else {
                        reject(data)
                    }
                }).catch((err: any) => {
                    reject(new Error(err))
                })
            })
        },
        // 登出
        logout(context: ActionContext<UserState, AllState>) {
            return new Promise((resolve, reject) => {
                logout().then((e: any) => {
                    const data = e.data
                    context.commit('clearLoginState')
                    resolve(data)
                }).catch((err: any) => {
                    reject(new Error(err))
                })
            })
        },
        // 清除缓存数据
        clearLoginState(context: ActionContext<UserState, AllState>) {
            context.commit('clearLoginState')
        }
    }
}

export default user
