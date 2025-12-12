<template>
  <a-layout-sider width="200">
    <a-menu
        v-model:selectedKeys="selectedKeys"
        mode="inline"
        :style="{ height: '100%', borderRight: 1 }"
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
  </a-layout-sider>
</template>

<script lang="ts">
import { HomeOutlined, SettingOutlined, FolderOpenOutlined } from '@ant-design/icons-vue';
import { ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';

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

    watch(() => route.path, (newPath) => {
      selectedKeys.value = [newPath];
    });

    const handleMenuClick = ({ key }) => {
      router.push(key);
    };

    return {
      selectedKeys,
      handleMenuClick,
    };
  }
}
</script>

<style scoped>
::-webkit-scrollbar {
  display: none;
}
</style>

