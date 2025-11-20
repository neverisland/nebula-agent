<template>
  <div class="navbar-wrapper">
    <div class="header-container">
      <div class="logo-container">
        <img alt="!" src="../../../assets/img/whale-32.png">
      </div>
      <div class="content">
        <div class="social-links">
          <a-space>
            <a-avatar :size="32" :src="avatarUrl"/>
            <a-typography-text>{{ userInfo.nickname }}</a-typography-text>
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

export default {
  name: "ConsoleMenuHeader",
  data() {
    return {
      avatarUrl: defaultAvatar,
      userInfo: store.getters["user/getUserInfo"] as UserDto
    }
  },
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

</style>
