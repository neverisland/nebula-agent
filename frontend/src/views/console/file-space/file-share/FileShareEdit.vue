<script lang="ts">
import { message } from 'ant-design-vue';
import type { FormInstance } from 'ant-design-vue';
import { updateShare, getShareDetail } from '@/api/FileShareApi';
import type { FileShareUpdatePo } from '@/type/file-share/po/FileShareUpdatePo';

export default {
    name: "FileShareEdit",
    props: {
        selectId: {
            type: String,
            required: true
        }
    },
    emits: ['closeDialogEdit'],
    data() {
        return {
            confirmLoading: false,
            loading: false,
            formState: {
                id: '',
                name: '',
                enablePassword: false,
                password: '',
                enableExpire: false,
                expireTime: '',
                fileIds: [],
                spaceId: ''
            } as FileShareUpdatePo,
            rules: {
                name: [{ required: true, message: '请输入分享名称', trigger: 'blur' }],
                password: [{ required: true, message: '请输入访问密码', trigger: 'blur' }],
                expireTime: [{ required: true, message: '请选择过期时间', trigger: 'change' }]
            }
        };
    },
    watch: {
        selectId: {
            handler(newVal) {
                if (newVal) {
                    this.loadDetail(newVal);
                }
            },
            immediate: true
        }
    },
    methods: {
        /**
         * 加载详情
         */
        async loadDetail(id: string) {
            if (!id) return;
            this.loading = true;
            try {
                const res = await getShareDetail(this.selectId);
                if (res.data.code === 0) {
                    const data = res.data.data;
                    this.formState = {
                        id: data.id,
                        name: data.name,
                        enablePassword: data.enablePassword,
                        password: '', // 密码不回显
                        enableExpire: data.enableExpire,
                        expireTime: data.expireTime,
                        fileIds: [], // 暂不处理文件回显
                        spaceId: ''
                    };
                }
            } catch (e) {
                console.error(e);
                message.error('加载详情失败');
            } finally {
                this.loading = false;
            }
        },

        /**
         * 确定按钮
         */
        async handleOk() {
            try {
                await (this.$refs.formRef as FormInstance)?.validate();
                this.confirmLoading = true;
                
                const res = await updateShare(this.formState);
                if (res.data.code === 0) {
                    message.success('修改成功');
                    this.$emit('closeDialogEdit', true);
                } else {
                    message.error(res.data.msg || '修改失败');
                }
            } catch (error) {
                console.error(error);
            } finally {
                this.confirmLoading = false;
            }
        },

        /**
         * 取消按钮
         */
        handleCancel() {
            this.$emit('closeDialogEdit', false);
        }
    }
}
</script>

<template>
    <div class="user-edit-container">
        <a-spin :spinning="loading">
            <a-form
            ref="formRef"
            :model="formState"
            :rules="rules"
            layout="vertical"
            >
            <a-form-item label="分享名称" name="name">
                <a-input v-model:value="formState.name" placeholder="请输入分享名称" />
            </a-form-item>
            
            <a-form-item label="访问密码">
                <a-switch v-model:checked="formState.enablePassword" />
                <div v-if="formState.enablePassword" style="margin-top: 8px">
                    <a-form-item name="password" :rules="formState.enablePassword ? rules.password : []" no-style>
                        <a-input-password v-model:value="formState.password" placeholder="重置访问密码 (留空则不修改)" />
                    </a-form-item>
                </div>
            </a-form-item>

            <a-form-item label="过期时间">
                <a-switch v-model:checked="formState.enableExpire" />
                <div v-if="formState.enableExpire" style="margin-top: 8px">
                    <a-form-item name="expireTime" :rules="formState.enableExpire ? rules.expireTime : []" no-style>
                        <a-date-picker 
                            v-model:value="formState.expireTime" 
                            show-time 
                            placeholder="选择过期时间" 
                            value-format="YYYY-MM-DD HH:mm:ss"
                            style="width: 100%"
                        />
                    </a-form-item>
                </div>
            </a-form-item>

            <a-form-item>
                <a-space style="display: flex; justify-content: flex-end">
                    <a-button @click="handleCancel">取消</a-button>
                    <a-button type="primary" :loading="confirmLoading" @click="handleOk">确定</a-button>
                </a-space>
            </a-form-item>
            </a-form>
        </a-spin>
    </div>
</template>
