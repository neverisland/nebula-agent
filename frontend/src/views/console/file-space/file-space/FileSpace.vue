<template>
  <a-layout>
    <a-layout-header class="space-manage-header">
      <div>
        <span class="title">个人空间</span>
      </div>
      <a-divider style="margin: 10px 0;"/>
      <a-form :model="queryForm" layout="inline" style="width: 100%;">
        <a-row style="width: 100%;" justify="space-between">
          <a-col>
            <a-space wrap>
              <a-form-item label="空间名称">
                <a-input v-model:value="queryForm.searchText"
                         placeholder="请输入名称"
                         allow-clear
                         @keyup.enter="query"/>
              </a-form-item>
              <a-form-item>
                <a-button type="primary" @click="query">查询</a-button>
                <a-button style="margin-left: 8px" @click="reset">重置</a-button>
              </a-form-item>
            </a-space>
          </a-col>
          <a-col>
            <a-button type="primary" @click="dialogInsert = true">
              <template #icon>
                <PlusOutlined/>
              </template>
              新增
            </a-button>
          </a-col>
        </a-row>
      </a-form>
    </a-layout-header>
    <a-layout-content style="padding: 0 20px;">
      <a-table
          :data-source="tableData"
          :pagination="{
            current: queryForm.current,
            pageSize: queryForm.size,
            total: total,
            showSizeChanger: true,
            pageSizeOptions: ['10', '50', '100'],
            showTotal: (total: number) => `共 ${total} 条`,
          }"
          @change="handleTableChange"
          :scroll="{ y: tableHeight }"
          rowKey="id"
      >
        <a-table-column title="空间名称" key="name" :width="200">
          <template #default="{ record }">
            <a @click="goToFiles(record)">{{ record.name }}</a>
          </template>
        </a-table-column>
        <a-table-column title="文件数量" dataIndex="fileCount" key="fileCount" :width="100"/>
        <a-table-column title="已用空间" key="totalSize" :width="120">
          <template #default="{ record }">
            {{ formatSize(record.totalSize) }}
          </template>
        </a-table-column>
        <a-table-column title="说明" dataIndex="remark" key="remark"/>
        <a-table-column title="创建时间" dataIndex="createTime" key="createTime" :width="180"/>
        <a-table-column title="操作" key="action" :width="200" align="center">
          <template #default="{ record }">
            <a-space>
              <a-button type="link" @click="editData(record)">修改</a-button>
              <a-button type="link" danger @click="deleteData(record.id, record.name)">删除</a-button>
            </a-space>
          </template>
        </a-table-column>
      </a-table>
    </a-layout-content>

    <!-- 弹窗组件 -->
    <FileSpaceAddModal v-model:open="dialogInsert" @success="query" />
    <FileSpaceEditModal v-model:open="dialogEdit" :editData="selectData" @success="query" />

  </a-layout>
</template>

<script lang="ts">
import { PlusOutlined } from '@ant-design/icons-vue';
import { message, Modal, TablePaginationConfig } from 'ant-design-vue';
import { deleteFileSpace, getFileSpacePage } from "@/api/FileSpaceApi.ts";
import { FileSpaceVo } from "@/type/filespace/FileSpaceVo.ts";
import FileSpaceAddModal from "./FileSpaceAddModal.vue";
import FileSpaceEditModal from "./FileSpaceEditModal.vue";

/**
 * 个人空间管理页面
 */
export default {
  name: "FileSpace",
  components: { PlusOutlined, FileSpaceAddModal, FileSpaceEditModal },
  data() {
    return {
      queryForm: {
        current: 1,
        size: 10,
        searchText: ''
      },
      tableData: [] as FileSpaceVo[],
      total: 0,
      tableHeight: 0,
      dialogInsert: false,
      dialogEdit: false,
      selectData: null as FileSpaceVo | null,
    }
  },
  mounted() {
    this.query();
    this.calculateTableHeight();
    window.addEventListener('resize', this.calculateTableHeight);
  },
  beforeUnmount() {
    window.removeEventListener('resize', this.calculateTableHeight);
  },
  methods: {
    calculateTableHeight() {
      this.tableHeight = window.innerHeight - 365;
    },
    query() {
      getFileSpacePage(this.queryForm).then(res => {
        if (res.data.code === 0) {
          this.tableData = res.data.data.list || [];
          this.total = res.data.data.total;
        } else {
          message.error("查询失败：" + res.data.details);
        }
      }).catch(error => {
        console.error('查询错误:', error);
        message.error("查询失败");
      });
    },
    reset() {
      this.queryForm.searchText = '';
      this.queryForm.current = 1;
      this.query();
    },
    handleTableChange(pagination: TablePaginationConfig) {
      this.queryForm.current = pagination.current || 1;
      this.queryForm.size = pagination.pageSize || 10;
      this.query();
    },
    editData(record: FileSpaceVo) {
      this.selectData = record;
      this.dialogEdit = true;
    },
    formatSize(size: number) {
      if (!size) return '0 B';
      if (size < 1024) return `${size} B`;
      if (size < 1024 * 1024) return `${(size / 1024).toFixed(1)} KB`;
      if (size < 1024 * 1024 * 1024) return `${(size / 1024 / 1024).toFixed(1)} MB`;
      return `${(size / 1024 / 1024 / 1024).toFixed(1)} GB`;
    },
    goToFiles(record: FileSpaceVo) {
      this.$router.push({
        name: 'file-library',
        query: { spaceId: record.id, spaceName: record.name }
      });
    },
    deleteData(id: string, name: string) {
      Modal.confirm({
        title: '提示',
        content: `确定要删除个人空间 "${name}" 吗？`,
        okText: '确定',
        okType: 'danger',
        cancelText: '取消',
        onOk: () => {
          deleteFileSpace(id).then(res => {
            if (res.data.code === 0) {
              message.success('删除成功');
              this.query();
            } else {
              message.error('删除失败：' + res.data.details);
            }
          }).catch(() => {
            message.error('删除失败');
          });
        },
      });
    }
  }
}
</script>

<style scoped>
.space-manage-header {
  height: 150px;
  padding: 20px;
  background-color: var(--ant-color-bg-container);
}
.title {
  font-size: 18px;
  font-weight: bold;
}
</style>
