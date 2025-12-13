<template>
  <div class="chat-window">
    <!-- 消息区域 -->
    <div class="message-list" ref="messageListRef" @scroll="handleScrollTop">
      <a-list :data-source="chatRecordList">
        <template #renderItem="{ item }">
          <a-list-item :class="['message-item', item.role]">
            <!-- 用户消息：居右气泡 -->
            <div v-if="item.role === 'user'" class="user-message-bubble">
              {{ item.content }}
            </div>
            <!-- AI 回复消息 -->
            <div v-else>
              <div v-if="item.type === 1">
                <v-md-preview style="width: 100%" :text="item.content"></v-md-preview>
              </div>
              <div v-else-if="item.type === 2">
                <a-image
                    :width="265"
                    :src="'/api/file' + item.imgPath"
                    fallback="data:image/png;base64,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"
                />
              </div>
            </div>
          </a-list-item>
        </template>
      </a-list>
    </div>

    <!-- 发送区域 -->
    <div class="input-area">
      <a-input v-model:value="inputText" placeholder="输入消息..." @change="inputTextChange" @pressEnter="sendMessage"/>
      <a-button type="primary" @click="sendMessage" :disabled="disabled" :loading="loading">发送</a-button>
    </div>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {newChat, selectChatRecordDtoByTaskId, selectChatRecordList} from "@/api/ChatApi.ts";
import {message} from "ant-design-vue";
import {readStream} from "@/utils/streamReader.ts";
import {ChatRecordDto} from "@/type/chat/ChatRecordDto.ts";
import {format} from "date-fns";
import {PollingTimer} from "@/utils/PollingTimer.ts";
import {SendMessageResultDto} from "@/type/chat/SendMessageResultDto.ts";
import {ChatRecordTypeEnum} from "@/enums/ChatRecordTypeStore.ts";

export default defineComponent({
  name: "ChatContent",
  components: {},
  props: {
    newPrompt: {
      type: String,
      default: null
    }
  },
  watch: {
    /**
     * 监听会话id参数
     * @param newVal 新值
     * @param oldVal 旧值
     */
    '$route.query.s'(newVal, oldVal) {
      this.isEnd = false
      if (newVal) {
        this.changeChat(newVal);
      } else {
        this.chatRecordList = []
        this.chatId = null
      }
    },
    newPrompt(newPrompt: string) {
      if (newPrompt) {
        this.prompt = newPrompt;
      } else {
        this.prompt = null;
      }
    }
  },
  data() {
    return {
      chatId: null as null | string, // 当前会话的id
      chatRecordList: [] as ChatRecordDto[], // 当前会话的内容
      inputText: '', // 输入的文本
      loading: false, // 发送按钮的加载状态
      disabled: true, // 发送按钮的禁用状态
      oldestMessageTime: null as null | string, // 获取到的最久的消息时间
      isEnd: false, // 是否还有旧消息标记
      messageListRef: null as HTMLElement | null, // 消息区域dom元素
      prompt: '', // 风格标识
    }
  },
  mounted() {
    if (this.$route.query.s) {
      this.changeChat(this.$route.query.s as string);
    }
  },
  methods: {
    /**
     * 根据会话id改变会话内容
     * @param chatId 会话id
     */
    changeChat(chatId: string) {
      if (this.chatId === chatId) {
        return
      }
      // 修改会话id
      this.chatId = chatId;
      this.chatRecordList = [];
      this.isEnd = false;
      this.oldestMessageTime = null;
      // 获取会话数据
      this.selectChatRecord();
    },
    handleScrollTop(e) {
      const container = e.target;
      if (!container) return;
      // 加载小一点
      if (container.scrollTop <= 1 && !this.loading && !this.isEnd) {
        this.selectChatRecord();
      }
    },
    /**
     * 获取会话历史记录
     */
    selectChatRecord() {
      if (this.isEnd) {
        console.log('已获取所有的消息了')
        return;
      }
      const param = {
        chatId: this.chatId,
        size: 10,
        messageData: this.oldestMessageTime
      }
      selectChatRecordList(param).then(res => {
        if (res.data.code === 0) {
          if (res.data.data.length > 0) {
            this.chatRecordList = [...res.data.data, ...this.chatRecordList];
            // 一开始的时候进行滚动到底部
            if (!this.oldestMessageTime) {
              this.scrollToBottom(true); // 滚动到底部
            }
            this.isLoad = true;
            this.oldestMessageTime = res.data.data[0].createTime
            // 记录是否存在没有图片的消息
            this.handlingUnGeneratedImages(res.data.data);
          }
          if (res.data.data.length < 10) {
            this.isEnd = true;
          }
        }
      })
    },
    /**
     * 处理未生成的图片
     * @param dataList 消息记录列表
     */
    handlingUnGeneratedImages(dataList: ChatRecordDto[]) {
      for (let data of dataList) {
        if (data.type === 2 && !data.imgPath) {
          const timer = new PollingTimer(() => this.handlerChatImageRecord(data.taskId), 1000);
          timer.start();
        }
      }
    },
    /**
     * 处理消息图片信息
     * @param taskId 图片任务id
     */
    async handlerChatImageRecord(taskId: string): Promise<boolean> {
      try {
        let response = await selectChatRecordDtoByTaskId(taskId);
        // 表示图片有了，进行数据替换
        if (response.data.code === 0 && response.data.data.imgPath) {
          for (let i = 0; i < this.chatRecordList.length; i++) {
            if (this.chatRecordList[i].taskId === taskId) {
              this.chatRecordList.splice(i, 1, response.data.data);
              return true;
            }
          }
        }
        return false;
      } catch (error) {
        console.error('请求失败:', error)
        return false;
      }
    },
    /**
     * 新建会话
     */
    newChat() {
      // 初始化数据
      this.chatId = null;
      this.chatRecordList = [];
      this.inputText = '';
    },
    /**
     * 消息文本框的有数据后显示按钮
     */
    inputTextChange(e: InputEvent) {
      this.disabled = !e.data;
    },
    /**
     * 发送消息
     */
    sendMessage() {
      this.switchSendMessageStatus(true);
      // 判断当前是否是新的会话
      if (this.chatId) {
        // 自定义的消息
        const chatRecord = {
          chatId: this.chatId,
          role: 'user',
          content: this.inputText,
          createTime: format(new Date(), 'yyyy-MM-dd HH:mm:ss')
        }
        this.chatRecordList.push(chatRecord);
        this.scrollToBottom(); // 滚动到底部
        this.sendMessageReq();
      } else {
        this.newChat();
      }
    },
    /**
     * 发送消息
     */
    sendMessageReq: function () {
      const param = {
        content: this.inputText,
        chatId: this.chatId,
        prompt: this.prompt
      }
      // 自定义的消息
      const chatRecord = {
        chatId: this.chatId,
        type: ChatRecordTypeEnum.TEXT,
        role: 'assistant',
        content: '',
        createTime: format(new Date(), 'yyyy-MM-dd HH:mm:ss')
      }
      this.chatRecordList.push(chatRecord)
      readStream({
        url: '/api/chat/sendMessage',
        body: param,
        onMessage: (msg: SendMessageResultDto) => {
          let message = JSON.parse(msg);
          const lastIndex = this.chatRecordList.length - 1
          const lastItem = this.chatRecordList[lastIndex]
          if (message.type === ChatRecordTypeEnum.TEXT) {
            // 文本的处理逻辑
            this.chatRecordList.splice(lastIndex, 1, {
              ...lastItem,
              content: lastItem.content + message.content
            })
          } else if (message.type === ChatRecordTypeEnum.PICTURE) {
            // 图片的处理逻辑
            this.chatRecordList.splice(lastIndex, 1, {
              ...lastItem,
              taskId: message.taskId,
              type: ChatRecordTypeEnum.PICTURE
            })
            // 新增定时任务
            const timer = new PollingTimer(() => this.handlerChatImageRecord(message.taskId), 1000);
            timer.start();
          }
          this.scrollToBottom(); // 滚动到底部
        },
        onFinish: () => {
          console.log('完成了')
        },
        onError: (err) => {
          message.error('消息发送失败,请重试');
          console.error('出错了:', err)
        },
        debug: true,
      }).finally(f => {
        this.switchSendMessageStatus();
      })
    },
    /**
     * 新建会话
     */
    newChat() {
      const param = {
        content: this.inputText
      }
      newChat(param).then(res => {
        if (res.data.code == 0) {
          // 成功之后，设置会话id
          this.chatId = res.data.data.id;
          this.$router.push({path: '/chat', query: {'s': this.chatId}})
          // 触发事件并传参
          this.$emit('new-chat', res.data.data);
          // 向上发送会话请求
          this.sendMessage();
        } else {
          message.error('消息发送失败,请重试');
          this.switchSendMessageStatus();
        }
      }).catch(e => {
        message.error('消息发送失败,请重试');
        console.log(e)
        this.switchSendMessageStatus();
      })
    },
    /**
     * 切换发送消息状态
     * @param flag 是否切换加载状态
     */
    switchSendMessageStatus(flag: boolean = false) {
      if (flag) {
        this.loading = true;
      } else {
        this.inputText = '';
        this.loading = false;
      }
    },
    /**
     * 判断是否需要自动置底
     */
    shouldAutoScroll() {
      const container = this.$refs.messageListRef;
      if (!container) return false;
      // 判断当前是否已经接近底部（允许误差 50px）
      const threshold = 100;
      const distanceFromBottom = container.scrollHeight - container.scrollTop - container.clientHeight;
      return distanceFromBottom <= threshold;
    },
    /**
     * 滚动到底部
     */
    scrollToBottom(force = false) {
      console.log('触发滚动到底部..')
      this.$nextTick(() => {
        const container = this.$refs.messageListRef;
        if (container && (force || this.shouldAutoScroll())) {
          container.scrollTop = container.scrollHeight;
        }
      });
    }
  }
})
</script>

<style scoped>

.chat-window {
  display: flex;
  flex-direction: column;
  height: 100%;
  max-width: 800px;
  margin: 0 auto;
  padding: 16px;
}

.message-list {
  flex: 1;
  overflow-y: auto;
  padding-bottom: 16px;
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE/Edge */
  scroll-behavior: smooth;
}

.message-list::-webkit-scrollbar {
  display: none; /* Chrome/Safari */
}

.input-area {
  display: flex;
  gap: 8px;
  margin-top: 8px;
}

.message-item {
  display: flex;
  justify-content: flex-start;
  margin-bottom: 12px;
}

.message-item.user {
  justify-content: flex-end;
}

/* 用户消息气泡 */
.user-message-bubble {
  max-width: 70%;
  border-radius: 8px;
  padding: 8px 12px;
  word-break: break-word;
}


/* 隐藏 a-comment 中多余的边距和背景 */
:deep(.ant-comment-inner) {
  background-color: transparent !important;
  padding: 0 !important;
}

</style>