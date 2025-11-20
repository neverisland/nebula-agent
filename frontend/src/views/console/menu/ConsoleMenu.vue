<template>
  <a-layout theme="light" style="min-height: 100vh;">
    <!--  头部  -->
    <a-layout-header style="height: 55px; padding: 0 20px; border-bottom: 1px solid #f0f0f0;">
      <ConsoleMenuHeader/>
    </a-layout-header>
    <a-layout>
      <!-- 菜单 -->
      <a-layout-sider theme="light" width="200" style="border-right: 1px solid #f0f0f0;">
        <ConsoleMenuContent/>
      </a-layout-sider>
      <!-- 内容 -->
      <a-layout>
        <a-layout-content style="padding: 5px; min-height: 280px;">
          <router-view/>
        </a-layout-content>
        <!-- 下角 -->
        <a-layout-footer style="background-color: rgb(0, 0, 0, 2%); text-align: center;">
          <a-typography-text type="secondary">少年不惧岁月长,彼方尚有荣光在</a-typography-text>
        </a-layout-footer>
      </a-layout>
    </a-layout>
  </a-layout>
</template>

<script lang="ts">
import ConsoleMenuHeader from "./ConsoleMenuHeader.vue";
import ConsoleMenuContent from "./ConsoleMenuContent.vue";

export default {
  name: "ConsoleMenu",
  components: {ConsoleMenuContent, ConsoleMenuHeader},
}
</script>

<style scoped>
:deep(.ant-layout) {
  background: #fff;
}

:deep(.ant-layout-header) {
  padding: 0;
  height: 55px;
  line-height: 55px;
  background: #fff;
}

:deep(.ant-layout-footer) {
  padding: 3px 0;
}

header {
  border-bottom: 1px solid #dcdfe6;
}

</style>
