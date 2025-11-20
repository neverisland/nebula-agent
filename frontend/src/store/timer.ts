import {defineStore} from "pinia";
import {verifyWhetherAuthenticated} from "@/api/AuthorizationApi.ts";
import router from "@/router";
import {useRoute} from "vue-router";

/**
 * 定时器
 */
export const useTimerStore = defineStore('timer', {
    state: () => ({
        timerId: null as number | null
    }),
    actions: {
        // 开启定时器
        startTimer() {
            if (!!this.timerId) return
            this.timerId = setInterval(() => {
                whetherAuthenticateListener()
            }, 5000)
        },
        // 关闭定时器
        stopTimer() {
            if(!!this.timerId) {
                clearInterval(this.timerId)
            }
            this.timerId = null
        }
    }
})

/**
 * 是否认证监听
 */
export const whetherAuthenticateListener = () => {
    // todo 如果不需要的话可以关闭它
    // return;
    verifyWhetherAuthenticated().then(res => {
        if (res.data.code !== 0 || !res.data.data) {
            // 认证失败,跳转至登录页面
            router.push({name: 'login'})
        }
    }).catch((err: any) => {
        console.log("认证监听失败", err)
    })
}