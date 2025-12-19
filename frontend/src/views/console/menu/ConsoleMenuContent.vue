<template>
  <a-menu
      v-model:selectedKeys="selectedKeys"
      v-model:openKeys="openKeys"
      mode="inline"
      :theme="menuTheme"
      :style="{ height: '100%', borderRight: 0 }"
      @click="handleMenuClick"
  >
    <a-menu-item key="/console/page/home">
      <template #icon>
        <HomeOutlined />
      </template>
      <span>主页</span>
    </a-menu-item>
    <a-sub-menu key="1">
      <template #icon>
        <SettingOutlined />
      </template>
      <template #title>系统管理</template>
      <a-menu-item key="/console/page/role">角色管理</a-menu-item>
      <a-menu-item key="/console/page/user">用户管理</a-menu-item>
    </a-sub-menu>
    <a-sub-menu key="2">
      <template #icon>
        <FolderOpenOutlined />
      </template>
      <template #title>文件空间</template>
      <a-menu-item key="/console/page/file-library">我的文件</a-menu-item>
      <a-menu-item key="/console/page/personal-space">个人空间</a-menu-item>
      <a-menu-item key="/console/page/my-share">我的分享</a-menu-item>
    </a-sub-menu>
  </a-menu>
</template>

<script lang="ts">
import { HomeOutlined, SettingOutlined, FolderOpenOutlined } from '@ant-design/icons-vue';
import { ref, watch, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import store from "@/store/cache.ts";

/**
 * 菜单内容组件
 */
export default {
  name: "ConsoleMenuContent",
  components: { HomeOutlined, SettingOutlined, FolderOpenOutlined },
  setup() {
    const route = useRoute();
    const router = useRouter();
    const selectedKeys = ref([route.path]);

    // 定义路由与子菜单的映射关系
    const subMenuMap: Record<string, string> = {
      '/console/page/role': '1',
      '/console/page/user': '1',
      '/console/page/file-library': '2',
      '/console/page/personal-space': '2',
      '/console/page/my-share': '2',
    };

    // 根据路径获取应该展开的子菜单key
    const getOpenKeyFromPath = (path: string): string[] => {
      const subMenuKey = subMenuMap[path];
      return subMenuKey ? [subMenuKey] : [];
    };

    // 初始化时根据当前路由设置展开的菜单
    const openKeys = ref(getOpenKeyFromPath(route.path));

    watch(() => route.path, (newPath) => {
      selectedKeys.value = [newPath];
      // 当路由变化时，确保对应的父菜单展开
      const newOpenKeys = getOpenKeyFromPath(newPath);
      if (newOpenKeys.length > 0 && !openKeys.value.includes(newOpenKeys[0])) {
        openKeys.value = [...openKeys.value, ...newOpenKeys];
      }
    });

    const handleMenuClick = ({ key }: { key: string }) => {
      router.push(key);
    };

    const isDarkTheme = computed(() => store.getters['theme/isDarkTheme']);
    const menuTheme = computed(() => isDarkTheme.value ? 'dark' : 'light');

    return {
      selectedKeys,
      openKeys,
      handleMenuClick,
      isDarkTheme,
      menuTheme
    };
  }
}
</script>

<style scoped>
::-webkit-scrollbar {
  display: none;
}
</style>

