import { ChatConfig } from "@/type/chat/ChatConfig.ts";

/**
 * 新建会话入参
 */
export interface NewChatDto extends ChatConfig {

    /**
     * 消息内容
     */
    content: string;
}