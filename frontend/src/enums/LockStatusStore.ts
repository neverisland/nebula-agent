import {defineStore} from 'pinia';

/**
 * 锁定状态枚举
 */
export const lockStatusStore = defineStore('lockStatus', {
    state: (): { type: boolean; description: string }[] => {
        return [
            { type: true, description: '未锁定' },  // 这里 TRUE 对应 未锁定
            { type: false, description: '锁定' },   // 这里 FALSE 对应 锁定
        ];
    },
    getters: {
        /**
         * 传入 type，获取对应描述
         */
        getDescriptionByType: (state) => {
            return (type: boolean): string => {
                return state.$state.find(item => item.type === type)?.description || '';
            };
        }
    }
});
