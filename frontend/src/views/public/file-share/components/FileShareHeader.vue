<script setup lang="ts">
import { computed } from 'vue';
import type { FileSharePublicVo } from "@/type/file-share/vo/FileSharePublicVo";
import { DownloadOutlined, ClockCircleOutlined, UserOutlined } from '@ant-design/icons-vue';

/**
 * 文件分享头部组件
 * 
 * @author 
 */

interface Props {
  info: FileSharePublicVo;
}

const props = defineProps<Props>();

// 格式化有效期
const expireText = computed(() => {
  if (!props.info.enableExpire) {
    return '永久有效';
  }
  return `有效期至: ${props.info.expireTime}`;
});

</script>

<template>
  <div class="share-header">
    <div class="header-content">
      <div class="info-left">
        <div class="avatar-box">
            <img src="https://cdn-icons-png.flaticon.com/512/3767/3767084.png" alt="logo" class="avatar-img"/>
        </div>
        <div class="text-info">
          <h1 class="share-title">{{ info.name }}</h1>
          <div class="share-meta">
             <span class="meta-item">
               <UserOutlined />
               <span>分享者</span>
             </span>
             <span class="meta-divider">|</span>
             <span class="meta-item" :class="{ 'is-expired': info.isExpired }">
               <ClockCircleOutlined />
               <span>{{ expireText }}</span>
             </span>
          </div>
        </div>
      </div>
      
      <div class="action-right">
        <a-button type="primary" shape="round" class="download-all-btn">
          <template #icon><DownloadOutlined /></template>
          全部下载 ({{ info.fileCount }})
        </a-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.share-header {
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0,0,0,0.05);
  padding: 16px 0;
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.info-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.avatar-box {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  overflow: hidden;
  background-color: #f0f2f5;
  border: 1px solid #e1e4e8;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.text-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.share-title {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #1f2329;
  line-height: 1.4;
}

.share-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 13px;
  color: #8f959e;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.meta-divider {
    color: #e1e4e8;
}

.is-expired {
    color: #ff4d4f;
}

.download-all-btn {
    font-weight: 500;
    transition: all 0.3s;
}

@media (max-width: 768px) {
    .header-content {
        flex-direction: column;
        align-items: flex-start;
        gap: 16px;
    }
    
    .action-right {
        width: 100%;
    }
    
    .download-all-btn {
        width: 100%;
    }
}
</style>
