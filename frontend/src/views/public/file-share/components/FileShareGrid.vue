<template>
  <div class="file-grid">
    <div class="grid-container">
        <div v-for="file in fileList" :key="file.id" class="file-card">
            <!-- 缩略图区域 -->
            <div class="card-preview">
                <a-image 
                    v-if="getFileType(file.mimeType) === 'image' && file.thumbnailsUrl"
                    :src="file.thumbnailsUrl" 
                    height="100%"
                    width="100%"
                    class="preview-image"
                    :preview="false"
                >
                    <template #placeholder>
                         <div class="preview-placeholder">
                            <FileImageOutlined style="font-size: 40px" />
                         </div>
                    </template>
                </a-image>
                <div v-else class="preview-placeholder">
                     <component :is="getFileIcon(file.mimeType)" style="font-size: 40px" />
                </div>
                
                <!-- 悬浮遮罩 (预览和下载按钮) -->
                <div class="card-mask">
                    <a-space :size="12">
                        <!-- 预览按钮 - 仅图片显示 -->
                        <a-button 
                            v-if="getFileType(file.mimeType) === 'image' && file.url"
                            shape="circle" 
                            class="hover-action-btn" 
                            @click.stop="handlePreview(file)"
                        >
                            <template #icon><EyeOutlined /></template>
                        </a-button>
                        <!-- 下载按钮 -->
                        <a-button 
                            shape="circle" 
                            class="hover-action-btn" 
                            @click.stop="handleDownload(file)"
                        >
                            <template #icon><DownloadOutlined /></template>
                        </a-button>
                    </a-space>
                </div>
            </div>
            
            <!-- 文件信息 -->
            <div class="card-info">
                <div class="file-name" :title="file.name">{{ file.name }}</div>
                <div class="file-meta">{{ formatSize(file.size) }} • {{ formatDate(file.createTime) }}</div>
            </div>
        </div>
    </div>
    
    <!-- 图片预览弹窗 -->
    <a-modal
        v-model:open="previewVisible"
        :title="previewFileName"
        :footer="null"
        :width="previewWidth"
        centered
        :closable="true"
        @cancel="previewVisible = false"
    >
      <div class="preview-content">
        <img
            v-if="previewUrl"
            :src="previewUrl"
            :style="{ maxWidth: '100%', maxHeight: '70vh', objectFit: 'contain' }"
            :alt="previewFileName"
        />
      </div>
    </a-modal>
  </div>
</template>

<script lang="ts">
import type { FileLibraryPageVo } from "@/type/filelibrary/FileLibraryPageVo";
import { FileOutlined, FileImageOutlined, FileTextOutlined, VideoCameraOutlined, DownloadOutlined, EyeOutlined } from '@ant-design/icons-vue';

/**
 * 文件网格展示组件
 */
export default {
  name: "FileShareGrid",
  components: {
    FileOutlined,
    FileImageOutlined,
    FileTextOutlined,
    VideoCameraOutlined,
    DownloadOutlined,
    EyeOutlined
  },
  props: {
    fileList: {
      type: Array as () => FileLibraryPageVo[],
      default: () => []
    }
  },
  data() {
    return {
      previewVisible: false,
      previewUrl: '',
      previewFileName: '',
      previewWidth: 800
    };
  },
  methods: {
    // 根据 MIME 类型判断文件类型
    getFileType(mimeType: string): 'image' | 'document' | 'video' | 'other' {
      if (!mimeType) return 'other';
      if (mimeType.startsWith('image/')) return 'image';
      if (mimeType.startsWith('video/')) return 'video';
      if (mimeType.includes('pdf') || mimeType.includes('word') || mimeType.includes('excel') || 
          mimeType.includes('text') || mimeType.includes('document')) return 'document';
      return 'other';
    },

    // 获取文件图标
    getFileIcon(mimeType: string) {
      const type = this.getFileType(mimeType);
      switch(type) {
        case 'image': return FileImageOutlined;
        case 'document': return FileTextOutlined;
        case 'video': return VideoCameraOutlined;
        default: return FileOutlined;
      }
    },

    // 格式化文件大小
    formatSize(bytes: number): string {
      if (bytes === 0) return '0 B';
      const k = 1024;
      const sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
      const i = Math.floor(Math.log(bytes) / Math.log(k));
      return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    },

    // 格式化日期
    formatDate(dateStr?: string): string {
      if (!dateStr) return '';
      return dateStr.split(' ')[0];
    },

    // 预览图片
    handlePreview(file: FileLibraryPageVo) {
      if (file.url) {
        this.previewUrl = file.url;
        this.previewFileName = file.name || '预览图片';
        this.previewWidth = 800;
        this.previewVisible = true;
      }
    },

    // 下载文件（不打开新标签，直接触发浏览器下载）
    async handleDownload(file: FileLibraryPageVo) {
      if (!file.url) return;
      
      try {
        // 使用 fetch 下载文件，确保路径不被改变
        const response = await fetch(file.url, {
          method: 'GET',
          headers: {
            'Accept': '*/*'
          }
        });
        
        if (!response.ok) {
          throw new Error('下载失败');
        }
        
        // 获取文件 blob
        const blob = await response.blob();
        
        // 创建 blob URL
        const blobUrl = window.URL.createObjectURL(blob);
        
        // 创建临时下载链接
        const link = document.createElement('a');
        link.href = blobUrl;
        link.download = file.name || 'download';
        document.body.appendChild(link);
        link.click();
        
        // 清理
        document.body.removeChild(link);
        window.URL.revokeObjectURL(blobUrl);
      } catch (error) {
        console.error('下载文件失败:', error);
        // 如果 fetch 失败，回退到直接打开链接
        window.open(file.url, '_blank');
      }
    }
  }
};
</script>

<style scoped>
.file-grid {
    padding: 24px;
    max-width: 1200px;
    margin: 0 auto;
}

.grid-container {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 20px;
}

.file-card {
    background: #fff;
    border-radius: 12px;
    overflow: hidden;
    border: 1px solid #ebedf0;
    transition: all 0.3s ease;
    cursor: pointer;
    position: relative;
    display: flex;
    flex-direction: column;
}

.file-card:hover {
    transform: translateY(-4px);
    box-shadow: 0 12px 24px rgba(0,0,0,0.08);
    border-color: transparent;
}

.card-preview {
    height: 140px;
    background-color: #f8f9fa;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    overflow: hidden;
}

:deep(.ant-image) {
    width: 100%;
    height: 100%;
}

:deep(.preview-image) {
    object-fit: cover !important;
}

.preview-placeholder {
    color: #c0c4cc;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
}

.card-mask {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0,0,0,0.5);
    display: flex;
    align-items: center;
    justify-content: center;
    opacity: 0;
    transition: opacity 0.3s;
}

.file-card:hover .card-mask {
    opacity: 1;
}

.hover-action-btn {
    background: rgba(255,255,255,0.95);
    border: none;
    color: #1677ff;
    width: 40px;
    height: 40px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 18px;
    box-shadow: 0 2px 8px rgba(0,0,0,0.15);
    transition: all 0.2s;
}

.hover-action-btn:hover {
    background: #1677ff;
    color: #fff;
    transform: scale(1.1);
}

.preview-content {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 200px;
    padding: 20px;
}

.card-info {
    padding: 12px;
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}

.file-name {
    font-size: 14px;
    color: #303133;
    margin-bottom: 6px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    font-weight: 500;
}

.file-meta {
    font-size: 12px;
    color: #909399;
}
</style>
