import { createRouter, createWebHistory } from 'vue-router';
import HelloWorld from "@/views/home/HelloWorld.vue";
import ConsoleLogin from "@/views/console/login/ConsoleLogin.vue";
import ConsoleMenu from "@/views/console/menu/ConsoleMenu.vue";
import RoleManagement from "@/views/console/system-manage/role/RoleManagement.vue";
import UserManagement from "@/views/console/system-manage/user/UserManagement.vue";
import { useTimerStore } from "@/store/timer.ts";
import ConsoleMainPage from "@/views/console/page/ConsoleMainPage.vue";
import FileLibraryList from "@/views/console/file-space/file-library/FileLibraryList.vue";
import FileShare from "@/views/console/file-space/file-share/FileShare.vue";
import FileSpace from "@/views/console/file-space/file-space/FileSpace.vue";

let routes = [
    {
        path: '/',
        redirect: '/console',
    },
    {
        path: '/home',
        component: HelloWorld,
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
                    },
                    {
                        // 文件库 - 我的文件
                        path: 'file-library',
                        name: 'file-library',
                        component: FileLibraryList
                    },
                    {
                        // 文件库 - 个人空间（占位）
                        path: 'personal-space',
                        name: 'personal-space',
                        component: FileSpace
                    },
                    {
                        // 文件库 - 我的分享
                        path: 'my-share',
                        name: 'my-share',
                        component: FileShare
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
router.beforeEach((to, _from, next) => {
    const timerStore = useTimerStore()
    // 在白名单停止是否登录监听
    if (whiteListRoutes.includes(to.path)) {
        timerStore.stopTimer()
    } else {
        timerStore.startTimer()
    }

    next()
})

// 白名单路由
export const whiteListRoutes: string[] = [
    '/',
    '/home',
    '/console/login',
]

export default router;
