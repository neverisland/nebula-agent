<script setup lang="ts">
import type { MockFileItem } from "../mock";
import { FileOutlined, FileImageOutlined, FileTextOutlined, VideoCameraOutlined, DownloadOutlined } from '@ant-design/icons-vue';

/**
 * 文件网格展示组件
 */

interface Props {
  fileList: MockFileItem[];
}

defineProps<Props>();

// 获取文件图标
const getFileIcon = (type: string) => {
    switch(type) {
        case 'image': return FileImageOutlined;
        case 'document': return FileTextOutlined;
        case 'video': return VideoCameraOutlined;
        default: return FileOutlined;
    }
};

// 格式化文件大小
const formatSize = (bytes: number) => {
    if (bytes === 0) return '0 B';
    const k = 1024;
    const sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
};

</script>

<template>
  <div class="file-grid">
    <div class="grid-container">
        <div v-for="file in fileList" :key="file.id" class="file-card">
            <!-- 缩略图区域 -->
            <div class="card-preview">
                <a-image 
                    v-if="file.fileType === 'image' && file.thumbnailUrl"
                    :src="file.thumbnailUrl" 
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
                     <component :is="getFileIcon(file.fileType)" style="font-size: 40px" />
                </div>
                
                <!-- 悬浮遮罩 (下载按钮) -->
                <div class="card-mask">
                    <a-button shape="circle" class="hover-download-btn">
                        <template #icon><DownloadOutlined /></template>
                    </a-button>
                </div>
            </div>
            
            <!-- 文件信息 -->
            <div class="card-info">
                <div class="file-name" :title="file.name">{{ file.name }}</div>
                <div class="file-meta">{{ formatSize(file.size) }} • {{ file.updateTime.split(' ')[0] }}</div>
            </div>
        </div>
    </div>
  </div>
</template>

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
    background: rgba(0,0,0,0.3);
    display: flex;
    align-items: center;
    justify-content: center;
    opacity: 0;
    transition: opacity 0.3s;
}

.file-card:hover .card-mask {
    opacity: 1;
}

.hover-download-btn {
    background: rgba(255,255,255,0.9);
    border: none;
    color: #1677ff;
    width: 40px;
    height: 40px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 18px;
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
