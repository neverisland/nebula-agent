import {createStore} from 'vuex'
import user, {UserState} from "@/store/models/user.ts";

export interface AllState {
    user: UserState,
}


// 实例化缓存
const store = createStore<AllState>({

    modules: {
        user
    }

})

export default store
