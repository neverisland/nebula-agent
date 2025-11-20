<template>
  <div class="conversation-container">
    <!-- 新建对话按钮 -->
    <div class="new-conversation">
      <a-button type="primary" block @click="createNewConversation">新建对话</a-button>
    </div>

    <!-- 会话列表（可滚动区域） -->
    <div class="conversation-scroll" ref="listContainer" @scroll="handleScroll">
      <a-list :dataSource="chatList">
        <template #renderItem="{ item }">
          <!-- 会话项 -->
          <a-list-item class="conversation-item"
                       :class="{ active: chatId === item.id }"
                       @click="handleClick(item)"
                       :hoverable="true">
            <a-list-item-meta :title="item.title"/>
            <template #actions>
              <a-dropdown :trigger="['click']">
                <a class="ant-dropdown-link" @click.stop>
                  ···
                </a>
                <template #overlay>
                  <a-menu>
                    <a-menu-item @click="editTitle(item)">修改标题</a-menu-item>
                    <a-menu-item @click="deleteItem(item.id)">删除</a-menu-item>
                  </a-menu>
                </template>
              </a-dropdown>
            </template>
          </a-list-item>
        </template>
      </a-list>

      <!-- 加载提示 -->
      <div v-if="loadingMore" class="loading-more">加载中...</div>
    </div>
  </div>

  <a-modal v-model:open="dialogUpdateChatTitle" title="修改标题" :width="400" :footer="null">
    <UpdateChatTitle :selectId="selectId" :selectTitle="selectTitle" @closeDialogUpdateChatTitle="closeDialogUpdateChatTitle"/>
  </a-modal>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import {deleteChatById, selectChatList} from "@/api/ChatApi.ts";
import {ChatDto} from "@/type/chat/ChatDto.ts";
import {message, Modal} from "ant-design-vue";
import UpdateChatTitle from "@/views/chat/UpdateChatTitle.vue";

export default defineComponent({
  name: "ChatSession",
  components: {UpdateChatTitle},
  props: {
    newChat: {
      type: Object,
      default: null
    }
  },
  watch: {
    newChat(newChatDto: ChatDto) {
      if (newChatDto) {
        this.chatList.unshift(newChatDto);
        this.chatId = newChatDto.id
      }
    }
  },
  data() {
    return {
      chatList: [] as ChatDto[], // 消息记录列表
      loadingMore: false,
      chatId: null as null | string, // 当前会话
      lastChatTime: null as null | string, // 最后的消息时间
      isEnd: false, // 标记是否没有会话数据
      dialogUpdateChatTitle: false, // 修改密码的弹窗
      selectId: '', // 选中的id
      selectTitle: '', // 选中的title
    };
  },
  mounted() {
    // 加载数据
    this.loadMore();
    if (this.$route.query.s) {
      this.chatId = this.$route.query.s as string;
    }
  },
  methods: {
    handleClick(item) {
      this.chatId = item.id;
      // 跳转路由
      this.$router.push({path: '/chat', query: {'s': item.id}})
    },
    /**
     * 修改标题
     * @param item 数据
     */
    editTitle(item) {
      this.selectId = item.id;
      this.selectTitle = item.title;
      this.dialogUpdateChatTitle = true;
    },
    /**
     * 删除会话
     * @param chatId 会话id
     */
    deleteItem(chatId: string) {
      Modal.confirm({
        title: '确定删除这一条对话记录吗?',
        content: `删除后对话记录无法恢复和找回，请谨慎操作`,
        okText: '确定删除',
        okType: 'danger',
        cancelText: '取消',
        onOk: () => {
          deleteChatById(chatId).then(res => {
            if (res.data.code === 0) {
              this.chatList = this.chatList.filter(c => c.id !== chatId);
              // 如果删除的是当前id，新建会话
              if (this.chatId === chatId) {
                this.createNewConversation();
              }
              message.success('删除成功');
            } else {
              message.error('删除失败：' + res.data.details);
            }
          }).catch(e => {
            message.error('删除失败');
          })
        }
      });
    },
    handleScroll(e) {
      const container = e.target;
      if (container.scrollHeight - container.scrollTop <= container.clientHeight + 10 && !this.loadingMore) {
        this.loadMore();
      }
    },
    /**
     * 加载更多
     */
    loadMore() {
      if (this.loadingMore || this.isEnd) {
        console.log('加载中或已获取所有会话..')
        return;
      }

      this.loadingMore = true;
      const chatQuery = {
        size: 20,
        chatTime: this.lastChatTime
      }
      selectChatList(chatQuery).then(res => {
        if (res.data.code == 0) {
          if (res.data.data.length > 0) {
            this.chatList = [...this.chatList, ...res.data.data];
            this.lastChatTime = res.data.data[res.data.data.length - 1].createTime
          }
          // 校验路由的会话id不存在会话列表，新建会话(这种情况是因为如果用户使用了最久远的会话id,但进行了刷新,是没有这个会话的)
          if (this.chatList.length > 0 && this.chatId && !this.chatList.find(chat => chat.id === this.chatId)) {
            this.createNewConversation();
          }
          if (res.data.data.length < 20) {
            this.isEnd = true;
          }
        } else {
          console.warn(res.data.details);
          message.error('获取消息失败')
        }
      }).catch(e => {
        console.error('获取消息失败', e)
        message.error('系统异常,获取消息失败')
      }).finally(f => {
        this.loadingMore = false;
      })
    },
    /**
     * 新建会话
     */
    createNewConversation() {
      this.chatId = null;
      this.$router.push({path: '/chat'})
    },
    /**
     * 关闭修改标题弹窗
     * @param flag 是否更新标题
     * @param newTitle 更新标题
     */
    closeDialogUpdateChatTitle(flag: boolean, newTitle: string) {
      this.dialogUpdateChatTitle = false;
      // 更新标题
      if (flag) {
        for (let i = 0; i < this.chatList.length; i++) {
          if (this.chatList[i].id === this.selectId) {
            this.chatList[i].title = newTitle
            return;
          }
        }
      }
    },
  }
})
</script>

<style scoped>

.conversation-container {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.new-conversation {
  padding: 12px;
  background-color: #fff;
  border-bottom: 1px solid #e8e8e8;
  z-index: 1;
}

.conversation-scroll {
  flex: 1;
  overflow-y: auto;
  padding: 12px;
  background-color: #fafafa;
}

.conversation-item {
  cursor: pointer;
  transition: background-color 0.2s ease;
  position: relative;
}

.conversation-item:hover {
  background-color: #f0f0f0;
}

.conversation-item.active {
  background-color: #e6f4ff;
  border-left: 3px solid #1890ff;
}

:deep(.conversation-item .ant-list-item-action) {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-inline-start: 10px;
}

.loading-more {
  text-align: center;
  padding: 10px;
  color: #999;
}

:deep(.conversation-item .ant-list-item-meta-title) {
  white-space: nowrap; /*强制文本不换行*/
  overflow: hidden; /*隐藏超出部分*/
  text-overflow: ellipsis; /*显示省略号（...）表示被截断*/
}

</style>