<template>
  <a-config-provider :theme="antTheme">
    <router-view/>
  </a-config-provider>
</template>

<script>
import { ConfigProvider as AConfigProvider, theme } from 'ant-design-vue';
import store from '@/store/cache.ts';
import { computed } from 'vue';

export default {
  name: 'App',
  components: {
    'a-config-provider': AConfigProvider
  },
  setup() {
    // 使用computed来响应式地获取主题
    const antTheme = computed(() => {
      const currentTheme = store.getters['theme/currentTheme'];
      
      // 同步到 body 类名，方便全局 CSS 及非 Ant 组件适配主题
      if (currentTheme === 'dark') {
          document.body.classList.add('dark');
      } else {
          document.body.classList.remove('dark');
      }

      return {
        algorithm: currentTheme === 'dark' ? theme.darkAlgorithm : theme.defaultAlgorithm,
        token: {
          colorBgLayout: currentTheme === 'dark' ? '#000000' : '#ffffff',
        },
        cssVar: true
      };
    });

    return {
      antTheme
    };
  }
}
</script>

<style>
html, body {
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  height: 100vh;
}

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  height: 100%;
}

.title {
  font-size: 20px;
  font-weight: bold;
}

.form-inline {
  display: flex;
}

.dialog-footer {
  text-align: right;
  margin-top: 20px;
}
</style>
