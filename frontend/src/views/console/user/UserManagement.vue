<template>
  <a-layout>
    <a-layout-header style="height: 150px; padding: 20px;">
      <div>
        <span class="title">用户管理</span>
      </div>
      <a-divider style="margin: 10px 0;"/>
      <a-form :model="queryForm" layout="inline" style="width: 100%;">
        <a-row style="width: 100%;" justify="space-between">
          <!-- 左侧查询项 -->
          <a-col>
            <a-space wrap>
              <a-form-item label="关键词">
                <a-input
                    v-model:value="queryForm.searchText"
                    placeholder="请输入昵称/手机号"
                    allow-clear
                    @keyup.enter="query"
                />
              </a-form-item>
              <a-form-item label="用户状态">
                <a-select
                    v-model:value="queryForm.enabled"
                    placeholder="请选择用户状态"
                    allow-clear
                    style="width: 120px"
                >
                  <a-select-option :value="true">正常</a-select-option>
                  <a-select-option :value="false">冻结</a-select-option>
                </a-select>
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
        <a-table-column title="用户名" dataIndex="username" key="username" :width="150"/>
        <a-table-column title="昵称" dataIndex="nickname" key="nickname" :width="150"/>
        <a-table-column title="手机号" dataIndex="phone" key="phone" :width="150"/>
        <a-table-column title="用户状态" key="status" :width="120" :align="center">
          <template #default="{ record }">
            <a-switch
                v-model:checked="record.enabled"
                :before-change="() => changeStatus(record)"
            />
          </template>
        </a-table-column>
        <a-table-column title="操作" key="action" :width="300" :align="'center'">
          <template #default="{ record }">
            <a-space>
              <a-button type="link" @click="openDialogDetail(record.id)">详情</a-button>
              <a-button type="link" @click="editData(record.id)">修改</a-button>
              <a-button type="link" @click="openDialogResetPassword(record.id)">重置密码</a-button>
              <a-button type="link" @click="openDialogAssigningRole(record.id)">分配角色</a-button>
              <a-button type="link" danger @click="deleteData(record.id, record.nickname)">删除</a-button>
            </a-space>
          </template>
        </a-table-column>
      </a-table>
    </a-layout-content>

    <a-modal v-model:open="dialogInsert" title="新增用户" :width="600" :footer="null">
      <UserAdd @closeDialogInsert="closeDialogInsert"/>
    </a-modal>
    <a-modal v-model:open="dialogDetail" title="用户详情" :width="600" :footer="null">
      <UserDetail :selectId="selectId" @closeDialogDetail="closeDialogDetail"/>
    </a-modal>
    <a-modal v-model:open="dialogEdit" title="修改用户" :width="600" :footer="null">
      <UserEdit :selectId="selectId" @closeDialogEdit="closeDialogEdit"/>
    </a-modal>
    <a-modal v-model:open="dialogResetPassword" title="重置密码" :width="400" :footer="null">
      <ResetPassword :selectId="selectId" @closeDialogResetPassword="closeDialogResetPassword"/>
    </a-modal>
    <a-modal v-model:open="dialogAssigningRole" title="分配角色" :width="600" :footer="null">
      <AssigningRole :selectId="selectId" @closeDialogAssigningRole="closeDialogAssigningRole"/>
    </a-modal>
  </a-layout>
</template>


<script lang="ts">
import {PlusOutlined} from '@ant-design/icons-vue';
import {message, Modal, TablePaginationConfig} from 'ant-design-vue';
import {deleteUserById, disableEnableUser, selectUserList} from "@/api/UserApi.ts";
import {UserPageDto} from "@/type/user/UserPageDto.ts";
import UserAdd from "./UserAdd.vue";
import UserEdit from "./UserEdit.vue";
import UserDetail from "./UserDetail.vue";
import {enabledEnumStore} from "@/enums/EnabledStore.ts";
import ResetPassword from "@/views/console/user/ResetPassword.vue";
import AssigningRole from "@/views/console/user/AssigningRole.vue";

export default {
  name: "UserManagement",
  components: {AssigningRole, ResetPassword, UserDetail, UserEdit, UserAdd, PlusOutlined},
  data() {
    return {
      // 查询表单
      queryForm: {
        current: 1,
        size: 10,
        searchText: '',
        enabled: undefined as boolean | undefined
      },
      // 表格数据
      tableData: [] as UserPageDto[],
      // 总条数
      total: 0,
      // 表格高度
      tableHeight: 0,
      // 弹窗控制
      dialogInsert: false,
      dialogDetail: false,
      dialogEdit: false,
      dialogResetPassword: false,
      dialogAssigningRole: false,
      // 选中id
      selectId: '',
      // 状态枚举
      enableEnums: enabledEnumStore(),
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
      this.tableHeight = window.innerHeight - 300;
    },
    /**
     * 查询数据
     */
    query() {
      console.log('查询参数:', this.queryForm)
      selectUserList(this.queryForm).then(res => {
        console.log('查询结果:', res)
        if (res.data.code === 0) {
          this.tableData = res.data.data.list || [];
          this.total = res.data.data.total;
          console.log('赋值后的数据:', this.tableData)
          console.log('赋值后的总数:', this.total)
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
      this.queryForm.enabled = undefined;
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
     * 修改用户状态
     */
    changeStatus(row: UserPageDto) {
      return new Promise((resolve) => {
        const status = row.enabled ? '禁用' : '启用';
        Modal.confirm({
          title: '提示',
          content: `确定要${status}用户 ${row.nickname} 吗？`,
          onOk: () => {
            disableEnableUser(row.id).then(res => {
              if (res.data.code === 0) {
                message.success(`${status}成功`);
                resolve(true);
              } else {
                message.error(`${status}失败：${res.data.details}`);
                resolve(false);
              }
            }).catch(() => {
              message.error(`${status}失败`);
              resolve(false);
            });
          },
          onCancel: () => {
            resolve(false);
          },
        });
      });
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
     * 打开重置密码弹窗
     */
    openDialogResetPassword(id: string) {
      this.selectId = id;
      this.dialogResetPassword = true;
    },
    /**
     * 打开分配角色弹窗
     */
    openDialogAssigningRole(id: string) {
      this.selectId = id;
      this.dialogAssigningRole = true;
    },
    /**
     * 删除数据
     */
    deleteData(id: string, nickname: string) {
      Modal.confirm({
        title: '提示',
        content: `确定要删除用户 ${nickname} 吗？`,
        okText: '确定',
        okType: 'danger',
        cancelText: '取消',
        onOk: () => {
          deleteUserById(id).then(res => {
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
    },
    /**
     * 关闭重置密码弹窗
     */
    closeDialogResetPassword(refresh: boolean) {
      this.dialogResetPassword = false;
      if (refresh) {
        this.query();
      }
    },
    /**
     * 关闭分配角色弹窗
     */
    closeDialogAssigningRole(refresh: boolean) {
      this.dialogAssigningRole = false;
      if (refresh) {
        this.query();
      }
    }
  }
}
</script>

<style scoped>

</style>