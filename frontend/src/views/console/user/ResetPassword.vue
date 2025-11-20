<template>
  <a-form
      ref="form"
      :model="form"
      :rules="rules"
      :label-col="{ span: 6 }"
      :wrapper-col="{ span: 15 }"
  >
    <a-form-item label="密码" name="password">
      <a-input v-model:value="form.password" autocomplete="off"/>
    </a-form-item>
  </a-form>
  <div class="dialog-footer">
    <a-space wrap>
      <a-button @click="close">取消</a-button>
      <a-button type="primary" @click="save">确认</a-button>
    </a-space>
  </div>
</template>

<script lang="ts">
import {FormInstance, message} from 'ant-design-vue';
import {resetPassword} from "@/api/AuthenticationPasswordApi.ts";

export default {
  name: "ResetPassword",
  props: {
    selectId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      form: {
        password: ''
      },
      rules: {
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    /**
     * 关闭弹窗
     */
    close() {
      this.$emit('closeDialogResetPassword', false);
    },
    /**
     * 保存
     */
    save() {
      (this.$refs.form as FormInstance).validate().then(() => {
        resetPassword({
          userId: this.selectId,
          password: this.form.password
        }).then(res => {
          if (res.data.code === 0) {
            message.success('重置密码成功');
            this.$emit('closeDialogResetPassword', true);
          } else {
            message.error('重置密码失败：' + res.data.details);
          }
        }).catch(() => {
          message.error('重置密码失败');
        });
      });
    }
  }
}
</script>

<style scoped>

</style>