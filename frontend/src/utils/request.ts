import axios, {AxiosResponse} from "axios";
import {ResultVo} from "../type/ResultVo";
import Cookies from "js-cookie";
import {message} from "ant-design-vue";
import store from "@/store/cache.ts";


const request = axios.create({
    // `baseURL` 将自动加在 `url` 前面，除非 `url` 是一个绝对 URL。
    // 它可以通过设置一个 `baseURL` 便于为 axios 实例的方法传递相对 URL
    baseURL: '/api',
    // `timeout` 指定请求超时的毫秒数。
    // 如果请求时间超过 `timeout` 的值，则请求会被中断
    timeout: 3000, // 默认值是 `0` (永不超时)
})

/**
 * @desc: 请求发送前拦截
 * @param { Object } config 配置参数
 */
request.interceptors.request.use(function (config) {
    // 设置token
    config.headers['authentication'] = store.state.user.token ?? ''
    return config
}, function (error) {
    console.error('请求错误:', error)
    return Promise.reject(error);
})

/**
 * @desc: 服务端响应后拦截
 * @param { Object } response 返回的数据
 */
request.interceptors.response.use(function (response: AxiosResponse<ResultVo>) {
    // 登录失效
    if (response.status === 200 && response.data.code === 9) {
        store.dispatch('user/clearLoginState').then(r => console.log('logout'))
        window.location.href = '/console/login'
        return Promise.reject(response)
    } else if (response.status === 200) {
        return response;
    } else {
        return Promise.reject(response)
    }
}, function (error) {
    console.error('响应错误:', error)
    message.error('服务错误')
    return Promise.reject(error);
})

export default request;
