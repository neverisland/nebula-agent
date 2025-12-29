<template>
  <a-layout>
    <a-layout-header class="file-library-header">
      <div class="title-row">
        <span class="title">我的文件</span>
        <a-space>
          <a-button v-if="selectedRowKeys.length > 0" @click="openMoveModal">
            移动至
          </a-button>
          <a-button v-if="selectedRowKeys.length > 0" @click="openShareModal">
            分享
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
          <a-radio-group v-model:value="viewMode" button-style="solid">
            <a-radio-button value="list"><UnorderedListOutlined /></a-radio-button>
            <a-radio-button value="grid"><AppstoreOutlined /></a-radio-button>
          </a-radio-group>
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
      <!-- 列表视图 -->
      <a-table
          v-if="viewMode === 'list'"
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
        <a-table-column title="上传时间" key="createTime" :width="180">
          <template #default="{ record }">
            {{ formatDateTime(record.createTime || '') }}
          </template>
        </a-table-column>
        <a-table-column title="操作" key="action" :width="400" fixed="right">
          <template #default="{ record }">
            <a-space>
              <a-button type="link" @click="copyLink(record)">复制链接</a-button>
              <a-button type="link" @click="download(record)">下载</a-button>
              <a-button type="link" @click="openRename(record)">重命名</a-button>
              <a-button type="link" @click="showDetail(record)">详情</a-button>
              <a-button type="link" danger @click="confirmDelete(record)">删除</a-button>
            </a-space>
          </template>
        </a-table-column>
      </a-table>

      <!-- 网格视图 -->
      <div v-else class="grid-container">
        <a-spin :spinning="loading">
          <a-row :gutter="[16, 16]">
            <a-col v-for="item in tableData" :key="item.id" :xs="24" :sm="12" :md="8" :lg="6" :xl="4">
              <a-card
                  hoverable
                  size="small"
                  :class="{ 'grid-card-selected': selectedRowKeys.includes(item.id) }"
                  @contextmenu.prevent="showContextMenu($event, item)"
              >
                <template #cover>
                  <div class="card-cover" @click="toggleCardSelect(item.id)">
                    <a-checkbox
                        class="card-checkbox"
                        :checked="selectedRowKeys.includes(item.id)"
                        @click.stop
                        @change="toggleCardSelect(item.id)"
                    />
                    <img
                        v-if="isImage(item.mimeType)"
                        :src="item.thumbnailsUrl || item.url"
                        @click.stop="openImagePreview(item)"
                    />
                    <img v-else :src="getFileIcon(item.mimeType)" class="file-icon" />
                  </div>
                </template>
                <a-card-meta>
                  <template #title>
                    <a-tooltip :title="item.name">
                      <span class="card-filename">{{ item.name }}</span>
                    </a-tooltip>
                  </template>
                </a-card-meta>
              </a-card>
            </a-col>
          </a-row>
          <div v-if="tableData.length === 0 && !loading" class="grid-empty">
            <a-empty description="暂无数据" />
          </div>
        </a-spin>
        <!-- 网格视图分页 -->
        <div class="grid-pagination">
          <a-pagination
              v-model:current="queryForm.current"
              v-model:pageSize="queryForm.size"
              :total="total"
              :showSizeChanger="true"
              :pageSizeOptions="['10', '20', '50']"
              :showTotal="(t: number) => `共 ${t} 条`"
              @change="handleGridPageChange"
          />
        </div>
      </div>
    </a-layout-content>

    <!-- 右键菜单 -->
    <div
        v-show="contextMenuVisible"
        class="context-menu-wrapper"
        :style="contextMenuStyle"
        @click.stop
    >
      <a-menu mode="vertical" @click="handleContextMenuClick">
        <a-menu-item key="copy"><CopyOutlined /> 复制链接</a-menu-item>
        <a-menu-item key="download"><DownloadOutlined /> 下载</a-menu-item>
        <a-menu-item key="rename"><EditOutlined /> 重命名</a-menu-item>
        <a-menu-item key="detail"><InfoCircleOutlined /> 详情</a-menu-item>
        <a-menu-divider />
        <a-menu-item key="delete" danger><DeleteOutlined /> 删除</a-menu-item>
      </a-menu>
    </div>

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
    
    <a-modal v-model:open="dialogShare" title="创建分享" :width="600" :footer="null">
      <FileShareAdd ref="shareRef" @closeDialogInsert="closeShareDialog"/>
    </a-modal>

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

    <!-- 详情弹窗 -->
    <a-modal v-model:open="detailVisible" title="文件详情" :footer="null" width="600px">
      <div v-if="detailRecord" class="file-detail-content">
        <!-- 缩略图预览区 -->
        <div v-if="isImage(detailRecord.mimeType) && (detailRecord.thumbnailsUrl || detailRecord.url)" class="detail-preview">
          <img :src="detailRecord.thumbnailsUrl || detailRecord.url" alt="缩略图" />
        </div>
        <!-- 文件信息 -->
        <a-descriptions :column="1" style="padding: 16px 0;">
          <a-descriptions-item label="文件名">{{ detailRecord.name }}</a-descriptions-item>
          <a-descriptions-item label="文件大小">{{ formatSize(detailRecord.size) }}</a-descriptions-item>
          <a-descriptions-item label="文件类型">{{ detailRecord.mimeType }}</a-descriptions-item>
          <a-descriptions-item label="上传时间">{{ formatDateTime(detailRecord.createTime || '') }}</a-descriptions-item>
          <a-descriptions-item label="文件链接">
            <a-button type="primary" size="small" @click="copyDetailLink">
              <template #icon><CopyOutlined /></template>
              复制链接
            </a-button>
          </a-descriptions-item>
        </a-descriptions>
      </div>
    </a-modal>
  </a-layout>
</template>

<script lang="ts">
import {
  FileOutlined,
  UploadOutlined,
  UnorderedListOutlined,
  AppstoreOutlined,
  CopyOutlined,
  DownloadOutlined,
  EditOutlined,
  InfoCircleOutlined,
  DeleteOutlined,
  ShareAltOutlined
} from '@ant-design/icons-vue';
import {message, Modal, TablePaginationConfig} from 'ant-design-vue';
import FileUploadModal from "./FileUploadModal.vue";
import FileShareAdd from "../file-share/FileShareAdd.vue";
import {deleteFileLibrary, getFileLibraryPage, removeFromSpace, renameFileLibrary} from "@/api/FileLibraryApi.ts";
import {allocateFilesToSpace, selectFileSpaces} from "@/api/FileSpaceApi.ts";
import {ShareTypeEnum} from "@/enums/ShareTypeEnum.ts";
import {nextTick} from "vue";
import type {PageResult} from "@/type/PageResult.ts";
import {FileLibraryPageDto} from "@/type/filelibrary/FileLibraryPageDto.ts";
import {FileLibraryPageVo} from "@/type/filelibrary/FileLibraryPageVo.ts";

/**
 * 文件库列表
 */
export default {
  name: "FileLibraryList",
  components: {
    FileUploadModal,
    UploadOutlined,
    FileOutlined,
    UnorderedListOutlined,
    AppstoreOutlined,
    CopyOutlined,
    DownloadOutlined,
    EditOutlined,
    InfoCircleOutlined,
    DeleteOutlined,
    ShareAltOutlined,
    FileShareAdd
  },
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
      // 视图模式：list 列表，grid 网格
      viewMode: 'list' as 'list' | 'grid',
      // 右键菜单
      contextMenuVisible: false,
      contextMenuStyle: { left: '0px', top: '0px' },
      contextRecord: null as FileLibraryPageVo | null,
      // 详情弹窗
      detailVisible: false,
      detailRecord: null as FileLibraryPageVo | null,
      dialogShare: false,
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
    openShareModal() {
      if (this.selectedRowKeys.length === 0) return;

      this.dialogShare = true;
      
      let name = '';
      let sourceName = '';

      if (this.selectedRowKeys.length === 1) {
        // 单个文件
        const file = this.tableData.find(item => item.id === this.selectedRowKeys[0]);
        if (file) {
          name = file.name;
          sourceName = file.name;
        }
      } else {
        // 多个文件
        name = `${this.selectedRowKeys.length}个文件分享`;
        sourceName = `${this.selectedRowKeys.length}个文件`;
      }

      nextTick(() => {
        (this.$refs.shareRef as any)?.init({
          shareType: ShareTypeEnum.FILE,
          fileIds: this.selectedRowKeys,
          name: name,
          sourceName: sourceName
        });
      });
    },
    closeShareDialog(refresh: boolean) {
      this.dialogShare = false;
      if (refresh) {
        this.selectedRowKeys = [];
        this.loadData();
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
    // ========== 网格视图相关方法 ==========
    /**
     * 切换卡片选中状态
     */
    toggleCardSelect(id: string) {
      const idx = this.selectedRowKeys.indexOf(id);
      if (idx > -1) {
        this.selectedRowKeys.splice(idx, 1);
      } else {
        this.selectedRowKeys.push(id);
      }
    },
    /**
     * 显示右键菜单
     */
    showContextMenu(event: MouseEvent, record: FileLibraryPageVo) {
      this.contextRecord = record;
      this.contextMenuStyle = {
        left: `${event.clientX}px`,
        top: `${event.clientY}px`
      };
      this.contextMenuVisible = true;
      // 点击其他地方关闭菜单
      document.addEventListener('click', this.hideContextMenu, { once: true });
    },
    /**
     * 隐藏右键菜单
     */
    hideContextMenu() {
      this.contextMenuVisible = false;
    },
    /**
     * 处理右键菜单点击
     */
    handleContextMenuClick({ key }: { key: string }) {
      this.contextMenuVisible = false;
      const record = this.contextRecord;
      if (!record) return;

      switch (key) {
        case 'copy':
          this.copyLink(record);
          break;
        case 'download':
          this.download(record);
          break;
        case 'rename':
          this.openRename(record);
          break;
        case 'detail':
          this.showDetail(record);
          break;
        case 'delete':
          this.confirmDelete(record);
          break;
      }
    },
    /**
     * 网格视图分页变化
     */
    handleGridPageChange(page: number, pageSize: number) {
      this.queryForm.current = page;
      this.queryForm.size = pageSize;
      this.loadData();
    },
    /**
     * 显示文件详情
     */
    showDetail(record: FileLibraryPageVo) {
      this.detailRecord = record;
      this.detailVisible = true;
    },
    /**
     * 格式化时间显示
     */
    formatDateTime(dateTime: string) {
      if (!dateTime) return '';
      // 将 2025-12-19 04-09-38 格式转为 2025-12-19 04:09:38
      return dateTime.replace(/(\d{2})-(\d{2})-(\d{2})$/, '$1:$2:$3');
    },
    /**
     * 复制详情弹窗中的文件链接
     */
    copyDetailLink() {
      if (!this.detailRecord?.url) {
        message.warning('无可复制的链接');
        return;
      }
      navigator.clipboard.writeText(this.detailRecord.url).then(() => {
        message.success('已复制文件链接');
      }).catch(() => {
        message.error('复制失败');
      });
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

/* 网格视图容器 */
.grid-container {
  padding: 16px 0;
  min-height: 400px;
}

/* 网格视图分页 */
.grid-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
  padding: 16px 0;
}

/* 卡片封面 */
.card-cover {
  position: relative;
  aspect-ratio: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  background-color: var(--ant-color-bg-layout);
  cursor: pointer;
}

.card-cover img {
  max-width: 100%;
  max-height: 100%;
  object-fit: cover;
}

.card-cover .file-icon {
  width: 80px;
  height: 80px;
  object-fit: contain;
}

/* 卡片复选框 */
.card-checkbox {
  position: absolute;
  top: 8px;
  left: 8px;
  z-index: 10;
  opacity: 0;
  transition: opacity 0.2s;
}

.card-cover:hover .card-checkbox,
.grid-card-selected .card-checkbox {
  opacity: 1;
}

/* 卡片选中态 */
.grid-card-selected {
  border-color: var(--ant-color-primary) !important;
  box-shadow: 0 0 0 2px var(--ant-color-primary-bg);
}

/* 卡片文件名 */
.card-filename {
  display: block;
  font-size: 12px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* 空状态 */
.grid-empty {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
}

/* 右键菜单 */
.context-menu-wrapper {
  position: fixed;
  z-index: 1050;
  background-color: var(--ant-color-bg-elevated);
  border-radius: 8px;
  box-shadow: 0 6px 16px 0 rgba(0, 0, 0, 0.08),
              0 3px 6px -4px rgba(0, 0, 0, 0.12),
              0 9px 28px 8px rgba(0, 0, 0, 0.05);
}

.context-menu-wrapper :deep(.ant-menu) {
  border-radius: 8px;
  min-width: 140px;
  border: none;
}

.context-menu-wrapper :deep(.ant-menu-item) {
  margin: 4px 8px;
  padding: 5px 12px;
  border-radius: 4px;
  height: auto;
  line-height: 22px;
}

/* 消除菜单选中态 */
.context-menu-wrapper :deep(.ant-menu-item-selected) {
  background-color: transparent !important;
  color: inherit;
}

/* 修复 hover 样式 */
.context-menu-wrapper :deep(.ant-menu-item:hover) {
  background-color: var(--ant-color-bg-text-hover) !important;
}

.context-menu-wrapper :deep(.ant-menu-item-active) {
  background-color: var(--ant-color-bg-text-hover) !important;
}

/* 详情弹窗预览区 */
.file-detail-content {
  padding: 0 16px;
}

.detail-preview {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 16px;
  margin-bottom: 8px;
  background-color: var(--ant-color-bg-layout);
  border-radius: 8px;
  min-height: 150px;
  max-height: 300px;
  overflow: hidden;
}

.detail-preview img {
  max-width: 100%;
  max-height: 280px;
  object-fit: contain;
  border-radius: 4px;
}
</style>

