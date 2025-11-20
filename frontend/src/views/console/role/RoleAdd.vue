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
    <a-form-item label="角色描述">
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
      <a-button key="back" @click="close(false)">取消</a-button>
      <a-button key="submit" type="primary" @click="save">保存</a-button>
    </a-space>
  </div>
</template>

<script lang="ts">
import {FormInstance, message} from 'ant-design-vue';
import {reactive} from 'vue';
import {insertRole} from "@/api/RoleApi.ts";
import {selectAllAuthorityList} from "@/api/AuthorityApi.ts";
import {SysPermissionDto} from "@/type/permission/SysPermissionDto.ts";

export default {
  name: "RoleAdd",
  data() {
    return {
      permissionList: [] as SysPermissionDto[] | undefined, // 权限列表
      // 新增权限初始化数据
      form: reactive({
        name: "",
        description: "",
        permissionIdList: [] as string[]
      }),
      rules: {
        name: [{required: true, message: '角色名称不能为空'}],
        permissionIdList: [{required: true, message: '权限列表不能为空', trigger: 'manual'}]
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
        onChange: (selectedRowKeys: string[]) => {
          console.log(selectedRowKeys)
          this.form.permissionIdList = selectedRowKeys;
        },
      };
    },
  },
  emits: ["closeDialogInsert"],
  mounted() {
    selectAllAuthorityList().then(res => {
      if (res.data.code === 0) {
        this.permissionList = res.data.data;
      } else {
        message.error("权限数据查询失败");
      }
    })
  },
  methods: {
    /**
     * 关闭弹窗
     * @param refresh 是否需要刷新
     */
    close(refresh: boolean) {
      this.$emit('closeDialogInsert', refresh);
    },
    /**
     * 保存数据
     */
    save() {
      (this.$refs.form as FormInstance).validate().then(() => {
        insertRole(this.form).then(res => {
          if (res.data.code === 0) {
            message.success('新增角色成功');
            this.close(true);
          } else {
            message.error(res.data.details || '新增角色失败');
          }
        }).catch(err => {
          console.log("保存失败", err);
          message.error('新增角色失败');
        });
      }).catch(() => {
        message.error('请填写完整信息');
      });
    },
  }
}
</script>

<style scoped>

</style>