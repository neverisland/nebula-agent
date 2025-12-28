<script lang="ts">
import { message } from 'ant-design-vue';
import { getShareDetail } from '@/api/FileShareApi';
import type { FileShareVo } from '@/type/file-share/vo/FileShareVo';
import { ShareTypeEnum } from '@/enums/ShareTypeEnum';

export default {
    name: "FileShareDetail",
    props: {
        selectId: {
            type: String,
            required: true
        }
    },
    emits: ['closeDialogDetail'],
    data() {
        return {
            loading: false,
            detail: {} as FileShareVo,
            ShareTypeEnum // Expose to template
        }
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
                    this.detail = res.data.data;
                }
            } catch (e) {
                console.error(e);
                message.error('加载详情失败');
            } finally {
                this.loading = false;
            }
        },

        /**
         * 关闭按钮
         */
        handleCancel() {
            this.$emit('closeDialogDetail');
        }
    }
}
</script>

<template>
    <a-spin :spinning="loading">
        <a-descriptions bordered column={1}>
            <a-descriptions-item label="分享ID">{{ detail.id }}</a-descriptions-item>
            <a-descriptions-item label="分享名称">{{ detail.name }}</a-descriptions-item>
            <a-descriptions-item label="分享类型">
                <a-tag v-if="detail.shareType === ShareTypeEnum.FILE" color="blue">个人文件</a-tag>
                <a-tag v-else-if="detail.shareType === ShareTypeEnum.SPACE" color="purple">个人空间</a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="分享链接">
                <a-typography-text copyable>{{ detail.shareUrl }}</a-typography-text>
            </a-descriptions-item>
            <a-descriptions-item label="密码保护">
                {{ detail.enablePassword ? '开启' : '关闭' }}
            </a-descriptions-item>
             <a-descriptions-item label="过期时间">
                {{ detail.enableExpire ? detail.expireTime : '永久有效' }}
            </a-descriptions-item>
            <a-descriptions-item label="状态">
                 <a-tag v-if="detail.isExpired" color="red">已过期</a-tag>
                 <a-tag v-else color="green">有效</a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="访问次数">{{ detail.visitCount }}</a-descriptions-item>
            <a-descriptions-item label="下载次数">{{ detail.downloadCount }}</a-descriptions-item>
            <a-descriptions-item label="创建时间">{{ detail.createTime }}</a-descriptions-item>
        </a-descriptions>
        
        <div style="margin-top: 20px; text-align: right">
            <a-button type="primary" @click="handleCancel">关闭</a-button>
        </div>
    </a-spin>
</template>
