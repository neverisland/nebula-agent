import {AllState} from '@/store/cache.ts'
import {ActionContext} from 'vuex'

export type ThemeType = 'light' | 'dark'

export type ThemeState = {
    currentTheme: ThemeType
}

const state: ThemeState = {
    currentTheme: 'light' // 默认为亮色主题，实际值在初始化时设置
}

const theme = {
    namespaced: true,

    state,

    mutations: {
        setTheme(state: ThemeState, theme: ThemeType) {
            state.currentTheme = theme
            localStorage.setItem('theme', theme)
        }
    },

    getters: {
        getCurrentTheme: (state: ThemeState) => state.currentTheme,
        currentTheme: (state: ThemeState) => state.currentTheme,
        isDarkTheme: (state: ThemeState) => state.currentTheme === 'dark'
    },

    actions: {
        toggleTheme(context: ActionContext<ThemeState, AllState>) {
            return new Promise<void>((resolve) => {
                const newTheme = context.state.currentTheme === 'light' ? 'dark' : 'light'
                context.commit('setTheme', newTheme)
                resolve()
            })
        },

        setTheme(context: ActionContext<ThemeState, AllState>, theme: ThemeType) {
            context.commit('setTheme', theme)
        }
    }
}

export default theme