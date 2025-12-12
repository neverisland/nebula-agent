<template>
  <a-descriptions :column="2" style="padding: 20px 40px;">
    <a-descriptions-item label="用户名">{{ data.username }}</a-descriptions-item>
    <a-descriptions-item label="昵称">{{ data.nickname }}</a-descriptions-item>
    <a-descriptions-item label="手机号">{{ data.phone }}</a-descriptions-item>
    <a-descriptions-item label="邮箱">{{ data.email }}</a-descriptions-item>
    <a-descriptions-item label="角色列表">
      <a-tag v-for="role in data.roles" :key="role.id" style="margin-right: 4px;">{{ role.name }}</a-tag>
    </a-descriptions-item>
    <a-descriptions-item label="用户状态">
      <a-tag :color="data.enabled ? 'success' : 'error'">
        {{ enableEnums.getDescriptionByType(data.enabled) }}
      </a-tag>
    </a-descriptions-item>
    <a-descriptions-item label="是否锁定">
      <a-tag :color="data.accountNonLocked ? 'success' : 'warning'">
        {{ lockStatusEnums.getDescriptionByType(data.accountNonLocked) }}
      </a-tag>
    </a-descriptions-item>
    <a-descriptions-item label="注册时间">{{ data.createTime }}</a-descriptions-item>
  </a-descriptions>
  <div class="dialog-footer">
    <a-button @click="close">关闭</a-button>
  </div>
</template>

<script lang="ts">
import {message} from 'ant-design-vue';
import {UserDto} from "@/type/user/UserDto.ts";
import {selectUserById} from "@/api/UserApi.ts";
import {enabledEnumStore} from "@/enums/EnabledStore.ts";
import {lockStatusStore} from "@/enums/LockStatusStore.ts";

export default {
  name: "UserDetail",
  data() {
    return {
      id: this.selectId, // 详情id
      data: {} as UserDto, // 用户数据
      enableEnums: enabledEnumStore(), // Store 实例
      lockStatusEnums: lockStatusStore() // Store 实例
    }
  },
  emits: ["closeDialogDetail"],
  props: ['selectId'],
  mounted() {
    selectUserById(this.id).then(res => {
      if (res.data.code === 0 && res.data.data) {
        this.data = res.data.data;
      } else {
        message.error("获取用户数据失败：" + res.data.details);
      }
    })
  },
  methods: {
    /**
     * 关闭弹窗
     */
    close() {
      this.$emit('closeDialogDetail', false);
    }
  }
}
</script>

<style scoped>
.dialog-footer {
  text-align: right;
  margin-top: 20px;
}
</style>
