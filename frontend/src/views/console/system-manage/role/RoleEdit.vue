<template>
  <a-form
      ref="form"
      :model="form"
      :rules="rules"
      :label-col="{ span: 5 }"
      :wrapper-col="{ span: 18 }"
  >
    <a-form-item label="角色名称" name="name">
      <a-input v-model:value="form.name"/>
    </a-form-item>
    <a-form-item label="角色描述" name="description">
      <a-input v-model:value="form.description"/>
    </a-form-item>
    <a-form-item label="权限列表" name="permissionIdList">
      <a-form-item-rest>
        <a-table
            ref="tableRef"
            row-key="id"
            :pagination="false"
            :columns="columns"
            :data-source="permissionList"
            :row-selection="rowSelection"
        />
      </a-form-item-rest>
    </a-form-item>
  </a-form>
  <div class="dialog-footer">
    <a-space wrap>
      <a-button @click="close(false)">取消</a-button>
      <a-button type="primary" @click="save">保存</a-button>
    </a-space>
  </div>
</template>

<script lang="ts">
import {FormInstance, message} from 'ant-design-vue';
import {selectAllAuthorityList} from "@/api/AuthorityApi.ts";
import {selectRoleById, updateRole} from "@/api/RoleApi.ts";
import {SysPermissionVo} from "@/type/permission/SysPermissionVo.ts";

export default {
  name: "RoleEdit",
  data() {
    return {
      id: this.selectId, // 详情id
      permissionList: [] as SysPermissionVo[] | undefined, // 权限列表
      // 角色表单数据
      form: {
        id: "",
        name: "",
        description: "",
        permissionIdList: [] as string[]
      },
      rules: {
        name: [{required: true, message: '角色名称不能为空'}],
        permissionIdList: [{required: true, message: '权限列表不能为空'}]
      },
      columns: [
        {
          title: '权限标识',
          dataIndex: 'mark',
          width: 180,
        },
        {
          title: '权限描述',
          dataIndex: 'description',
        },
      ],
    }
  },
  computed: {
    rowSelection() {
      return {
        selectedRowKeys: this.form.permissionIdList,
        onChange: (selectedRowKeys: any[]) => {
          console.log(selectedRowKeys)
          this.form.permissionIdList = selectedRowKeys;
        },
      };
    },
  },
  emits: ["closeDialogEdit"],
  props: ['selectId'],
  mounted() {
    Promise.all([
      selectAllAuthorityList(),
      selectRoleById(this.id)
    ]).then(([authRes, roleRes]) => {
      // 处理权限数据
      if (authRes.data.code === 0) {
        this.permissionList = authRes.data.data;
      }
      // 处理角色数据
      if (roleRes.data.code === 0) {
        const role = roleRes.data.data;
        this.form.id = role.id;
        this.form.name = role.name;
        this.form.description = role.description;
        this.form.permissionIdList = role.permissionList?.map(p => p.id) || [];
      }
    })
  },
  methods: {
    /**
     * 设置整行选中
     */
    handleRowClick(record: SysPermissionVo) {
      const index = this.form.permissionIdList.indexOf(record.id);
      if (index === -1) {
        this.form.permissionIdList.push(record.id);
      } else {
        this.form.permissionIdList.splice(index, 1);
      }
    },
    /**
     * 关闭弹窗
     * @param refresh 是否需要刷新
     */
    close(refresh: boolean) {
      this.$emit('closeDialogEdit', refresh);
    },
    /**
     * 保存角色
     */
    save() {
      (this.$refs.form as FormInstance).validate().then(() => {
        updateRole(this.form).then(res => {
          if (res.data.code === 0) {
            message.success('保存成功');
            this.close(true);
          } else {
            message.error(res.data.details || '保存失败');
          }
        }).catch(err => {
          console.log("保存失败", err);
          message.error('保存失败');
        });
      }).catch(() => {
        message.error('请填写完整信息');
      });
    }
  }
}
</script>

<style scoped>

</style>