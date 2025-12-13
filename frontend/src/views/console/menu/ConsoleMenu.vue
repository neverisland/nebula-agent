<template>
  <a-layout style="min-height: 100vh;">
    <!--  头部  -->
    <a-layout-header class="console-menu-header" :style="{ borderBottom: `1px solid ${token.colorSplit}` }">
      <ConsoleMenuHeader/>
    </a-layout-header>
    <a-layout>
      <!-- 菜单 -->
      <a-layout-sider width="200" class="console-menu-sider" :style="{ borderRight: `1px solid ${token.colorSplit}` }">
        <ConsoleMenuContent/>
      </a-layout-sider>
      <!-- 内容 -->
      <a-layout>
        <a-layout-content style="padding: 5px; min-height: 280px;">
          <router-view/>
        </a-layout-content>
        <!-- 下角 -->
        <a-layout-footer style="text-align: center;">
          <a-typography-text type="secondary">少年不惧岁月长,彼方尚有荣光在</a-typography-text>
        </a-layout-footer>
      </a-layout>
    </a-layout>
  </a-layout>
</template>

<script lang="ts">
import ConsoleMenuHeader from "./ConsoleMenuHeader.vue";
import ConsoleMenuContent from "./ConsoleMenuContent.vue";
import { theme } from 'ant-design-vue';

export default {
  name: "ConsoleMenu",
  components: {ConsoleMenuContent, ConsoleMenuHeader},
  setup() {
    const { useToken } = theme;
    const { token } = useToken();
    return {
      token,
    };
  },
  computed: {
  }
}
</script>

<style scoped>
/* Removed :deep(.ant-layout-header) as it is now handled by inline style */

:deep(.ant-layout-footer) {
  padding: 3px 0;
}

.console-menu-header {
  height: 55px;
  padding: 0 20px;
  line-height: 55px;
  background-color: var(--ant-color-bg-container);
}

.console-menu-sider {
  background-color: var(--ant-color-bg-container);
}
</style>
