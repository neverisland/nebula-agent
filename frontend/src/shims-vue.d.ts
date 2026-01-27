declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}

// 扩展 Vue 模板中的静态资源路径类型
// 允许在模板中使用 public 目录下的绝对路径
declare module '@vue/runtime-core' {
  import { RouteLocationNormalizedLoaded, Router } from 'vue-router';
  interface ComponentCustomProperties {
    $route: RouteLocationNormalizedLoaded;
    $router: Router;
  }
}