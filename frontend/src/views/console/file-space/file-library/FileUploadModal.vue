<template>
  <a-modal
      :open="open"
      title="上传文件"
      width="820px"
      :footer="null"
      @cancel="close"
  >
    <div ref="uploadContainerRef" class="upload-container" @paste="handlePaste" tabindex="-1">
      <a-upload-dragger
          name="file"
          :file-list="fileList"
          :before-upload="beforeUpload"
          :on-change="handleChange"
          :on-remove="handleRemove"
          :multiple="true"
          :show-upload-list="true"
          :max-count="10"
          class="upload-drag"
      >
        <p class="ant-upload-drag-icon">
          <inbox-outlined />
        </p>
        <p class="ant-upload-text">点击选择文件或将文件拖拽到此处</p>
        <p class="ant-upload-hint">也支持粘贴剪贴板中的文件</p>
      </a-upload-dragger>
      <div class="actions">
        <a-space>
          <a-button @click="clearFiles">清空列表</a-button>
          <a-button type="primary" :loading="uploading" @click="handleUpload">
            <UploadOutlined style="margin-right: 8px;" />
            开始上传
          </a-button>
        </a-space>
      </div>
      <div v-if="uploadedResults.length" class="result-list">
        <a-typography-title :level="5">上传结果</a-typography-title>
        <div class="result-scroll">
          <a-list :data-source="uploadedResults">
            <template #renderItem="{ item }">
              <a-list-item>
                <a-list-item-meta>
                  <template #avatar>
                    <a-image
                        v-if="isImage(item.mimeType) || isImageByFilename(item.name)"
                        :src="item.thumbnailsUrl || item.url"
                        :width="40"
                        :height="40"
                        :fallback="item.mimeType ? getFileIcon(item.mimeType) : getFileIconByFilename(item.name)"
                        :preview="false"
                    />
                    <img v-else :src="item.mimeType ? getFileIcon(item.mimeType) : getFileIconByFilename(item.name)" style="width: 40px; height: 40px; object-fit: contain;" />
                  </template>
                  <template #title>
                    <span class="file-name">{{ item.name }}</span>
                  </template>
                  <template #description>
                    <span class="file-url">{{ item.url }}</span>
                  </template>
                </a-list-item-meta>
                <a-space>
                  <a-button type="link" size="small" :href="item.url" target="_blank">
                    <template #icon><LinkOutlined /></template>
                    预览
                  </a-button>
                  <a-button type="link" size="small" @click="copyLink(item.url)">
                    <template #icon><CopyOutlined /></template>
                    复制
                  </a-button>
                </a-space>
              </a-list-item>
            </template>
          </a-list>
        </div>
      </div>
    </div>
  </a-modal>
</template>

<script lang="ts">
import { InboxOutlined, LinkOutlined, CopyOutlined, UploadOutlined } from "@ant-design/icons-vue";
import { message } from "ant-design-vue";
import type { UploadFile } from "ant-design-vue";
import { uploadFileLibrary } from "@/api/FileLibraryApi.ts";
import type { FileLibraryUploadVo } from "@/type/filelibrary/FileLibraryUploadVo";
import { isImage, getFileIcon, isImageByFilename, getFileIconByFilename } from "@/utils/fileUtils";

/**
 * 文件上传弹窗
 */
export default {
  name: "FileUploadModal",
  components: { InboxOutlined, LinkOutlined, CopyOutlined, UploadOutlined },
  props: {
    open: {
      type: Boolean,
      default: false,
    },
  },
  emits: ['update:open', 'uploaded'],
  data() {
    return {
      fileList: [] as UploadFile[],
      uploading: false,
      uploadedResults: [] as FileLibraryUploadVo[],
      visibilityChangeHandler: null as (() => void) | null,
    }
  },
  watch: {
    open(newVal: boolean) {
      if (newVal) {
        // 弹窗打开时，延迟聚焦以确保 DOM 已渲染
        this.$nextTick(() => {
          this.focusContainer();
        });
      }
    }
  },
  mounted() {
    // 监听标签页可见性变化
    this.visibilityChangeHandler = () => {
      if (document.visibilityState === 'visible' && this.open) {
        // 标签页切换回来且弹窗打开时，自动聚焦
        this.$nextTick(() => {
          this.focusContainer();
        });
      }
    };
    document.addEventListener('visibilitychange', this.visibilityChangeHandler);
  },
  beforeUnmount() {
    // 清理事件监听
    if (this.visibilityChangeHandler) {
      document.removeEventListener('visibilitychange', this.visibilityChangeHandler);
    }
  },
  computed: {
    isImage() {
      return (mimeType: string) => isImage(mimeType);
    },
    isImageByFilename() {
      return isImageByFilename;
    },
    getFileIcon() {
      return getFileIcon;
    },
    getFileIconByFilename() {
      return getFileIconByFilename;
    }
  },
  methods: {
    beforeUpload(file: UploadFile) {
      this.fileList = [...this.fileList, file];
      return false;
    },
    handleChange(info: { fileList: UploadFile[] }) {
      this.fileList = [...info.fileList];
    },
    handleRemove(file: UploadFile) {
      this.fileList = this.fileList.filter(item => item.uid !== file.uid);
      return true;
    },
    handlePaste(event: ClipboardEvent) {
      if (!event.clipboardData) {
        return;
      }
      const files = event.clipboardData.files;
      if (!files || files.length === 0) {
        return;
      }
      for (let i = 0; i < files.length; i++) {
        const file = files.item(i);
        if (file) {
          const uploadFile = {
            uid: `${Date.now()}-${i}`,
            name: file.name,
            size: file.size,
            type: file.type,
            originFileObj: file,
          } as any;
          this.fileList.push(uploadFile);
        }
      }
      message.success(`已添加 ${files.length} 个文件`);
    },
    async handleUpload() {
      if (!this.fileList.length) {
        message.warning('请先选择文件');
        return;
      }
      this.uploading = true;

      // 过滤出还未上传的文件（只上传状态不是 'done' 的文件）
      const filesToUpload = this.fileList.filter(file => {
        return file.status !== 'done';
      });

      if (filesToUpload.length === 0) {
        message.warning('所有文件都已上传完成');
        this.uploading = false;
        return;
      }

      for (const file of filesToUpload) {
        if (!file.originFileObj) {
          message.warning(`${file.name} 无原始文件，已跳过`);
          continue;
        }
        const formData = new FormData();
        formData.append('file', file.originFileObj as File);
        try {
          const res = await uploadFileLibrary(formData);
          if (res.data.code === 0) {
            file.status = 'done';
            file.percent = 100;
            const data = res.data.data as FileLibraryUploadVo;
            this.uploadedResults.unshift(data);
          } else {
            file.status = 'error';
            message.error(res.data.details || '上传失败');
          }
        } catch (e) {
          file.status = 'error';
          message.error('上传失败');
        }
      }
      this.uploading = false;
      // 清空已上传的文件列表
      this.fileList = [];
      // 仅通知父级刷新列表，不自动关闭弹窗
      this.$emit('uploaded', this.uploadedResults.length);
    },
    clearFiles() {
      this.fileList = [];
      this.uploadedResults = [];
    },
    close() {
      this.$emit('update:open', false);
    },
    async copyLink(url: string) {
      try {
        await navigator.clipboard.writeText(url);
        message.success('已复制链接');
      } catch (e) {
        message.error('复制失败，请手动复制');
      }
    },
    focusContainer() {
      // 聚焦到上传容器，使其可以接收粘贴事件
      const container = this.$refs.uploadContainerRef as HTMLElement;
      if (container) {
        container.focus();
      }
    },
  }
}
</script>

<style scoped>
.upload-container {
  padding: 12px;
  outline: none;
}
.actions {
  margin-top: 12px;
  text-align: right;
}
.result-list {
  margin-top: 16px;
}
.result-scroll {
  max-height: 300px;
  overflow-y: auto;
  border-radius: 6px;
  padding: 8px;
}
.file-name {
  font-weight: 500;
  margin-bottom: 4px;
  display: block;
}
.file-url {
  font-size: 12px;
  word-break: break-all;
}
.upload-drag :deep(.ant-upload-drag) {
  padding: 40px 32px;
  min-height: 260px;
}

.upload-drag :deep(.ant-upload-drag-icon .anticon) {
  font-size: 64px;
  color: var(--ant-primary-color);
}
</style>

