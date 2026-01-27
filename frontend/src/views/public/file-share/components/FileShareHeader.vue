<template>
  <div class="share-header">
    <div class="header-content">
      <div class="info-left">
        <div class="logo-wrapper">
          <img alt="logo" width="36" height="36" src="/icon/whale-y.png" class="logo-img" />
        </div>
        <div class="text-info">
          <h1 class="share-title">{{ info.name }}</h1>
          <div class="share-meta">
             <span class="meta-item" v-if="info.createUserName">
               <UserOutlined />
               <span>{{ info.createUserName }}</span>
             </span>
             <span class="meta-divider" v-if="info.createUserName">|</span>
             <span class="meta-item" :class="{ 'is-expired': info.isExpired }">
               <ClockCircleOutlined />
               <span>{{ expireText }}</span>
             </span>
          </div>
        </div>
      </div>
      
      <div class="action-right">
        <a-button type="primary" shape="round" class="download-all-btn" @click="handleDownloadAll">
          <template #icon><DownloadOutlined /></template>
          全部下载 ({{ info.fileCount }})
        </a-button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import type { FileSharePublicVo } from "@/type/file-share/vo/FileSharePublicVo";
import { DownloadOutlined, ClockCircleOutlined, UserOutlined } from '@ant-design/icons-vue';

/**
 * 文件分享头部组件
 * 
 * @author 
 */
import { defineComponent } from 'vue';

/**
 * 文件分享头部组件
 * 
 * @author 
 */
export default defineComponent({
  name: "FileShareHeader",
  components: {
    DownloadOutlined,
    ClockCircleOutlined,
    UserOutlined
  },
  props: {
    info: {
      type: Object as () => FileSharePublicVo,
      required: true
    }
  },
  computed: {
    // 格式化有效期
    expireText(): string {
      if (!this.info.enableExpire) {
        return '永久有效';
      }
      return `有效期至: ${this.info.expireTime}`;
    }
  },
  methods: {
    // 下载全部文件
    handleDownloadAll() {
      this.$emit('download-all');
    }
  }
});
</script>

<style scoped>
.share-header {
  background-color: var(--ant-color-bg-container);
  /* 使用半透明背景 + 模糊效果增强层级感 */
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  /* 确保背景不透明 */
  background-color: rgba(255, 255, 255, 0.95);
  box-shadow: 0 2px 8px rgba(0,0,0,0.08), 0 1px 2px rgba(0,0,0,0.04);
  padding: 16px 0;
  position: sticky;
  top: 0;
  z-index: 100;
  border-bottom: 1px solid var(--ant-color-border-secondary);
}

/* 黑暗模式下的样式 */
:deep(.dark) .share-header {
  background-color: rgba(0, 0, 0, 0.85);
  box-shadow: 0 2px 8px rgba(0,0,0,0.5), 0 1px 2px rgba(0,0,0,0.3);
  border-bottom-color: var(--ant-color-border-secondary);
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

.logo-wrapper {
  display: flex;
  align-items: center;
  flex-shrink: 0;
}

.logo-img {
  display: block;
  object-fit: contain;
}

.text-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
  min-width: 0;
}

.share-title {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: var(--ant-color-text);
  line-height: 1.4;
}

.share-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 13px;
  color: var(--ant-color-text-secondary);
  flex-wrap: wrap;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 4px;
  white-space: nowrap;
}

.meta-item :deep(.anticon) {
  font-size: 13px;
  display: flex;
  align-items: center;
}

.meta-divider {
    color: var(--ant-color-border-secondary);
}

.is-expired {
    color: var(--ant-color-error);
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
    
    .share-meta {
        gap: 8px;
    }
}
</style>
