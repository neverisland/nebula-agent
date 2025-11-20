/**
 * 会话通用设置
 */
export interface ChatConfig {

    /**
     * 模型标识
     */
    model: string;

    /**
     * 是否联网
     */
    networking: boolean;

    /**
     * 是否深度思考
     */
    deepThinking: boolean;
}