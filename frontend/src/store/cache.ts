import {createStore} from 'vuex'
import user, {UserState} from "@/store/models/user.ts";
import theme, {ThemeState} from "@/store/models/theme.ts";

export interface AllState {
    user: UserState,
    theme: ThemeState,
}


// 实例化缓存
const store = createStore<AllState>({

    modules: {
        user,
        theme
    }

})

export default store
