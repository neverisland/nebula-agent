<template>
  <a-descriptions :column="1" style="padding: 20px 40px;">
    <a-descriptions-item label="角色名称">{{ data.name }}</a-descriptions-item>
    <a-descriptions-item label="角色描述">{{ data.description }}</a-descriptions-item>
    <a-descriptions-item label="角色类型">{{ roleTypeEnums.getDescriptionByType(data.type) }}</a-descriptions-item>
    <a-descriptions-item label="权限列表">
      {{ getPermissionListStr(data.permissionList) }}
    </a-descriptions-item>
  </a-descriptions>
  <div class="dialog-footer">
    <a-button @click="close">关闭</a-button>
  </div>
</template>

<script lang="ts">
import { message } from 'ant-design-vue';
import { selectRoleById } from "@/api/RoleApi.ts";
import { SysRoleVo } from "@/type/role/SysRoleVo.ts";
import { selectAllAuthorityList } from "@/api/AuthorityApi.ts";
import { SysPermissionVo } from "@/type/permission/SysPermissionVo.ts";
import { roleEnumStore } from "@/enums/RoleTypeStore.ts";

export default {
  name: "RoleDetail",
  data() {
    return {
      id: this.selectId, // 详情id
      data: {} as SysRoleVo, // 权限数据
      permissionList: [] as SysPermissionVo[] | undefined, // 权限列表
      roleTypeEnums: roleEnumStore() // store 实例  角色类型枚举
    }
  },
  emits: ["closeDialogDetail"],
  props: ['selectId'],
  mounted() {
    // 获取角色数据
    selectRoleById(this.id).then(res => {
      if (res.data.code === 0 && res.data.data) {
        this.data = res.data.data;
      } else {
        message.error("获取权限数据失败：" + res.data.details);
      }
    })
    // 获取权限树数据
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
     */
    close() {
      this.$emit('closeDialogDetail', false);
    },
    /**
     * 获取权限列表字符串格式
     */
    getPermissionListStr(permissionList: SysPermissionVo[]): string {
      let returnStr = '';
      if (permissionList) {
        for (let item of permissionList) {
          if (returnStr !== '') {
            returnStr += ',';
          }
          returnStr += item.description;
        }
      }
      return returnStr;
    }
  }
}
</script>

<style scoped>

</style>
