<script lang="ts">
import { message } from 'ant-design-vue';
import { getShareDetail } from '@/api/FileShareApi';
import type { FileShareVo } from '@/type/file-share/vo/FileShareVo';
import { ShareTypeEnum } from '@/enums/ShareTypeEnum';

import { CopyOutlined } from '@ant-design/icons-vue';

export default {
    name: "FileShareDetail",
    components: { CopyOutlined },
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
         * 复制文本
         */
        handleCopy(text: string) {
            if (!text) return;
            navigator.clipboard.writeText(text).then(() => {
                message.success('复制成功');
            }).catch(() => {
                message.error('复制失败');
            });
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
        <a-descriptions :column="2" style="padding: 20px 40px;">
            <a-descriptions-item label="分享名称">{{ detail.name }}</a-descriptions-item>
            <a-descriptions-item label="分享类型">
                <a-tag v-if="detail.shareType === ShareTypeEnum.FILE" color="blue">个人文件</a-tag>
                <a-tag v-else-if="detail.shareType === ShareTypeEnum.SPACE" color="purple">个人空间</a-tag>
            </a-descriptions-item>
            
            <a-descriptions-item label="分享内容">
                 <span v-if="detail.shareType === ShareTypeEnum.SPACE && detail.fileSpaceVo">
                    {{ detail.fileSpaceVo.name }}
                 </span>
                 <span v-else>
                    文件 {{ detail.fileCount }} 个
                 </span>
            </a-descriptions-item>

            <a-descriptions-item label="分享状态">
                    <a-tag v-if="detail.isExpired" color="red">已过期</a-tag>
                    <a-tag v-else color="green">有效</a-tag>
            </a-descriptions-item>

            <a-descriptions-item label="分享链接">
                 <a-button type="link" size="small" @click="handleCopy(detail.shareUrl)" style="padding-left: 0">
                      <template #icon><CopyOutlined /></template>
                      复制链接
                 </a-button>
            </a-descriptions-item>

            <a-descriptions-item label="密码保护">
                <a-space v-if="detail.enablePassword">
                     <span style="font-family: monospace;">{{ detail.password }}</span>
                     <a-button type="link" size="small" @click="handleCopy(detail.password)">
                         <template #icon><CopyOutlined /></template>
                    </a-button>
                </a-space>
                <span v-else>无</span>
            </a-descriptions-item>

            <a-descriptions-item label="访问次数">{{ detail.visitCount }}</a-descriptions-item>
            <a-descriptions-item label="下载次数">{{ detail.downloadCount }}</a-descriptions-item>
            
            <a-descriptions-item label="创建时间">{{ detail.createTime }}</a-descriptions-item>
            <a-descriptions-item label="过期时间">
                {{ detail.enableExpire ? detail.expireTime : '永久有效' }}
            </a-descriptions-item>
        </a-descriptions>
        
        <div class="dialog-footer">
            <a-button @click="handleCancel">关闭</a-button>
        </div>
    </a-spin>
</template>

<style scoped>
.dialog-footer {
  text-align: right;
  margin-top: 20px;
}
</style>
