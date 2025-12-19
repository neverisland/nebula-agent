<template>
  <a-layout style="min-height: 100vh;">
    <a-layout>
      <!-- 菜单 -->
      <a-layout-sider width="260" class="chat-sider">
        <ChatSession :new-chat="newChatData"/>
      </a-layout-sider>
      <!-- 内容 -->
      <a-layout>
        <!--  头部  -->
        <a-layout-header class="chat-header">
          <ChatHeader @change-prompt="handleChangePrompt" />
        </a-layout-header>
        <a-layout-content style="padding: 5px; height: calc(100vh - 84px);">
          <ChatContent @new-chat="handleNewChat" :new-prompt="newPromptData"/>
        </a-layout-content>
        <!-- 下角 -->
<!--        <a-layout-footer style="text-align: center;">-->
<!--          <a-typography-text type="secondary">少年不惧岁月长,彼方尚有荣光在</a-typography-text>-->
<!--        </a-layout-footer>-->
      </a-layout>
    </a-layout>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent} from 'vue'
import ChatContent from "@/views/chat/ChatContent.vue";
import ChatSession from "@/views/chat/ChatSession.vue";
import ChatHeader from "@/views/chat/ChatHeader.vue";
import {ChatVo} from "@/type/chat/ChatVo.ts";

export default defineComponent({
  name: "Chat",
  components: {ChatHeader, ChatSession, ChatContent},
  computed: {
  },
  data() {
    return {
      newChatData: null as ChatVo | null,
      newPromptData: '' as string,
    }
  },
  methods: {
    /**
     * 新建会话处理
     * @param newChat 新建的会话
     */
    handleNewChat(newChat: ChatVo) {
      this.newChatData = newChat;
    },
    handleChangePrompt(newPrompt: string) {
      this.newPromptData = newPrompt;
    }
  }
})
</script>


<style scoped>

:deep(.ant-layout-header) {
  padding: 0;
  height: 55px;
  line-height: 55px;
}

.chat-sider {
  overflow-y: auto;
  height: 100vh;
  background-color: var(--ant-color-bg-container);
}

.chat-header {
  height: 57px;
  padding: 0 20px;
  background-color: var(--ant-color-bg-container);
}

:deep(.ant-layout-footer) {
  padding: 3px 0;
}

</style>