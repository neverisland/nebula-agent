import {defineStore} from "pinia";

/**
 * 是否禁用状态的枚举
 */
export const enabledEnumStore = defineStore('enable', {
    state: (): { type: boolean, description: string }[] => {
        return [
            {type: true, description: '正常'},
            {type: false, description: '禁用'},
        ]
    },
    getters: {
        /**
         * 根据type获取description
         * @param state
         */
        getDescriptionByType: (state) => {
            return (type: boolean): string => {
                return state.$state.find(item => item.type === type)?.description || '';
            };
        }
    }
})