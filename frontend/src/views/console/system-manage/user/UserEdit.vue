<template>
  <a-form
    ref="form"
    :model="form"
    :rules="rules"
    :label-col="{ span: 6 }"
    :wrapper-col="{ span: 15 }"
  >
    <a-form-item label="用户名" name="username">
      <a-input v-model:value="form.username" autocomplete="off" />
    </a-form-item>
    <a-form-item label="昵称" name="nickname">
      <a-input v-model:value="form.nickname" autocomplete="off" />
    </a-form-item>
    <a-form-item label="手机号" name="phone">
      <a-input v-model:value="form.phone" autocomplete="off" />
    </a-form-item>
    <a-form-item label="邮箱" name="email">
      <a-input v-model:value="form.email" autocomplete="off" />
    </a-form-item>
    <a-form-item label="分配角色" name="roleIdList">
      <a-select
        v-model:value="form.roleIdList"
        :options="roleOptions"
        mode="multiple"
        placeholder="请选择角色"
        show-search
        :filter-option="filterOption"
        style="width: 100%"
      />
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
import { FormInstance, message } from 'ant-design-vue';
import { selectUserById, updateUser } from '@/api/UserApi.ts';
import { selectAllRoles } from '@/api/RoleApi.ts';

export default {
  name: 'UserEdit',
  props: {
    selectId: { type: String, required: true },
  },
  data() {
    return {
      form: {
        id: '',
        username: '',
        nickname: '',
        phone: '',
        email: '',
        roleIdList: [] as string[],
      },
      roleOptions: [] as any[],
      rules: {
        username: [{ min: 2, max: 20, message: '用户名长度在 2 到 20 个字符', trigger: 'blur' }],
        nickname: [{ min: 2, max: 20, message: '昵称长度在 2 到 20 个字符', trigger: 'blur' }],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' },
        ],
        email: [{ type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }],
        roleIdList: [{ required: true, message: '请至少选择一个角色', trigger: 'change', type: 'array' }],
      },
    };
  },
  mounted() {
    this.loadRoles();
    this.initData();
  },
  methods: {
    loadRoles() {
      selectAllRoles().then(res => {
        if (res.data.code === 0) {
          this.roleOptions = res.data.data.map((item: any) => ({ value: item.id, label: item.name, description: item.description }));
        } else {
          message.error('获取角色列表失败：' + res.data.details);
        }
      }).catch(() => {
        message.error('获取角色列表失败');
      });
    },
    filterOption(inputValue: string, item: any) {
      return item.label.indexOf(inputValue) !== -1;
    },
    initData() {
      selectUserById(this.selectId).then(res => {
        if (res.data.code === 0) {
          const data = res.data.data;
          this.form.id = data.id;
          this.form.username = data.username;
          this.form.nickname = data.nickname;
          this.form.phone = data.phone;
          this.form.email = data.email;
          this.form.roleIdList = data.roles && data.roles.length ? data.roles.map((r: any) => r.id) : [];
        } else {
          message.error('获取用户信息失败：' + res.data.details);
        }
      }).catch(() => {
        message.error('获取用户信息失败');
      });
    },
    close(refresh: boolean) {
      this.$emit('closeDialogEdit', refresh);
    },
    save() {
      (this.$refs.form as FormInstance).validate().then(() => {
        updateUser(this.form).then(res => {
          if (res.data.code === 0) {
            message.success('修改用户成功');
            this.close(true);
          } else {
            message.error('修改用户失败：' + res.data.details);
          }
        }).catch(() => {
          message.error('修改用户失败');
        });
      });
    },
  },
};
</script>

<style scoped></style>
