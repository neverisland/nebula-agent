/**
 * 消息记录查询入参
 */
export interface ChatRecordQueryDto {

    /**
     * 会话id
     */
    chatId?: string | null;

    /**
     * 查询数量
     */
    size: number;

    /**
     * 消息时间
     */
    messageData?: string | null;
}