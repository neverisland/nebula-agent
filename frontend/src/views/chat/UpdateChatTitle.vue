<template>
  <a-form
      ref="form"
      :model="form"
      :rules="rules"
      :label-col="{ span: 6 }"
      :wrapper-col="{ span: 15 }"
  >
    <a-form-item label="标题" name="title">
      <a-input v-model:value="form.title" autocomplete="off"/>
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
import {updateChatTitle} from "@/api/ChatApi.ts";

export default {
  name: "UpdateChatTitle",
  props: {
    selectId: {
      type: String,
      required: true
    },
    selectTitle: {
      type: String,
      required: false,
      default: ''
    }
  },
  mounted() {
    this.form.title = this.selectTitle;
  },
  emits: ['closeDialogUpdateChatTitle'],
  data() {
    return {
      form: {
        title: this.selectTitle
      },
      rules: {
        title: [
          {required: true, message: '请输入标题', trigger: 'blur'},
          {min: 2, max: 20, message: '标题长度在 2 到 20 个字符', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    /**
     * 关闭弹窗
     */
    close() {
      this.$emit('closeDialogUpdateChatTitle', false);
    },
    /**
     * 保存
     */
    save() {
      (this.$refs.form as FormInstance).validate().then(() => {
        updateChatTitle({
          chatId: this.selectId,
          title: this.form.title
        }).then(res => {
          if (res.data.code === 0) {
            message.success('修改成功');
            this.$emit('closeDialogUpdateChatTitle', true, this.form.title);
          } else {
            message.error('修改失败：' + res.data.details);
          }
        }).catch((e) => {
          console.error(e)
          message.error('修改失败');
        });
      });
    }
  }
}
</script>

<style scoped>

</style>