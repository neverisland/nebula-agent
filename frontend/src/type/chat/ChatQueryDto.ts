/**
 * 消息查询入参
 */
export interface ChatQueryDto {

    /**
     * 查询数量
     */
    size: number;

    /**
     * 消息时间
     */
    chatTime?: string | null;
}