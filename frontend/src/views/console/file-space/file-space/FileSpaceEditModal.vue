<template>
  <a-modal
      :open="open"
      title="编辑空间"
      @ok="handleOk"
      @cancel="handleCancel"
      :confirmLoading="loading"
  >
    <a-form
        ref="formRef"
        :model="formState"
        :rules="rules"
        layout="vertical"
    >
      <a-form-item label="名称" name="name">
        <a-input v-model:value="formState.name" placeholder="请输入空间名称" />
      </a-form-item>
      <a-form-item label="备注" name="remark">
        <a-textarea v-model:value="formState.remark" placeholder="请输入备注 (可选)" :rows="4" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<script lang="ts">
import { defineComponent, ref, watch } from 'vue';
import { message } from 'ant-design-vue';
import { updateFileSpace } from "@/api/FileSpaceApi.ts";
import type { FileSpaceVo } from "@/type/filespace/FileSpaceVo.ts";

export default defineComponent({
  name: "FileSpaceEditModal",
  props: {
    open: {
      type: Boolean,
      default: false,
    },
    editData: {
      type: Object as () => FileSpaceVo | null,
      default: null,
    },
  },
  emits: ['update:open', 'success'],
  setup(props, { emit }) {
    const formRef = ref();
    const loading = ref(false);

    const formState = ref({
      id: '',
      name: '',
      remark: '',
    });

    const rules = {
      name: [
        { required: true, message: '请输入空间名称', trigger: 'blur' },
        { max: 50, message: '名称长度不能超过50个字符', trigger: 'blur' },
      ],
      remark: [
        { max: 200, message: '备注长度不能超过200个字符', trigger: 'blur' },
      ],
    };

    watch(() => props.open, (newVal) => {
      if (newVal && props.editData) {
        formState.value = {
          id: props.editData.id,
          name: props.editData.name,
          remark: props.editData.remark || '',
        };
      }
    });

    const handleOk = async () => {
      try {
        await formRef.value.validateFields();
        loading.value = true;
        
        const res = await updateFileSpace({
          id: formState.value.id,
          name: formState.value.name,
          remark: formState.value.remark,
        });

        if (res.data.code === 0) {
          message.success('更新成功');
          emit('success');
          handleCancel();
        } else {
          message.error(res.data.details || '操作失败');
        }
      } catch (error) {
        console.error('Validate Failed:', error);
      } finally {
        loading.value = false;
      }
    };

    const handleCancel = () => {
      emit('update:open', false);
      formRef.value?.resetFields();
    };

    return {
      formRef,
      formState,
      rules,
      loading,
      handleOk,
      handleCancel,
    };
  },
});
</script>
