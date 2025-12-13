<template>
  <div class="navbar-wrapper">
    <div class="header-container">
      <div class="logo-container">
        <img alt="!" src="../../../assets/img/whale-32.png">
      </div>
      <div class="content">
        <div class="social-links">
          <a-space>
            <a-button type="text" @click="toggleTheme" size="small">
              <template #icon>
                <BulbOutlined v-if="!isDarkTheme" />
                <BulbFilled v-else />
              </template>
            </a-button>

            <a-dropdown :trigger="['hover']">
              <span style="cursor: pointer; display: flex; align-items: center;">
                <a-avatar :size="32" :src="avatarUrl"/>
                <span style="margin-left: 8px;">{{ userInfo.nickname }}</span>
              </span>
              <template #overlay>
                <a-menu @click="handleMenuClick">
                  <a-menu-item key="logout">
                    <template #icon>
                      <LogoutOutlined />
                    </template>
                    退出登录
                  </a-menu-item>
                </a-menu>
              </template>
            </a-dropdown>
          </a-space>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import defaultAvatar from "@/assets/img/default-avatar.png";
import store from "@/store/cache.ts";
import {UserDto} from "@/type/user/UserDto.ts";
import {DownOutlined, LogoutOutlined, BulbOutlined, BulbFilled} from "@ant-design/icons-vue";
import {message} from "ant-design-vue";

export default {
  name: "ConsoleMenuHeader",
  components: {
    DownOutlined,
    LogoutOutlined,
    BulbOutlined,
    BulbFilled
  },
  data() {
    return {
      avatarUrl: defaultAvatar,
      userInfo: store.getters["user/getUserInfo"] as UserDto
    }
  },
  computed: {
    isDarkTheme() {
      return store.getters['theme/isDarkTheme'];
    }
  },
  methods: {
    handleMenuClick({ key }: { key: string }) {
      if (key === 'logout') {
        this.handleLogout();
      }
    },
    toggleTheme() {
      store.dispatch('theme/toggleTheme').then(() => {
        const isDark = store.getters['theme/isDarkTheme'];
        message.success(isDark ? '已切换到暗色模式' : '已切换到亮色模式');
      });
    },
    async handleLogout() {
      try {
        await store.dispatch('user/logout');
        this.$router.push('/console');
        message.success('已成功退出登录');
      } catch (error) {
        message.error('退出登录失败');
      }
    }
  }
}
</script>

<style scoped>
.navbar-wrapper {
  position: relative;
  margin: auto;
  padding: 0 32px;
  height: var(--header-height);
}

.navbar-wrapper .header-container {
  justify-content: space-between;
  display: flex;
  margin: 0 auto;
}

.navbar-wrapper .header-container .content {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  flex-grow: 1;
}

.logo-container {
  display: flex;
  align-items: center;
  height: 54px;
}

.social-links {
  display: flex;
  align-items: center;
  gap: 8px;
}
</style>
