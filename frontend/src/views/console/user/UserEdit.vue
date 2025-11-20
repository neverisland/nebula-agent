<template>
  <a-form
      ref="form"
      :model="form"
      :rules="rules"
      :label-col="{ span: 6 }"
      :wrapper-col="{ span: 15 }"
  >
    <a-form-item label="用户名" name="username">
      <a-input v-model:value="form.username" autocomplete="off"/>
    </a-form-item>
    <a-form-item label="昵称" name="nickname">
      <a-input v-model:value="form.nickname" autocomplete="off"/>
    </a-form-item>
    <a-form-item label="手机号" name="phone">
      <a-input v-model:value="form.phone" autocomplete="off"/>
    </a-form-item>
    <a-form-item label="邮箱" name="email">
      <a-input v-model:value="form.email" autocomplete="off"/>
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
import {selectUserById, updateUser} from "@/api/UserApi.ts";

export default {
  name: "UserEdit",
  props: {
    selectId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      form: {
        id: '',
        username: '',
        nickname: '',
        phone: '',
        email: ''
      },
      rules: {
        username: [
          {min: 2, max: 20, message: '用户名长度在 2 到 20 个字符', trigger: 'blur'}
        ],
        nickname: [
          {min: 2, max: 20, message: '昵称长度在 2 到 20 个字符', trigger: 'blur'}
        ],
        phone: [
          {required: true, message: '请输入手机号', trigger: 'blur'},
          {pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur'}
        ],
        email: [
          {type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur'}
        ]
      }
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
      selectUserById(this.selectId).then(res => {
        if (res.data.code === 0) {
          this.form = res.data.data;
        } else {
          message.error('获取用户信息失败：' + res.data.details);
        }
      }).catch(() => {
        message.error('获取用户信息失败');
      });
    },
    /**
     * 关闭弹窗
     */
    close(refresh: boolean) {
      this.$emit('closeDialogEdit', refresh);
    },
    /**
     * 保存
     */
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
    }
  }
}
</script>

<style scoped>

</style>
