<template>
  <div style="padding: 20px;">
    <a-form layout="vertical">
      <a-form-item label="选择角色">
        <a-select
            v-model:value="selectedRoleIds"
            mode="multiple"
            style="width: 100%"
            placeholder="请选择角色"
            :options="roleOptions"
            :filter-option="filterOption"
        />
      </a-form-item>
      <div class="dialog-footer" style="text-align: right; margin-top: 20px;">
        <a-space>
          <a-button @click="close">取消</a-button>
          <a-button type="primary" @click="save">确认</a-button>
        </a-space>
      </div>
    </a-form>
  </div>
</template>

<script lang="ts">
import {message} from 'ant-design-vue';
import {selectAllRoles} from "@/api/RoleApi.ts";
import {assignRole, selectUserById} from "@/api/UserApi.ts";

export default {
  name: "AssigningRole",
  props: {
    selectId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      roleOptions: [] as any[],
      selectedRoleIds: [] as string[],
    }
  },
  mounted() {
    this.initData();
  },
  methods: {
    /**
     * 初始化数据
     */
    initData() {
      // 获取所有角色列表
      selectAllRoles().then(res => {
        if (res.data.code === 0) {
          this.roleOptions = res.data.data.map(item => ({
            value: item.id,
            label: item.name,
            description: item.description
          }));
          // 获取用户已分配的角色
          selectUserById(this.selectId).then(res => {
            if (res.data.code === 0) {
              if (res.data.data.roles) {
                this.selectedRoleIds = res.data.data.roles.map(item => item.id);
              } else {
                this.selectedRoleIds = [];
              }
            } else {
              message.error('获取用户角色失败：' + res.data.details);
            }
          }).catch(() => {
            message.error('获取用户角色失败');
          });
        } else {
          message.error('获取角色列表失败：' + res.data.details);
        }
      }).catch(() => {
        message.error('获取角色列表失败');
      });
    },
    /**
     * 过滤选项
     */
    filterOption(input: string, option: any) {
      return option.label.toLowerCase().indexOf(input.toLowerCase()) >= 0;
    },
    /**
     * 关闭弹窗
     */
    close() {
      this.$emit('closeDialogAssigningRole', false);
    },
    /**
     * 保存
     */
    save() {
      assignRole({
        userId: this.selectId,
        roleIdList: this.selectedRoleIds
      }).then(res => {
        if (res.data.code === 0) {
          message.success('分配角色成功');
          this.$emit('closeDialogAssigningRole', true);
        } else {
          message.error('分配角色失败：' + res.data.details);
        }
      }).catch(() => {
        message.error('分配角色失败');
      });
    }
  }
}
</script>

<style scoped>
</style>