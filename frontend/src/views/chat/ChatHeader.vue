<template>
  <div class="navbar-wrapper">
    <div class="header-container">
      <!-- 左侧下拉菜单 -->
      <div class="left-section">
<!--        <a-select v-model:value="selectedPrompt" style="width: 140px;" @change="changeModelHandle" placeholder="Ai回答风格" allowClear>-->
<!--          <a-select-option v-for="prompt in modelOptions" :key="prompt.type" :value="prompt.type">-->
<!--            {{ prompt.description }}-->
<!--          </a-select-option>-->
<!--        </a-select>-->
      </div>

      <!-- 右侧内容 -->
      <div class="content">
        <a-avatar :size="28" :src="avatarUrl"/>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import {defineComponent} from "vue";
import defaultAvatar from "@/assets/img/default-avatar.png";
import {promptStore} from "@/enums/PromptStore.ts";

export default defineComponent({
  name: "ChatHeader",
  components: {},
  data() {
    return {
      avatarUrl: defaultAvatar,
      modelOptions: promptStore().$state,
      selectedPrompt: null as null | string,
    }
  },
  mounted() {
    this.$emit('change-prompt', this.selectedPrompt);
  },
  methods: {
    /**
     * 修改模型的处理事件
     */
    changeModelHandle(newPrompt: string | null) {
      console.log(newPrompt);
      // 触发事件并传参
      this.$emit('change-prompt', newPrompt);
    }
  }
})
</script>

<style scoped>
.navbar-wrapper {
  position: relative;
  margin: auto;
  height: var(--header-height);
}

.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 0 auto;
  padding: 0 16px;
}

.left-section {
  flex-grow: 0;
}

.content {
  display: flex;
  align-items: center;
}
</style>