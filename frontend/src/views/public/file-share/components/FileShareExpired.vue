<template>
  <div class="expired-container">
     <div class="expired-content">
         <div class="icon-box">
             <WarningOutlined style="font-size: 64px; color: #909399" />
         </div>
         <h2 class="title">分享已过期</h2>
         <p class="desc">该分享链接已超过有效期或已被取消分享，无法访问。</p>
         <p class="tip">您可以联系分享者重新获取链接。</p>
     </div>
  </div>
</template>

<script lang="ts">
import { WarningOutlined } from '@ant-design/icons-vue';

/**
 * 分享过期提示组件
 */
export default {
  name: "FileShareExpired",
  components: {
    WarningOutlined
  }
};
</script>

<style scoped>
.expired-container {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 60vh;
    padding: 20px;
}

.expired-content {
    text-align: center;
    max-width: 400px;
}

.icon-box {
    margin-bottom: 24px;
}

.title {
    color: #303133;
    font-size: 24px;
    margin-bottom: 12px;
}

.desc {
    color: #606266;
    margin-bottom: 8px;
    line-height: 1.5;
}

.tip {
    color: #909399;
    font-size: 13px;
}
</style>
