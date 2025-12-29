<template>
    <div class="user-add-container">
        <a-form
        ref="formRef"
        :model="formState"
        :rules="rules"
        layout="vertical"
        >
        <!-- 显示当前分享的对象名称 -->
        <div v-if="sourceName" style="margin-bottom: 24px; padding: 12px; background: rgba(0,0,0,0.02); border-radius: 6px;">
            <div style="font-size: 14px; color: #666; margin-bottom: 4px;">当前分享</div>
            <div style="font-size: 16px; font-weight: 500; color: #333; display: flex; align-items: center;">
                <span v-if="formState.shareType === ShareTypeEnum.FILE">📄</span>
                <span v-else>📂</span>
                <span style="margin-left: 8px;">{{ sourceName }}</span>
            </div>
        </div>

        <a-form-item label="分享类型" v-show="false">
            <a-radio-group v-model:value="formState.shareType" disabled>
            <a-radio :value="ShareTypeEnum.FILE">个人文件</a-radio>
            <a-radio :value="ShareTypeEnum.SPACE">个人空间</a-radio>
            </a-radio-group>
        </a-form-item>

        <a-form-item label="分享名称" name="name">
            <a-input v-model:value="formState.name" placeholder="请输入分享名称" />
        </a-form-item>
        
        <a-form-item label="访问密码">
            <a-switch v-model:checked="formState.enablePassword" />
            <div v-if="formState.enablePassword" style="margin-top: 8px">
                <a-form-item name="password" :rules="formState.enablePassword ? rules.password : []" no-style>
                    <a-input-password v-model:value="formState.password" placeholder="设置访问密码" />
                </a-form-item>
            </div>
        </a-form-item>

        <a-form-item label="过期时间">
            <a-switch v-model:checked="formState.enableExpire" />
            <div v-if="formState.enableExpire" style="margin-top: 8px">
                <a-form-item name="expireTime" :rules="formState.enableExpire ? rules.expireTime : []" no-style>
                    <a-date-picker 
                        v-model:value="formState.expireTime" 
                        placeholder="选择过期时间" 
                        value-format="YYYY-MM-DD"
                        style="width: 100%"
                    />
                </a-form-item>
            </div>
        </a-form-item>

        <a-form-item style="margin-top: 10px;">
            <a-space style="display: flex; justify-content: flex-end">
                <a-button @click="handleCancel" size="small">取消</a-button>
                <a-button type="primary" :loading="confirmLoading" @click="handleOk" size="small">确定</a-button>
            </a-space>
        </a-form-item>
        </a-form>
    </div>
</template>

<script lang="ts">
import { message } from 'ant-design-vue';
import type { FormInstance } from 'ant-design-vue';
import { ShareTypeEnum } from '@/enums/ShareTypeEnum';
import { createShare } from '@/api/FileShareApi';
import type { FileShareCreatePo } from '@/type/file-share/po/FileShareCreatePo';

export default {
    name: "FileShareAdd",
    emits: ['closeDialogInsert'],
    data() {
        return {
            confirmLoading: false,
            sourceName: '', // 显示的源名称（文件或空间名）
            formState: {
                shareType: ShareTypeEnum.FILE,
                name: '',
                enablePassword: false,
                password: '',
                enableExpire: false,
                expireTime: '',
                fileIds: [],
                spaceId: ''
            } as FileShareCreatePo,
            rules: {
                name: [{ required: true, message: '请输入分享名称', trigger: 'blur' }],
                password: [{ required: true, message: '请输入访问密码', trigger: 'blur' }],
                expireTime: [{ required: true, message: '请选择过期时间', trigger: 'change' }]
            },
            ShareTypeEnum // Expose enum to template
        }
    },
    methods: {
        /**
         * 初始化方法
         */
        init(data: Partial<FileShareCreatePo> & { sourceName?: string }) {
            this.sourceName = data.sourceName || '';
            this.formState = {
                shareType: ShareTypeEnum.FILE,
                name: '',
                enablePassword: false,
                password: '',
                enableExpire: false,
                expireTime: '',
                fileIds: [],
                spaceId: '',
                ...data
            };
            // 如果没有传入 name，但有 sourceName，则用 sourceName 作为默认名称
            if (!this.formState.name && this.sourceName) {
                this.formState.name = this.sourceName;
            }
            this.confirmLoading = false;
        },

        /**
         * 确定按钮
         */
        async handleOk() {
            try {
                await (this.$refs.formRef as FormInstance)?.validate();
                this.confirmLoading = true;
                
                const res = await createShare(this.formState);
                if (res.data.code === 0) {
                    message.success('创建成功');
                    this.$emit('closeDialogInsert', true);
                } else {
                    message.error(res.data.msg || '创建失败');
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
            this.$emit('closeDialogInsert', false);
        }
    }
}
</script>
