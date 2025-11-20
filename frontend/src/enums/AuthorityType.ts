import {defineStore} from "pinia";

/**
 * 权限的枚举
 * M目录 V页面 B按钮 I接口
 */
export const authorityTypeStore = defineStore('authorityType', {
    state: (): { type: string, description: string }[] => {
        return [
            {type: 'M', description: '目录'},
            {type: 'V', description: '页面'},
            {type: 'B', description: '按钮'},
            {type: 'I', description: '接口'}
        ]
    },
    getters: {
        getAuthorityType: (state): { type: string, description: string }[] => {
            return state;
        },
    }
})