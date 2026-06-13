import { IconHome } from '@arco-design/web-vue/es/icon'
import { createRouter, createWebHashHistory } from 'vue-router'
import Layout from '@/layout/Layout.vue'

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    {
      path: '/',
      component: Layout,
      children: [
        {
          path: '',
          name: 'home',
          component: () => import('@/views/index.vue'),
          meta: { title: '首页', icon: IconHome },
        },
      ],
    },
  ],
})

const DEFAULT_TITLE = '公司培训项目'

router.afterEach((to) => {
  document.title = (to.meta?.title as string) || DEFAULT_TITLE
})

export default router
