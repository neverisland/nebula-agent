<template>
  <a-layout>
    <a-layout-header style="height: 150px; padding: 20px;">
      <div class="title-row">
        <span class="title">文件库 - 我的文件</span>
        <a-space>
          <a-button type="primary" @click="uploadVisible = true">
            <template #icon><UploadOutlined /></template>
            上传
          </a-button>
        </a-space>
      </div>
      <a-divider style="margin: 10px 0;"/>
      <a-form :model="queryForm" layout="inline" style="width: 100%;">
        <a-row style="width: 100%;" justify="space-between">
          <a-col>
            <a-space wrap>
              <a-form-item label="关键字">
                <a-input v-model:value="queryForm.searchText"
                         placeholder="名称 / 路径"
                         allow-clear
                         @keyup.enter="search"/>
              </a-form-item>
              <a-form-item label="文件类型">
                <a-input v-model:value="queryForm.mimeType"
                         placeholder="image/ 或 video/"
                         allow-clear
                         @keyup.enter="search"/>
              </a-form-item>
              <a-form-item>
                <a-button type="primary" @click="search">检索</a-button>
                <a-button style="margin-left: 8px" @click="reset">重置</a-button>
              </a-form-item>
            </a-space>
          </a-col>
        </a-row>
      </a-form>
    </a-layout-header>
    <a-layout-content style="padding: 0 20px;">
      <a-table
          :data-source="tableData"
          :loading="loading"
          :pagination="{
            current: queryForm.current,
            pageSize: queryForm.size,
            total: total,
            showSizeChanger: true,
            pageSizeOptions: ['10','20','50'],
            showTotal: total => `共 ${total} 条`,
          }"
          row-key="id"
          @change="handleTableChange"
      >
        <a-table-column title="预览" key="preview" :width="120">
          <template #default="{ record }">
            <a-image
                v-if="isImage(record.mimeType)"
                :src="record.thumbnailsUrl || record.url"
                :width="70"
                :height="70"
                :fallback="getFileIcon('file')"
                :preview="false"
            />
            <img v-else :src="getFileIcon(record.mimeType)" style="width: 70px; height: 70px; object-fit: contain;" />
          </template>
        </a-table-column>
        <a-table-column title="文件名" dataIndex="name" key="name"/>
        <a-table-column title="大小" key="size" :width="140">
          <template #default="{ record }">
            {{ formatSize(record.size) }}
          </template>
        </a-table-column>
        <a-table-column title="上传时间" dataIndex="createTime" key="createTime" :width="200"/>
        <a-table-column title="操作" key="action" :width="260">
          <template #default="{ record }">
            <a-space>
              <a-button type="link" @click="copyLink(record)">复制链接</a-button>
              <a-button type="link" @click="download(record)">下载</a-button>
              <a-button type="link" @click="openRename(record)">重命名</a-button>
              <a-button type="link" danger @click="confirmDelete(record)">删除</a-button>
            </a-space>
          </template>
        </a-table-column>
      </a-table>
    </a-layout-content>

    <a-modal v-model:open="renameVisible" title="重命名" @ok="submitRename">
      <a-input v-model:value="renameForm.name" placeholder="请输入文件名称"/>
    </a-modal>

    <FileUploadModal v-model:open="uploadVisible" @uploaded="handleUploaded"/>
  </a-layout>
</template>

<script lang="ts">
import { UploadOutlined, FileOutlined } from '@ant-design/icons-vue';
import { message, Modal, TablePaginationConfig } from 'ant-design-vue';
import FileUploadModal from "./FileUploadModal.vue";
import { deleteFileLibrary, getFileLibraryPage, renameFileLibrary } from "@/api/FileLibraryApi.ts";
import type { FileLibraryPageVo, FileLibraryPageDto } from "@/type/filelibrary/FileLibrary.ts";
import type { PageResult } from "@/type/PageResult.ts";

/**
 * 文件库列表
 */
export default {
  name: "FileLibraryList",
  components: { FileUploadModal, UploadOutlined, FileOutlined },
  data() {
    return {
      queryForm: {
        current: 1,
        size: 10,
        searchText: '',
        mimeType: '',
        spaceId: ''
      } as FileLibraryPageDto,
      tableData: [] as FileLibraryPageVo[],
      total: 0,
      loading: false,
      uploadVisible: false,
      renameVisible: false,
      renameForm: {
        id: '',
        name: ''
      },
    }
  },
  mounted() {
    this.loadData();
  },
  methods: {
    async loadData() {
      this.loading = true;
      try {
        const res = await getFileLibraryPage(this.queryForm);
        console.log('分页查询结果:', res.data);
        if (res.data.code === 0) {
          const page = res.data.data as PageResult<FileLibraryPageVo[]>;
          console.log('PageResult:', page);
          const records = (page as any).records || page.list || [];
          console.log('记录数:', records.length, '总记录数:', page.total);
          this.tableData = records as FileLibraryPageVo[];
          this.total = page.total || 0;
        } else {
          message.error(res.data.details || '查询失败');
        }
      } catch (e) {
        message.error('查询失败');
      } finally {
        this.loading = false;
      }
    },
    search() {
      this.queryForm.current = 1;
      this.loadData();
    },
    reset() {
      this.queryForm.searchText = '';
      this.queryForm.mimeType = '';
      this.queryForm.current = 1;
      this.queryForm.size = 10;
      this.loadData();
    },
    handleTableChange(pagination: TablePaginationConfig) {
      this.queryForm.current = pagination.current || 1;
      this.queryForm.size = pagination.pageSize || 10;
      this.loadData();
    },
    isImage(mimeType: string) {
      return mimeType && mimeType.startsWith('image/');
    },
    getFileIcon(mimeType: string) {
      if (!mimeType) return '/file-icon/file.png';

      // 根据 MIME 类型映射到对应的图标
      const mimeIconMap: Record<string, string> = {
        // 图片类型
        'image/': '/file-icon/image.png',
        // 视频类型
        'video/': '/file-icon/video.png',
        // 音频类型
        'audio/': '/file-icon/mp3.png',
        // 文档类型
        'application/pdf': '/file-icon/pdf.png',
        'application/msword': '/file-icon/file.png',
        'application/vnd.openxmlformats-officedocument.wordprocessingml.document': '/file-icon/file.png',
        'application/vnd.ms-powerpoint': '/file-icon/ppt.png',
        'application/vnd.openxmlformats-officedocument.presentationml.presentation': '/file-icon/ppt.png',
        'application/vnd.ms-excel': '/file-icon/file.png',
        'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet': '/file-icon/file.png',
        // 压缩文件
        'application/zip': '/file-icon/zip.png',
        'application/x-zip-compressed': '/file-icon/zip.png',
        'application/x-rar-compressed': '/file-icon/rar.png',
        'application/rar': '/file-icon/rar.png',
        // 可执行文件
        'application/x-msdownload': '/file-icon/exe.png',
        'application/octet-stream': '/file-icon/exe.png',
        // 文本文件
        'text/': '/file-icon/txt.png',
        'application/json': '/file-icon/txt.png',
        'application/javascript': '/file-icon/txt.png',
        'application/xml': '/file-icon/txt.png',
      };

      // 优先匹配具体的 MIME 类型
      if (mimeIconMap[mimeType]) {
        return mimeIconMap[mimeType];
      }

      // 其次匹配 MIME 类型前缀
      for (const [prefix, icon] of Object.entries(mimeIconMap)) {
        if (prefix.endsWith('/') && mimeType.startsWith(prefix)) {
          return icon;
        }
      }

      // 默认图标
      return '/file-icon/file.png';
    },
    formatSize(size: number) {
      if (size < 1024) return `${size} B`;
      if (size < 1024 * 1024) return `${(size / 1024).toFixed(1)} KB`;
      if (size < 1024 * 1024 * 1024) return `${(size / 1024 / 1024).toFixed(1)} MB`;
      return `${(size / 1024 / 1024 / 1024).toFixed(1)} GB`;
    },
    copyLink(record: FileLibraryPageVo) {
      navigator.clipboard.writeText(record.url).then(() => {
        message.success('已复制下载链接');
      }).catch(() => {
        message.error('复制失败');
      });
    },
    download(record: FileLibraryPageVo) {
      window.open(record.url, '_blank');
    },
    openRename(record: FileLibraryPageVo) {
      this.renameForm.id = record.id;
      this.renameForm.name = record.name;
      this.renameVisible = true;
    },
    async submitRename() {
      if (!this.renameForm.name) {
        message.warning('文件名不能为空');
        return;
      }
      try {
        const res = await renameFileLibrary({ id: this.renameForm.id, name: this.renameForm.name });
        if (res.data.code === 0) {
          message.success('重命名成功');
          this.renameVisible = false;
          this.loadData();
        } else {
          message.error(res.data.details || '重命名失败');
        }
      } catch (e) {
        message.error('重命名失败');
      }
    },
    confirmDelete(record: FileLibraryPageVo) {
      Modal.confirm({
        title: '删除确认',
        content: `确定删除文件【${record.name}】吗？`,
        okText: '删除',
        okType: 'danger',
        cancelText: '取消',
        onOk: async () => {
          try {
            const res = await deleteFileLibrary(record.id);
            if (res.data.code === 0) {
              message.success('删除成功');
              this.loadData();
            } else {
              message.error(res.data.details || '删除失败');
            }
          } catch (e) {
            message.error('删除失败');
          }
        }
      });
    },
    handleUploaded(successCount?: number) {
      if (successCount && successCount > 0) {
        message.success('上传完成');
      }
      this.loadData();
    },
  }
}
</script>

<style scoped>
.title-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.title {
  font-size: 18px;
  font-weight: 600;
}
</style>

