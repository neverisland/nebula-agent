import {createApp} from 'vue';
import './style.css';
import App from './App.vue';
import router from "./router";
import {createPinia} from "pinia";
import store from "@/store/cache.ts";
import 'ant-design-vue/dist/reset.css';

import VMdPreview from '@kangc/v-md-editor/lib/preview';
import '@kangc/v-md-editor/lib/style/preview.css';
// 引入你所使用的主题 此处以 github 主题为例
import githubTheme from '@kangc/v-md-editor/lib/theme/github';
import '@kangc/v-md-editor/lib/theme/style/github.css';

import hljs from 'highlight.js';

VMdPreview.use(githubTheme, {
    Hljs: hljs,
});

const pinia = createPinia();

createApp(App)
    .use(pinia)
    .use(store)
    .use(router)
    .use(VMdPreview)
    .mount('#app')
