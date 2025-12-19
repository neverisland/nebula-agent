/**
 * 新增会话出参
 */
export interface ChatVo {

    /**
     * 会话id
     */
    id: string;

    /**
     * 会话标题
     */
    title: string;

    /**
     * 模型标识
     */
    model: string;

    /**
     * 消息时间
     */
    createTime: string;
}
