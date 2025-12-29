<template>
  <div class="file-share-layout">
    <!-- 全局加载组件 -->
    <FileShareLoading :loading="loading" />
    
    <!-- 1. 过期状态 -->
    <FileShareExpired v-if="!loading && isExpired" />
    
    <!-- 2. 密码锁定状态 -->
    <FileSharePassword 
        v-else-if="!loading && isLocked" 
        :loading="passwordVerifying"
        @submit="handlePasswordSubmit" 
    />
    
    <!-- 3. 正常内容 -->
    <template v-else-if="!loading && shareInfo">
        <FileShareHeader :info="shareInfo" @download-all="handleDownloadAll" />
        <FileShareGrid :file-list="fileList" />
    </template>
    
    <!-- 4. 空/错误状态 -->
    <a-empty v-else-if="!loading" description="未找到分享信息" style="padding-top: 100px" />
  </div>
</template>

<script lang="ts">
import { message } from 'ant-design-vue';
import { getPublicShareInfo, verifySharePassword, incrementVisitCount, getPublicFileList, downloadAllFiles } from "@/api/FileSharePublicApi";
import type { FileSharePublicVo } from "@/type/file-share/vo/FileSharePublicVo";
import type { FileLibraryPageVo } from "@/type/filelibrary/FileLibraryPageVo";
import FileShareHeader from "./components/FileShareHeader.vue";
import FileShareGrid from "./components/FileShareGrid.vue";
import FileSharePassword from "./components/FileSharePassword.vue";
import FileShareExpired from "./components/FileShareExpired.vue";
import FileShareLoading from "./components/FileShareLoading.vue";

/**
 * 文件分享公开页布局
 * 
 * @author 
 */
export default {
  name: "FileShareLayout",
  components: {
    FileShareHeader,
    FileShareGrid,
    FileSharePassword,
    FileShareExpired,
    FileShareLoading
  },
  data() {
    return {
      loading: true,
      passwordVerifying: false, // 密码验证中
      shareInfo: undefined as FileSharePublicVo | undefined,
      fileList: [] as FileLibraryPageVo[],
      isLocked: false, // 是否被密码锁定
      isExpired: false, // 是否过期
      currentPassword: undefined as string | undefined // 当前使用的密码
    };
  },
  computed: {
    shareId(): string {
      return (this.$route.params.id as string) || '';
    },
    urlPassword(): string | undefined {
      return this.$route.query.pwd as string | undefined;
    },
    hasViewed(): boolean {
      return this.$route.query.viewed === '1';
    }
  },
  mounted() {
    this.init();
  },
  methods: {
    // 更新 URL 参数
    updateUrlParams(params: Record<string, string | undefined>) {
      const query = { ...this.$route.query };
      Object.keys(params).forEach(key => {
        if (params[key]) {
          query[key] = params[key];
        } else {
          delete query[key];
        }
      });
      this.$router.replace({ query });
    },

    // 加载文件列表
    async loadFileList(password?: string) {
      if (!this.shareId) return;
      try {
        const { data: res } = await getPublicFileList({
          shareId: this.shareId,
          password: password || ''
        });
        if (res.code === 0) {
          this.fileList = res.data || [];
        } else {
          message.error(res.msg || '获取文件列表失败');
        }
      } catch (e) {
        console.error(e);
        message.error('获取文件列表失败');
      }
    },

    // 初始化
    async init(forcePassword?: string) {
      if (!this.shareId) return;
      this.loading = true;
      try {
        // 优先使用传入的密码，否则从 URL 获取密码
        const password = forcePassword !== undefined ? forcePassword : (this.urlPassword || this.currentPassword);
        this.currentPassword = password;
        
        const { data: res } = await getPublicShareInfo(this.shareId, password);
        if (res.code === 0) {
          this.shareInfo = res.data;
          
          // 1. 处理过期状态
          this.isExpired = !!res.data.isExpired;
          
          // 2. 处理密码锁定
          // 如果开启了密码，且没有密码参数，则需要输入密码
          if (res.data.enablePassword && !password) {
            this.isLocked = true;
          } else {
            this.isLocked = false;
            
            // 3. 如果未过期且已解锁，加载文件列表
            if (!this.isExpired) {
              await this.loadFileList(password);
              
              // 4. 增加访问次数（如果还没有 viewed=1 参数）
              if (!this.hasViewed) {
                try {
                  await incrementVisitCount(this.shareId);
                  // 成功后更新 URL 添加 viewed=1
                  this.updateUrlParams({ viewed: '1' });
                } catch (e) {
                  console.error('增加访问次数失败:', e);
                }
              }
            }
          }
        } else {
          message.error(res.msg || '获取分享信息失败');
        }
      } catch (e) {
        console.error(e);
        message.error('网络异常，请稍后重试');
      } finally {
        this.loading = false;
      }
    },

    // 提交密码
    async handlePasswordSubmit(passwordInput: string) {
      if (!this.shareId || !passwordInput) return;
      
      this.passwordVerifying = true;
      try {
        // 验证密码
        const { data: res } = await verifySharePassword({
          shareId: this.shareId,
          password: passwordInput
        });
        
        if (res.code === 0) {
          // 密码验证成功，更新 URL 添加 pwd 参数
          this.currentPassword = passwordInput;
          this.updateUrlParams({ pwd: passwordInput });
          
          // 直接使用验证成功的密码初始化，不等待 URL 更新
          this.isLocked = false;
          await this.init(passwordInput);
        } else {
          // 密码错误，清除 URL 中的 pwd 参数
          if (this.urlPassword) {
            this.updateUrlParams({ pwd: undefined });
            this.currentPassword = undefined;
          }
          message.error(res.msg || '密码错误');
        }
      } catch (e: any) {
        console.error(e);
        const errorMsg = e?.response?.data?.msg || e?.message || '密码验证失败';
        message.error(errorMsg);
        
        // 密码验证失败，清除 URL 中的 pwd 参数
        if (this.urlPassword) {
          this.updateUrlParams({ pwd: undefined });
          this.currentPassword = undefined;
        }
      } finally {
        this.passwordVerifying = false;
      }
    },

    // 下载全部文件
    handleDownloadAll() {
      if (!this.shareId) return;
      downloadAllFiles(this.shareId, this.currentPassword);
    }
  }
};
</script>

<style scoped>
.file-share-layout {
  min-height: 100vh;
  background-color: #f5f7fa;
}
</style>
