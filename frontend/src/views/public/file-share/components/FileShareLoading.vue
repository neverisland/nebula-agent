<template>
  <div v-if="loading" class="global-loading">
    <a-spin size="large" tip="加载中...">
      <div class="loading-content"></div>
    </a-spin>
  </div>
</template>

<script lang="ts">
/**
 * 全局加载组件
 */
export default {
  name: "FileShareLoading",
  props: {
    loading: {
      type: Boolean,
      default: false
    }
  }
};
</script>

<style scoped>
.global-loading {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 9999;
  background-color: rgba(255, 255, 255, 0.9);
  display: flex;
  align-items: center;
  justify-content: center;
}

.loading-content {
  width: 200px;
  height: 200px;
}
</style>

