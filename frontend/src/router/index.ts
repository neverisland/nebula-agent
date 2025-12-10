import {createRouter, createWebHistory} from 'vue-router';
import HelloWorld from "@/views/home/HelloWorld.vue";
import ConsoleLogin from "@/views/console/login/ConsoleLogin.vue";
import ConsoleMenu from "@/views/console/menu/ConsoleMenu.vue";
import RoleManagement from "@/views/console/role/RoleManagement.vue";
import UserManagement from "@/views/console/user/UserManagement.vue";
import {useTimerStore} from "@/store/timer.ts";
import Chat from "@/views/chat/Chat.vue";
import ConsoleMainPage from "@/views/console/page/ConsoleMainPage.vue";

let routes = [
    {
        path: '/',
        redirect: '/chat',
    },
    {
        path: '/home',
        component: HelloWorld,
    },
    {
        path: '/chat', // chat
        name: 'chat',
        component: Chat,
    },
    {
        // 后台根路由
        path: '/console',
        name: '',
        redirect: '/console/login',
        children: [
            {
                // 后台登录
                path: 'login',
                name: 'login',
                component: ConsoleLogin
            },
            {
                // 后台菜单
                path: 'page',
                name: 'page',
                redirect: '/console/page/home',
                component: ConsoleMenu,
                children: [
                    {
                        // 后台主页面
                        path: 'home',
                        name: 'console',
                        component: ConsoleMainPage
                    },
                    {
                        // 角色管理
                        path: 'role',
                        name: 'role',
                        component: RoleManagement
                    },
                    {
                        // 用户管理
                        path: 'user',
                        name: 'user',
                        component: UserManagement
                    }
                ]
            }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes: routes,
});

/**
 * 全局路由守卫
 */
router.beforeEach((to, from, next) => {
    const timerStore = useTimerStore()
    // 在白名单停止是否登录监听
    if (whiteListRoutes.includes(to.path)) {
        timerStore.stopTimer()
    } else {
        timerStore.startTimer()
    }

    next()
})


export const whiteListRoutes = [
    '/',
    '/home',
    '/console/login',
] as const

export default router;
