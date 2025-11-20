<template>
  <div class="common-layout">
    <div class="center">
      <!--     登录表单     -->
      <h1 class="title">Login</h1>
      <a-form
          ref="form"
          :model="formData"
          :label-col="{ span: 24 }"
          :wrapper-col="{ span: 24 }"
          size="large"
      >
        <a-form-item label="账号" name="username">
          <a-input v-model:value="formData.username" @input="changeForm">
            <template #prefix>
              <UserOutlined :style="{ fontSize: '16px' }"/>
            </template>
          </a-input>
        </a-form-item>
        <a-form-item label="密码" name="password">
          <a-input-password v-model:value="formData.password" @input="changeForm" @keydown.enter="loginVerify">
            <template #prefix>
              <LockOutlined :style="{ fontSize: '16px' }"/>
            </template>
          </a-input-password>
        </a-form-item>
      </a-form>
      <a-button
          :disabled="disabledLogin"
          class="login"
          type="primary"
          size="large"
          @click="loginVerify"
      >
        Login
      </a-button>
    </div>
  </div>

  <a-modal
      v-model:open="sliderVerifyDialog"
      :footer="null"
      :width="356"
      :centered="true"
      :destroyOnClose="true"
      :closable="false"
  >
    <SliderVerify
        ref="sliderVerify"
        business="password_login"
        @again="onAgain"
        @fail="onFail"
        @success="onSuccess"
        style="display: block; width: 100%;"
    />
  </a-modal>
</template>

<script lang="ts">
import {LockOutlined, UserOutlined} from '@ant-design/icons-vue';
import SliderVerify from "@/components/slider-verify/SliderVerify.vue";
import {message} from 'ant-design-vue';
import {authenticationPasswordLogin} from "@/api/AuthenticationPasswordApi.ts";
import store from "@/store/cache.ts";

export default {
  name: "ConsoleLogin",
  components: {SliderVerify, UserOutlined, LockOutlined},
  data() {
    return {
      // 表单数据
      formData: {
        username: '', // 账号
        password: '', // 密码
        userSlideDistance: 0, // 用户滑动的距离
        cacheSlidingVerificationCode: '' // 缓存中的滑动验证码code
      },
      sliderVerifyDialog: false, // 滑动验证码弹窗
      disabledLogin: true, // 是否禁用登录按钮
    }
  },
  methods: {
    /**
     * 校验数据是否填写
     * 如果填写了账号密码，可以进行登录
     */
    changeForm() {
      this.disabledLogin = !this.formData.username || !this.formData.password
    },
    /**
     * 校验是否填写登录信息完成
     */
    loginVerify() {
      if (!this.disabledLogin) {
        this.sliderVerifyDialog = true;
      }
    },
    /**
     * 登录
     */
    login() {
      authenticationPasswordLogin(this.formData).then(res => {
        if (res.data.code === 0) {
          // 设置用户token
          store.commit('user/setToken', res.data.data)
          // 获取当前登录用户信息
          this.getCurrentUserInfo();
        } else {
          message.error(res.data.details ? res.data.details : "用户名或密码错误");
        }
      }).catch(err => {
        console.log("登录失败", err)
        message.error("登录失败");
      }).finally(() => {
        this.sliderVerifyDialog = false;
      })
    },
    /**
     * 获取当前登录用户信息
     */
    getCurrentUserInfo() {
      store.dispatch('user/loginGetUserInfo').then(res => {
        message.success("登录成功");
        this.$router.push({name: 'chat'})
      }).catch(error => {
        console.log("登录失败", error)
        message.error("登录失败");
      })
    },
    /*
     * 滑动验证成功
     * 成功后调取 获取验证码接口
     */
    onSuccess(captcha: { nonceStr: string, value: string }) {
      this.formData.cacheSlidingVerificationCode = captcha.nonceStr;
      this.formData.userSlideDistance = captcha.value;
      this.login();
    },
    /**
     * 滑动验证失败
     * @param msg 提示消息
     */
    onFail(msg: string) {
      this.sliderVerifyDialog = false;
      message.error({
        content: msg || '验证失败，请控制拼图对齐缺口',
        duration: 2,
      });
    },
    /**
     * 滑动验证异常
     */
    onAgain() {
      this.sliderVerifyDialog = false;
      message.warning({
        content: '滑动操作异常，请重试',
        duration: 2,
      });
    },
  },
}
</script>

<style scoped>
.common-layout {
  min-height: 780px;
  background-image: url('@/assets/img/login-bj.jpg');
  background-size: 100% 100%;
}

.title {
  font-size: 24px;
  color: #fff;
}

.login {
  width: 100%;
}

:deep(.ant-form-item-label > label) {
  color: #fff;
}

:deep(.ant-input) {
  letter-spacing: 1px;
}

.center {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 400px;
  padding: 40px;
  background: rgba(0, 0, 0, 0.2);
  box-sizing: border-box;
  box-shadow: 0 15px 25px rgba(0, 0, 0, .6);
  border-radius: 10px;
}
</style>
