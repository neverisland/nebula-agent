<template>
  <a-transfer
      v-model:target-keys="targetKeys"
      :data-source="roleData"
      :filter-option="filterOption"
      :titles="['可选角色', '已选角色']"
      show-search
      :search-placeholder="'请输入角色名称'"
  />
  <div class="dialog-footer">
    <a-space wrap>
      <a-button @click="close">取消</a-button>
      <a-button type="primary" @click="save">确认</a-button>
    </a-space>
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
      roleData: [],
      targetKeys: [],
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
          this.roleData = res.data.data.map(item => ({
            key: item.id,
            label: item.name,
            description: item.description
          }));
          // 获取用户已分配的角色
          selectUserById(this.selectId).then(res => {
            if (res.data.code === 0) {
              if (res.data.data.roles) {
                this.targetKeys = res.data.data.roles.map(item => item.id);
              } else {
                this.targetKeys = [];
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
    filterOption(inputValue: string, item: any) {
      return item.label.indexOf(inputValue) !== -1;
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
        roleIdList: this.targetKeys
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