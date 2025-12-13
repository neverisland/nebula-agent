<template>
  <a-layout style="min-height: 100vh;">
    <!--  头部  -->
    <a-layout-header :style="headerStyle" style="height: 55px; padding: 0 20px;">
      <ConsoleMenuHeader/>
    </a-layout-header>
    <a-layout>
      <!-- 菜单 -->
      <a-layout-sider width="200" :style="siderStyle">
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
import store from "@/store/cache.ts";

export default {
  name: "ConsoleMenu",
  components: {ConsoleMenuContent, ConsoleMenuHeader},
  computed: {
    isDarkTheme() {
      return store.getters['theme/isDarkTheme'];
    },
    headerStyle() {
      return {
        backgroundColor: this.isDarkTheme ? '#141414' : '#ffffff',
        padding: '0 20px',
        height: '55px',
        lineHeight: '55px'
      }
    },
    siderStyle() {
      return {
        backgroundColor: this.isDarkTheme ? '#141414' : '#ffffff'
      }
    }
  }
}
</script>

<style scoped>
/* Removed :deep(.ant-layout-header) as it is now handled by inline style */

:deep(.ant-layout-footer) {
  padding: 3px 0;
}

</style>
