/**
 * 会话记录出参
 */
export interface ChatRecordVo {

    /**
     * 记录id
     */
    id: string;

    /**
     * 会话id
     */
    chatId: string;

    /**
     * 消息内容
     */
    content: string;

    /**
     * 消息类型 1 用户 2 助手
     */
    type: number;

    /**
     * 状态 1 发送中 2 发送成功 3 发送失败
     */
    status: number;

    /**
     * 消息时间
     */
    createTime: string;

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
    /**
     * 任务id
     */
    taskId: string;

    /**
     * 图片地址
     */
    imgPath: string;
}
