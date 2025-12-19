/**
 * 消息发送的返回信息
 */
export interface SendMessageResultVo {

    /**
     * 发送响应的id
     */
    requestId: string;

    /**
     * 消息类型
     */
    type: number;

    /**
     * 文本消息内容
     */
    content: string;

    /**
     * 图片消息任务id
     */
    taskId: string;
}
