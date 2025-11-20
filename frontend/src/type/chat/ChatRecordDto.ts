/**
 * 会话记录详情
 */
export interface ChatRecordDto {
    /**
     * 会话记录主键id
     */
    id: string;

    /**
     * 会话消息id
     */
    chatId: string;

    /**
     * 会话消息类型
     */
    type: number;

    /**
     * 会话消息角色
     */
    role: string;

    /**
     * 会话内容
     */
    content: string;

    /**
     * 深度思考过程内容
     */
    deepThinkingContent: string;

    /**
     * 任务id
     */
    taskId: string;

    /**
     * 图片相对路径
     */
    imgPath: string;

    /**
     * 消息时间
     */
    createTime: string;
}