<template>
  <a-layout>
    <a-layout-header class="file-library-header">
      <div class="title-row">
        <span class="title">我的文件</span>
        <a-space>
          <a-button v-if="selectedRowKeys.length > 0" @click="openMoveModal">
            移动至
          </a-button>
          <a-button v-if="selectedRowKeys.length > 0 && queryForm.spaceId" @click="confirmRemoveFromSpace">
            移除空间
          </a-button>
          <a-button type="primary" @click="uploadVisible = true">
            <template #icon>
              <UploadOutlined/>
            </template>
            上传
          </a-button>
        </a-space>
      </div>
      <a-divider style="margin: 10px 0;"/>
      <a-form :model="queryForm" layout="inline" style="width: 100%;">
        <a-row style="width: 100%;" justify="space-between">
          <a-col>
            <a-space wrap>
              <a-form-item label="所属空间">
                <a-select v-model:value="queryForm.spaceId"
                          placeholder="选择空间"
                          style="width: 200px"
                          allow-clear
                          @change="search">
                  <a-select-option value="">未归类</a-select-option>
                  <a-select-option v-for="space in displaySpaces" :key="space.id" :value="space.id">
                    {{ space.name }}
                  </a-select-option>
                </a-select>
              </a-form-item>
              <a-form-item label="关键字">
                <a-input v-model:value="queryForm.searchText"
                         placeholder="名称 / 路径"
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
          :scroll="{ x: 1000, y: tableHeight }"
          :data-source="tableData"
          :loading="loading"
          :pagination="{
            current: queryForm.current,
            pageSize: queryForm.size,
            total: total,
            showSizeChanger: true,
            pageSizeOptions: ['10','20','50'],
            showTotal: (total: number) => `共 ${total} 条`,
          }"
          row-key="id"
          :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange, fixed: true }"
          @change="handleTableChange"
      >
        <a-table-column title="预览" key="preview" :width="100" fixed="left">
          <template #default="{ record }">
            <img
                v-if="isImage(record.mimeType)"
                :src="record.thumbnailsUrl || record.url"
                style="width: 70px; height: 70px; object-fit: cover; cursor: pointer; border-radius: 4px;"
                @click="openImagePreview(record)"
            />
            <img v-else :src="getFileIcon(record.mimeType)" style="width: 70px; height: 70px; object-fit: contain;"/>
          </template>
        </a-table-column>
        <a-table-column title="文件名" dataIndex="name" key="name" :ellipsis="true"/>
        <a-table-column title="大小" key="size" :width="100">
          <template #default="{ record }">
            {{ formatSize(record.size) }}
          </template>
        </a-table-column>
        <a-table-column title="上传时间" dataIndex="createTime" key="createTime" :width="180"/>
        <a-table-column title="操作" key="action" :width="330" fixed="right">
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

    <a-modal v-model:open="moveVisible" title="移动至空间" @ok="confirmMove">
      <a-form layout="vertical">
        <a-form-item label="目标空间">
          <a-select v-model:value="moveSpaceId" placeholder="请选择目标空间">
            <a-select-option v-for="space in spaces" :key="space.id" :value="space.id">
              {{ space.name }}
            </a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>

    <FileUploadModal v-model:open="uploadVisible" @uploaded="handleUploaded"/>

    <a-modal
        v-model:open="previewVisible"
        :title="previewTitle"
        :footer="null"
        :width="previewWidth"
        centered
    >
      <div style="display: flex; justify-content: center; align-items: center; min-height: 200px;">
        <img
            :src="previewUrl"
            :style="{ maxWidth: '100%', maxHeight: '70vh', objectFit: 'contain' }"
            @load="onPreviewImageLoad"
        />
      </div>
    </a-modal>
  </a-layout>
</template>

<script lang="ts">
import {FileOutlined, UploadOutlined} from '@ant-design/icons-vue';
import {message, Modal, TablePaginationConfig} from 'ant-design-vue';
import FileUploadModal from "./FileUploadModal.vue";
import {deleteFileLibrary, getFileLibraryPage, removeFromSpace, renameFileLibrary} from "@/api/FileLibraryApi.ts";
import {allocateFilesToSpace, selectFileSpaces} from "@/api/FileSpaceApi.ts";
import type {PageResult} from "@/type/PageResult.ts";
import {FileLibraryPageDto} from "@/type/filelibrary/FileLibraryPageDto.ts";
import {FileLibraryPageVo} from "@/type/filelibrary/FileLibraryPageVo.ts";

/**
 * 文件库列表
 */
export default {
  name: "FileLibraryList",
  components: {FileUploadModal, UploadOutlined, FileOutlined},
  computed: {
    displaySpaces() {
      // 如果正式列表还没加载完，但路由里有传过来的空间信息，先造个假数据占位，避免回显 ID
      const routeSpaceId = this.$route.query.spaceId as string;
      const routeSpaceName = this.$route.query.spaceName as string;
      
      if (this.spaces.length === 0 && routeSpaceId && routeSpaceName) {
        return [{ id: routeSpaceId, name: routeSpaceName }];
      }
      return this.spaces;
    }
  },
  data() {
    return {
      queryForm: {
        current: 1,
        size: 10,
        searchText: '',
        spaceId: ''
      } as FileLibraryPageDto,
      spaces: [] as any[],
      tableData: [] as FileLibraryPageVo[],
      total: 0,
      loading: false,
      uploadVisible: false,
      renameVisible: false,
      renameForm: {
        id: '',
        name: ''
      },
      tableHeight: 0,
      selectedRowKeys: [] as string[],
      moveVisible: false,
      moveSpaceId: '',
      previewVisible: false,
      previewUrl: '',
      previewTitle: '',
      previewWidth: 600,
    }
  },
  mounted() {
    this.calculateTableHeight();
    window.addEventListener('resize', this.calculateTableHeight);

    // 处理路由传参
    const routeSpaceId = this.$route.query.spaceId as string;
    if (routeSpaceId) {
      this.queryForm.spaceId = routeSpaceId;
    }

    this.loadData();
    this.loadSpaces();
  },
  beforeUnmount() {
    window.removeEventListener('resize', this.calculateTableHeight);
  },
  methods: {
    calculateTableHeight() {
      this.tableHeight = window.innerHeight - 365;
    },
    async loadSpaces() {
      try {
        const res = await selectFileSpaces();
        if (res.data.code === 0) {
          this.spaces = res.data.data;
        }
      } catch (e) {
        console.error('获取文件空间失败', e);
      }
    },
    async loadData() {
      this.loading = true;
      try {
        const res = await getFileLibraryPage(this.queryForm);
        console.log('分页查询结果:', res.data);
        if (res.data.code === 0) {
          const page = res.data.data as PageResult<FileLibraryPageVo>;
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
      this.queryForm.spaceId = '';
      this.queryForm.current = 1;
      this.queryForm.size = 10;
      this.loadData();
    },
    onSelectChange(selectedRowKeys: string[]) {
      this.selectedRowKeys = selectedRowKeys;
    },
    openMoveModal() {
      this.moveSpaceId = '';
      this.moveVisible = true;
    },
    openImagePreview(record: FileLibraryPageVo) {
      this.previewUrl = record.url;
      this.previewTitle = record.name;
      this.previewWidth = 600;
      this.previewVisible = true;
    },
    onPreviewImageLoad(event: Event) {
      const img = event.target as HTMLImageElement;
      const naturalWidth = img.naturalWidth;
      const maxWidth = window.innerWidth * 0.8;
      this.previewWidth = Math.min(naturalWidth + 48, maxWidth, 1200);
    },
    confirmRemoveFromSpace() {
      if (this.selectedRowKeys.length === 0) {
        message.warning('请先选择文件');
        return;
      }
      Modal.confirm({
        title: '移除确认',
        content: `确定将选中的 ${this.selectedRowKeys.length} 个文件从当前空间移除吗？`,
        okText: '确定',
        cancelText: '取消',
        onOk: async () => {
          try {
            const res = await removeFromSpace(this.selectedRowKeys);
            if (res.data.code === 0) {
              message.success('移除成功');
              this.selectedRowKeys = [];
              this.loadData();
            } else {
              message.error(res.data.details || '移除失败');
            }
          } catch (e) {
            message.error('移除失败');
          }
        }
      });
    },
    async confirmMove() {
      if (this.selectedRowKeys.length === 0) {
        message.warning('请先选择文件');
        return;
      }
      try {
        const res = await allocateFilesToSpace({
          spaceId: this.moveSpaceId,
          fileIds: this.selectedRowKeys
        });
        if (res.data.code === 0) {
          message.success('移动成功');
          this.moveVisible = false;
          this.selectedRowKeys = [];
          this.loadData();
        } else {
          message.error(res.data.details || '移动失败');
        }
      } catch (e) {
        message.error('移动失败');
      }
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
        const res = await renameFileLibrary({id: this.renameForm.id, name: this.renameForm.name});
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

.file-library-header {
  height: 150px;
  padding: 20px;
  background-color: var(--ant-color-bg-container);
}
</style>

