<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { getPublicShareInfo } from "@/api/FileSharePublicApi";
import type { FileSharePublicVo } from "@/type/file-share/vo/FileSharePublicVo";
import FileShareHeader from "./components/FileShareHeader.vue";
import FileShareGrid from "./components/FileShareGrid.vue";
import FileSharePassword from "./components/FileSharePassword.vue";
import FileShareExpired from "./components/FileShareExpired.vue";
import { getMockFileList, mockState, type MockFileItem } from "./mock";
import { message } from 'ant-design-vue';

/**
 * 文件分享公开页布局
 * 
 * @author 
 */

const route = useRoute();
const shareId = route.params.id as string;

// 状态管理
const loading = ref(true);
const shareInfo = ref<FileSharePublicVo>();
const fileList = ref<MockFileItem[]>([]);
const isLocked = ref(false); // 是否被密码锁定
const isExpired = ref(false); // 是否过期

// 初始化
const init = async (password?: string) => {
    if (!shareId) return;
    loading.value = true;
    try {
        const { data: res } = await getPublicShareInfo(shareId, password);
        if (res.code === 0) {
           shareInfo.value = res.data;
           
           // --- Mock 逻辑 ---
           // 1. 处理过期状态
           if (mockState.forceExpired) {
               isExpired.value = true;
           } else {
               isExpired.value = !!res.data.isExpired;
           }

           // 2. 处理密码锁定
           if (mockState.forceLocked && !password) {
               isLocked.value = true;
           } else if (res.data.enablePassword && !password && !res.data.fileCount) { 
               isLocked.value = true;
           } else {
                isLocked.value = false;
           }
           
           // 3. 填充文件列表
           if (!isLocked.value && !isExpired.value) {
               fileList.value = getMockFileList();
               if (shareInfo.value) {
                   shareInfo.value.fileCount = fileList.value.length;
               }
           }
        } else {
            message.error(res.msg || '获取分享信息失败');
        }
    } catch (e) {
        console.error(e);
        message.error('网络异常，请稍后重试');
    } finally {
        loading.value = false;
    }
};

onMounted(() => {
    init();
});

// 提交密码
const handlePasswordSubmit = (passwordInput: string) => {
    if (mockState.correctPassword && passwordInput !== mockState.correctPassword) {
        message.error('提取码错误');
        return;
    }
    isLocked.value = false;
    fileList.value = getMockFileList();
    if (shareInfo.value) {
         shareInfo.value.fileCount = fileList.value.length;
    }
};

</script>

<template>
  <div class="file-share-layout">
    <a-spin :spinning="loading" tip="加载中..." wrapperClassName="full-loading">
        <!-- 1. 过期状态 -->
        <FileShareExpired v-if="!loading && isExpired" />
        
        <!-- 2. 密码锁定状态 -->
        <FileSharePassword 
            v-else-if="!loading && isLocked" 
            @submit="handlePasswordSubmit" 
        />
        
        <!-- 3. 正常内容 -->
        <template v-else-if="!loading && shareInfo">
            <FileShareHeader :info="shareInfo" />
            <FileShareGrid :file-list="fileList" />
        </template>
        
        <!-- 4. 空/错误状态 -->
        <a-empty v-else-if="!loading" description="未找到分享信息" style="padding-top: 100px" />
    </a-spin>
  </div>
</template>

<style scoped>
.file-share-layout {
  min-height: 100vh;
  background-color: #f5f7fa;
}
:deep(.full-loading) {
    min-height: 100vh;
}
</style>
