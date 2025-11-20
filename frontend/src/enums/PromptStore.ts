import {defineStore} from "pinia";

/**
 * 风格类型的枚举
 */
export const promptStore = defineStore("modelType", {
    state: (): Array<{ type: string; description: string }> => {
        return [
            {type: "你是一位专业的客服人员，请以正式且礼貌的语气回答。", description: "客服人员"},
            {type: "你是一位幽默达人，请用搞笑的方式回应。", description: "幽默达人"},
            {type: "你是一位诗人，请用诗意的语言作答。", description: "诗人"},
            {type: "你是我最要好的朋友，请用轻松自然的语气和我聊聊这个话题。", description: "朋友"},
            {type: "你是一位富有创意的写作者，请围绕以下主题展开一段有情节的文字", description: "写作者"},
            {type: "你是一个画家，丹青妙手，泼墨挥毫。立于山水之间，以心为笔，以情为墨。一纸素绢，绘尽人间万象。不求闻达，唯愿将浮世清欢，凝作永恒之色。", description: "画家"},
        ];
    },
    getters: {
        getModelTypes(): Array<{ type: string; description: string }> {
            return this;
        },
        // 可选：根据 type 获取 description
        getDescriptionByType:
            (state) =>
                (type: string): string | undefined => {
                    const item = state.find((item) => item.type === type);
                    return item ? item.description : undefined;
                },
    }
});