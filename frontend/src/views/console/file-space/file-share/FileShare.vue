<template>
  <a-layout>
    <a-layout-header class="file-share-header">
      <div>
        <span class="title">分享管理</span>
      </div>
      <a-divider style="margin: 10px 0;"/>
      <a-form :model="queryParams" layout="inline" style="width: 100%;">
        <a-row style="width: 100%;" justify="space-between">
          <!-- 左侧查询项 -->
          <a-col>
            <a-space wrap>
              <a-form-item label="关键词">
                <a-input
                    v-model:value="queryParams.name"
                    placeholder="请输入分享名称"
                    allow-clear
                    @keyup.enter="handleSearch"
                />
              </a-form-item>
              <a-form-item label="分享类型">
                <a-select
                    v-model:value="queryParams.shareType"
                    placeholder="请选择类型"
                    allow-clear
                    style="width: 120px"
                >
                  <a-select-option :value="ShareTypeEnum.FILE">个人文件</a-select-option>
                  <a-select-option :value="ShareTypeEnum.SPACE">个人空间</a-select-option>
                </a-select>
              </a-form-item>
              <a-form-item label="状态">
                <a-select
                    v-model:value="queryParams.isExpired"
                    placeholder="请选择状态"
                    allow-clear
                    style="width: 120px"
                >
                  <a-select-option :value="false">有效</a-select-option>
                  <a-select-option :value="true">已过期</a-select-option>
                </a-select>
              </a-form-item>
              <a-form-item>
                <a-button type="primary" @click="handleSearch">查询</a-button>
                <a-button style="margin-left: 8px" @click="handleReset">重置</a-button>
              </a-form-item>
            </a-space>
          </a-col>

          <a-col>
          </a-col>
        </a-row>
      </a-form>
    </a-layout-header>

    <a-layout-content style="padding: 0 20px;">
      <a-table
          :data-source="listData"
          :pagination="{
            current: queryParams.current,
            pageSize: queryParams.size,
            total: totalInfo,
            showSizeChanger: true,
            pageSizeOptions: ['10', '50', '100'],
            showTotal: (total: number) => `共 ${total} 条`,
            onChange: onPageChange
          }"
          :loading="loading"
          rowKey="id"
      >
        <a-table-column title="分享名称" dataIndex="name" key="name" :width="200"/>
        
        <a-table-column title="类型" key="shareType" :width="120" align="center">
          <template #default="{ record }">
             <a-tag v-if="record.shareType === ShareTypeEnum.FILE" color="blue">个人文件</a-tag>
             <a-tag v-else-if="record.shareType === ShareTypeEnum.SPACE" color="purple">个人空间</a-tag>
          </template>
        </a-table-column>

        <a-table-column title="密码保护" key="password" :width="150" align="center">
          <template #default="{ record }">
             <a-space v-if="record.enablePassword">
                 <span style="font-family: monospace;">{{ record.password }}</span>
                 <a-button type="link" size="small" @click="handleCopy(record.password)">
                     <template #icon><CopyOutlined /></template>
                </a-button>
            </a-space>
            <span v-else>-</span>
          </template>
        </a-table-column>
        <a-table-column title="过期时间" dataIndex="expireTime" key="expireTime" :width="120" align="center"/>



        <a-table-column title="访问次数" dataIndex="visitCount" key="visitCount" :width="100" align="center"/>
        <a-table-column title="下载次数" dataIndex="downloadCount" key="downloadCount" :width="100" align="center"/>
        
        <a-table-column title="状态" key="status" :width="100" align="center">
          <template #default="{ record }">
             <a-tag v-if="record.isExpired" color="red">已过期</a-tag>
             <a-tag v-else color="green">有效</a-tag>
          </template>
        </a-table-column>

        <a-table-column title="创建时间" dataIndex="createTime" key="createTime" :width="180" align="center"/>

        <a-table-column title="操作" key="action" :width="220" align="center" fixed="right">
          <template #default="{ record }">
            <a-space>
              <a-button type="link" size="small" @click="openDialogDetail(record.id)">详情</a-button>
              <a-button type="link" size="small" @click="handleCopy(record.shareUrl)">复制链接</a-button>
              <a-button 
                type="link" 
                size="small" 
                :disabled="record.isExpired"
                @click="openDialogEdit(record.id)"
              >
                编辑
              </a-button>
              <a-button type="link" size="small" danger @click="handleDelete(record)">删除</a-button>
            </a-space>
          </template>
        </a-table-column>
      </a-table>
    </a-layout-content>

    <!-- Modals -->
    <a-modal v-model:open="dialogEdit" title="修改分享" :width="600" :footer="null">
      <FileShareEdit :selectId="selectId" @closeDialogEdit="closeDialogEdit"/>
    </a-modal>
    <a-modal v-model:open="dialogDetail" title="分享详情" :width="600" :footer="null">
      <FileShareDetail :selectId="selectId" @closeDialogDetail="closeDialogDetail"/>
    </a-modal>
  </a-layout>
</template>

<script lang="ts">
import { h } from 'vue';
import { message, Modal } from 'ant-design-vue';
import { ExclamationCircleOutlined, PlusOutlined, CopyOutlined } from '@ant-design/icons-vue';
import { getSharePage, deleteShare } from '@/api/FileShareApi';
import type { FileShareVo } from '@/type/file-share/vo/FileShareVo';
import type { FileSharePageQueryPo } from '@/type/file-share/po/FileSharePageQueryPo';
import { ShareTypeEnum } from '@/enums/ShareTypeEnum';
import FileShareEdit from './FileShareEdit.vue';
import FileShareDetail from './FileShareDetail.vue';

export default {
  name: "FileShare",
  components: {
    FileShareEdit,
    FileShareDetail,
    PlusOutlined,
    CopyOutlined
  },
  data() {
    return {
      // 加载状态
      loading: false,
      // 列表数据
      listData: [] as FileShareVo[],
      // 总条数
      totalInfo: 0,
      // 弹窗控制：编辑
      dialogEdit: false,
      // 弹窗控制：详情
      dialogDetail: false,
      // 当前选中的ID
      selectId: '',
      // 查询参数
      queryParams: {
        current: 1,
        size: 10,
        name: undefined,
        shareType: undefined,
        isExpired: undefined
      } as FileSharePageQueryPo,
      // 枚举
      ShareTypeEnum,
    };
  },
  mounted() {
    this.loadData();
  },
  methods: {
    /**
     * 加载列表数据
     */
    async loadData() {
      this.loading = true;
      try {
        const res = await getSharePage(this.queryParams);
        if(res.data && res.data.data) {
            this.listData = res.data.data.list || [];
            this.totalInfo = res.data.data.total;
        }
      } finally {
        this.loading = false;
      }
    },

    /**
     * 查询按钮点击事件
     */
    handleSearch() {
      this.queryParams.current = 1;
      this.loadData();
    },

    /**
     * 重置按钮点击事件
     */
    handleReset() {
      this.queryParams.name = undefined;
      this.queryParams.shareType = undefined;
      this.queryParams.isExpired = undefined;
      this.handleSearch();
    },

    /**
     * 分页变化事件
     * @param page 页码
     * @param size 每页条数
     */
    onPageChange(page: number, size: number) {
      this.queryParams.current = page;
      this.queryParams.size = size;
      this.loadData();
    },

    /**
     * 打开编辑弹窗
     * @param id 分享ID
     */
    openDialogEdit(id: string) {
      // 检查分享是否过期
      const record = this.listData.find(item => item.id === id);
      if (record && record.isExpired) {
        message.warning('已过期的分享不能进行修改');
        return;
      }
      this.selectId = id;
      this.dialogEdit = true;
    },

    /**
     * 打开详情弹窗
     * @param id 分享ID
     */
    openDialogDetail(id: string) {
      this.selectId = id;
      this.dialogDetail = true;
    },

    /**
     * 关闭编辑弹窗
     * @param refresh 是否刷新列表
     */
    closeDialogEdit(refresh: boolean) {
      this.dialogEdit = false;
      if (refresh) this.loadData();
    },

    /**
     * 关闭详情弹窗
     */
    closeDialogDetail() {
      this.dialogDetail = false;
    },

    /**
     * 删除分享
     * @param record 分享对象
     */
    handleDelete(record: FileShareVo) {
      Modal.confirm({
        title: '确认删除',
        icon: () => h(ExclamationCircleOutlined),
        content: `确定要删除分享 "${record.name}" 吗？删除后链接将失效。`,
        onOk: async () => {
          try {
            await deleteShare(record.id);
            message.success('删除成功');
            this.loadData();
          } catch (e) {
            console.error(e);
          }
        }
      });
    },
    /**
     * 复制文本
     */
    handleCopy(text: string) {
       navigator.clipboard.writeText(text).then(() => {
         message.success('复制成功');
       }).catch(() => {
         message.error('复制失败');
       });
    }
  }
};
</script>

<style scoped>
.file-share-header {
  height: 150px;
  padding: 20px;
  background-color: var(--ant-color-bg-container);
}
.search-bar {
  margin-bottom: 16px;
  display: flex;
  justify-content: space-between;
}
.table-container {
  background: #fff;
}
</style>
