/**
 * 修改会话标题入参
 */
export interface UpdateChatTitleDto {

    /**
     * 会话id
     */
    chatId: string;

    /**
     * 标题
     */
    title: string;
}