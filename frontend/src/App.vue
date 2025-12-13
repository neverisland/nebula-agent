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
      return {
        algorithm: currentTheme === 'dark' ? theme.darkAlgorithm : theme.defaultAlgorithm
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
