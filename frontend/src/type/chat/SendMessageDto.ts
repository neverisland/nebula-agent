import {ChatConfig} from "@/type/chat/ChatConfig.ts";

/**
 * 发送消息入参
 */
export interface SendMessageDto extends ChatConfig {

    /**
     * 会话id
     */
    chatId: string;

    /**
     * 消息内容
     */
    content: string;
}