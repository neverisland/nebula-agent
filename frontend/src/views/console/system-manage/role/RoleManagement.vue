<template>
  <a-layout>
    <a-layout-header style="height: 150px; padding: 20px;">
      <div>
        <span class="title">角色管理</span>
      </div>
      <a-divider style="margin: 10px 0;"/>
      <a-form :model="queryForm" layout="inline" style="width: 100%;">
        <a-row style="width: 100%;" justify="space-between">
          <!-- 左侧查询项 -->
          <a-col>
            <a-space wrap>
              <a-form-item label="角色名称">
                <a-input v-model:value="queryForm.searchText"
                         placeholder="请输入角色名称"
                         allow-clear
                         @keyup.enter="query"/>
              </a-form-item>
              <a-form-item>
                <a-button type="primary" @click="query">查询</a-button>
                <a-button style="margin-left: 8px" @click="reset">重置</a-button>
              </a-form-item>
            </a-space>
          </a-col>

          <!-- 右侧新增按钮 -->
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
            showTotal: total => `共 ${total} 条`,
          }"
          @change="handleTableChange"
          :scroll="{ y: tableHeight }"
          rowKey="id"
      >
        <a-table-column title="角色名称" dataIndex="name" key="name" :width="220"/>
        <a-table-column title="角色描述" dataIndex="description" key="description"/>
        <a-table-column title="角色类型"
                        dataIndex="type"
                        key="type"
                        :width="220"
                        :customRender="({ text }) => getType(text)"
        />
        <a-table-column title="操作" key="action" :width="220">
          <template #default="{ record }">
            <a-space>
              <a-button type="link" @click="openDialogDetail(record.id)">详情</a-button>
              <a-button v-show="record.type === 1" type="link" @click="editData(record.id)">修改</a-button>
              <a-button v-show="record.type === 1" type="link" danger @click="deleteData(record.id, record.name)">删除
              </a-button>
            </a-space>
          </template>
        </a-table-column>
      </a-table>
    </a-layout-content>

    <a-modal v-model:open="dialogInsert" title="新增角色" :width="660" :footer="null">
      <RoleAdd @closeDialogInsert="closeDialogInsert"/>
    </a-modal>
    <a-modal
        v-model:open="dialogDetail"
        title="角色详情"
        :width="660"
        :footer="null"
    >
      <RoleDetail :selectId="selectId" @closeDialogDetail="closeDialogDetail"/>
    </a-modal>
    <a-modal
        v-model:open="dialogEdit"
        title="修改角色"
        :width="660"
        :footer="null"
    >
      <RoleEdit :selectId="selectId" @closeDialogEdit="closeDialogEdit"/>
    </a-modal>
  </a-layout>
</template>

<script lang="ts">
import {PlusOutlined} from '@ant-design/icons-vue';
import {message, Modal, TablePaginationConfig} from 'ant-design-vue';
import {deleteRoleById, selectRoleList} from "@/api/RoleApi.ts";
import {SysRolePageDto} from "@/type/role/SysRolePageDto.ts";
import RoleAdd from "./RoleAdd.vue";
import RoleEdit from "./RoleEdit.vue";
import RoleDetail from "./RoleDetail.vue";
import {roleEnumStore} from "@/enums/RoleTypeStore.ts";

export default {
  name: "RoleManagement",
  components: {RoleDetail, RoleEdit, RoleAdd, PlusOutlined},
  data() {
    return {
      // 查询表单
      queryForm: {
        current: 1,
        size: 10,
        searchText: ''
      },
      // 表格数据
      tableData: [] as SysRolePageDto[] | undefined, // 表格数据,
      // 总条数
      total: 0,
      // 表格高度
      tableHeight: 0,
      // 弹窗控制
      dialogInsert: false,
      dialogDetail: false,
      dialogEdit: false,
      // 选中id
      selectId: '',
      // 角色类型枚举
      roleTypeEnums: roleEnumStore(),
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
    /**
     * 计算表格高度
     */
    calculateTableHeight() {
      this.tableHeight = window.innerHeight - 365;
      console.log(this.tableHeight)
    },
    /**
     * 查询数据
     */
    query() {
      selectRoleList(this.queryForm).then(res => {
        if (res.data.code === 0) {
          this.tableData = res.data.data.list || [];
          this.total = res.data.data.total;
        } else {
          message.error("查询失败：" + res.data.details);
        }
      }).catch(error => {
        console.error('查询错误:', error)
        message.error("查询失败：" + error.message);
      })
    },
    /**
     * 重置查询条件
     */
    reset() {
      this.queryForm.searchText = '';
      this.query();
    },
    /**
     * 修改每页条数
     */
    handleTableChange(pagination: TablePaginationConfig) {
      this.queryForm.current = pagination.current;
      this.queryForm.size = pagination.pageSize;
      this.query();
    },
    /**
     * 获取角色类型描述
     */
    getType(type: number) {
      return this.roleTypeEnums.getDescriptionByType(type);
    },
    /**
     * 打开详情弹窗
     */
    openDialogDetail(id: string) {
      this.selectId = id;
      this.dialogDetail = true;
    },
    /**
     * 打开修改弹窗
     */
    editData(id: string) {
      this.selectId = id;
      this.dialogEdit = true;
    },
    /**
     * 删除数据
     */
    deleteData(id: string, name: string) {
      Modal.confirm({
        title: '提示',
        content: `确定要删除角色 ${name} 吗？`,
        okText: '确定',
        okType: 'danger',
        cancelText: '取消',
        onOk: () => {
          deleteRoleById(id).then(res => {
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
    },
    /**
     * 关闭新增弹窗
     */
    closeDialogInsert(refresh: boolean) {
      this.dialogInsert = false;
      if (refresh) {
        this.query();
      }
    },
    /**
     * 关闭详情弹窗
     */
    closeDialogDetail() {
      this.dialogDetail = false;
    },
    /**
     * 关闭修改弹窗
     */
    closeDialogEdit(refresh: boolean) {
      this.dialogEdit = false;
      if (refresh) {
        this.query();
      }
    }
  }
}
</script>

<style scoped>

</style>