import {defineStore} from 'pinia';

/**
 * 角色类型枚举
 */
export const roleEnumStore = defineStore('roleEnum', {
    state: (): { type: number; description: string }[] => {
        return [
            { type: 0, description: '内置角色' },
            { type: 1, description: '自定义角色' },
        ];
    },
    getters: {
        /**
         * 传入 type，获取对应描述
         */
        getDescriptionByType: (state) => {
            return (type: number): string => {
                return state.$state.find(item => item.type === type)?.description || '';
            };
        }
    }
});
