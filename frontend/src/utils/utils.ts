const USER_TOKEN_KEY = 'token'
const USERINFO_KEY = 'userinfo'
const ROLES_KEY = 'roles'
const PERMISSIONS_KEY = 'permissions'


/**
 * @description 判断是否存在用户token
 */
export const hasUserToken = (): boolean => {
    const token = getUserToken()
    return !!token;
}

/**
 * @description 获取用户token
 */
export const getUserToken = (): string => {
    const token = sessionStorage.getItem(USER_TOKEN_KEY)
    if (token) return token
    else return ''
}

/**
 * @description 设置用户token
 * @param token
 */
export const setUserToken = (token: string) => {
    sessionStorage.setItem(USER_TOKEN_KEY, token,)
}

/**
 * @description 删除用户token
 */
export const removeUserToken = () => {
    sessionStorage.removeItem(USER_TOKEN_KEY)
}

/**
 * 存储用户信息
 * @param userInfo 用户信息
 */
export const setUserInfo = (userInfo) => {
    localStorage.setItem(USERINFO_KEY, JSON.stringify(userInfo))
}

/**
 * 获取用户信息
 */
export const getUserInfo = () => {
    return JSON.parse(localStorage.getItem(USERINFO_KEY))
}

/**
 * 清除用户信息
 */
export const removeUserinfo = () => {
    if (localStorage.getItem(USERINFO_KEY)) {
        localStorage.removeItem(USERINFO_KEY)
    }
}

/**
 * 存储权限列表
 * @param permissions
 */
export const setPermission = (permissions) => {
    localStorage.setItem('permissions', JSON.stringify(permissions))
}

/**
 * 删除权限列表
 */
export const removePermission = () => {
    if (localStorage.getItem(PERMISSIONS_KEY)) {
        localStorage.removeItem(PERMISSIONS_KEY)
    }
}
/**
 * 获取权限列表
 */
export const getPermission = (): string[] => {
    if (localStorage.getItem(PERMISSIONS_KEY)) {
        const permissions: string | null = localStorage.getItem(PERMISSIONS_KEY)
        return permissions ? JSON.parse(permissions) : []
    } else {
        return []
    }
}

/**
 * 存储角色列表
 * @param roleList 角色列表
 */
export const setRoleList = (roleList) => {
    localStorage.setItem(ROLES_KEY, JSON.stringify(roleList))
}

/**
 * 获取角色列表
 */
export const getRoleList = () => {
    if (localStorage.getItem(ROLES_KEY)) {
        const roleList = localStorage.getItem(ROLES_KEY)
        return JSON.parse(roleList)
    } else {
        return []
    }
}

/**
 * 删除角色列表
 */
export const removeRoleList = () => {
    if (localStorage.getItem(ROLES_KEY)) {
        localStorage.removeItem(ROLES_KEY)
    }
}

/**
 * 清除用户所有的信息
 */
export const removeAllUserInfo = () => {
    // 清除token
    removeUserToken()
    // 清除用户信息
    removeUserinfo()
    // 删除权限列表
    removePermission()
    // 删除角色列表
    removeRoleList()
}
