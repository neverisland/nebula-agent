import request from "../utils/request";
import {ResultVo} from "../type/ResultVo";
import {AxiosResponse} from "axios";
import {NewChatDto} from "@/type/chat/NewChatDto.ts";
import {ChatDto} from "@/type/chat/ChatDto.ts";
import {ChatQueryDto} from "@/type/chat/ChatQueryDto.ts";
import {ChatRecordQueryDto} from "@/type/chat/ChatRecordQueryDto.ts";
import {ChatRecordDto} from "@/type/chat/ChatRecordDto.ts";
import {UpdateChatTitleDto} from "@/type/chat/UpdateChatTitleDto.ts";

type ConfigType<T = ResultVo> = Promise<AxiosResponse<T>>

/**
 * 新建会话
 */
export const newChat = (data: NewChatDto): ConfigType<ResultVo<ChatDto>> => {
    return request({
        url: "/chat/newChat",
        method: 'POST',
        data: data
    });
}

/**
 * 获取会话消息
 */
export const selectChatList = (data: ChatQueryDto): ConfigType<ResultVo<ChatDto[]>> => {
    return request({
        url: "/chat/selectChatList",
        method: 'POST',
        data: data,
    })
}

/**
 * 获取会话记录
 */
export const selectChatRecordList = (data: ChatRecordQueryDto): ConfigType<ResultVo<ChatRecordDto[]>> => {
    return request({
        url: "/chat/selectChatRecordList",
        method: 'POST',
        data: data,
    })
}

/**
 * 删除会话
 * @param chatId 会话id
 */
export const deleteChatById = (chatId: string): ConfigType<ResultVo<null>> => {
    return request({
        url: "/chat/deleteById?chatId=" + chatId,
        method: 'GET',
    })
}

/**
 * 修改会话标题
 * @param data 入参
 */
export const updateChatTitle = (data: UpdateChatTitleDto): ConfigType<ResultVo<null>> => {
    return request({
        url: "/chat/updateChatTitle",
        method: 'POST',
        data
    })
}

/**
 * 根据taskId获取消息记录信息
 * @param taskId 任务id
 */
export const selectChatRecordDtoByTaskId = (taskId: string): ConfigType<ResultVo<ChatRecordDto>> => {
    return request({
        url: "/chat/selectChatRecordDtoByTaskId?taskId=" + taskId,
        method: 'GET',
    })
}