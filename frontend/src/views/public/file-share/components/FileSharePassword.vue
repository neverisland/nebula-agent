<template>
  <div class="password-container">
    <div class="password-card">
        <div class="icon-box">
             <LockOutlined style="font-size: 48px; color: #1677ff" />
        </div>
        <h2 class="title">请输入提取码</h2>
        <p class="subtitle">此分享受密码保护，请输入密码访问</p>
        
        <div class="input-area">
             <a-input-password 
                v-model:value="password" 
                placeholder="请输入密码" 
                size="large"
                @keyup.enter="handleSubmit"
             >
                <template #prefix>
                    <LockOutlined style="color: rgba(0,0,0,.25)" />
                </template>
             </a-input-password>
             
             <a-button 
                type="primary" 
                size="large" 
                class="submit-btn" 
                :loading="loading"
                :disabled="!password || loading"
                @click="handleSubmit"
             >
                提取文件
             </a-button>
        </div>
    </div>
  </div>
</template>

<script lang="ts">
import { LockOutlined } from '@ant-design/icons-vue';

/**
 * 密码输入组件
 */
import { defineComponent } from 'vue';

/**
 * 密码输入组件
 */
export default defineComponent({
  name: "FileSharePassword",
  components: {
    LockOutlined
  },
  props: {
    loading: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      password: ''
    };
  },
  methods: {
    handleSubmit() {
      if (!this.password || this.loading) return;
      // 直接触发提交事件，由父组件处理验证
      this.$emit('submit', this.password);
    }
  }
});
</script>

<style scoped>
.password-container {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 60vh;
    padding: 20px;
}

.password-card {
    background: #fff;
    border-radius: 16px;
    padding: 40px;
    width: 100%;
    max-width: 420px;
    text-align: center;
    box-shadow: 0 8px 30px rgba(0,0,0,0.05);
}

.icon-box {
    width: 80px;
    height: 80px;
    background: #e6f4ff;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 auto 24px;
}

.title {
    margin: 0 0 8px;
    color: #303133;
    font-size: 24px;
}

.subtitle {
    margin: 0 0 32px;
    color: #909399;
    font-size: 14px;
}

.input-area {
    display: flex;
    flex-direction: column;
    gap: 16px;
}

.submit-btn {
    width: 100%;
    font-size: 16px;
    letter-spacing: 1px;
}
</style>
