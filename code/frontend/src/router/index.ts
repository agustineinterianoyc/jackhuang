import { IconBookmark, IconHome, IconStorage } from '@arco-design/web-vue/es/icon'
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
        {
          path: 'opinion',
          name: 'opinion-root',
          redirect: { name: 'opinion-survey-list' },
          meta: { title: '指标体系意见征集', icon: IconBookmark },
          children: [
            {
              path: 'survey',
              name: 'opinion-survey-list',
              component: () => import('@/views/opinion/survey/List.vue'),
              meta: { title: '意见征集管理', icon: IconStorage },
            },
            {
              path: 'survey/:id/edit',
              name: 'opinion-survey-edit',
              component: () => import('@/views/opinion/survey/Edit.vue'),
              meta: { title: '编辑意见征集', hidden: true },
            },
            {
              path: 'survey/:id/view',
              name: 'opinion-survey-view',
              component: () => import('@/views/opinion/survey/View.vue'),
              meta: { title: '查看意见征集', hidden: true },
            },
            {
              path: 'unit-fill',
              name: 'opinion-unit-fill-list',
              component: () => import('@/views/opinion/unit-fill/List.vue'),
              meta: { title: '基层单位意见征集', icon: IconBookmark },
            },
            {
              path: 'unit-fill/:taskId',
              name: 'opinion-unit-fill-detail',
              component: () => import('@/views/opinion/unit-fill/Detail.vue'),
              meta: { title: '基层单位意见征集', hidden: true },
            },
            {
              path: 'unit-audit',
              name: 'opinion-unit-audit-list',
              component: () => import('@/views/opinion/unit-audit/List.vue'),
              meta: { title: '基层单位意见征集审核', icon: IconBookmark },
            },
            {
              path: 'unit-audit/:taskId',
              name: 'opinion-unit-audit-detail',
              component: () => import('@/views/opinion/unit-audit/Detail.vue'),
              meta: { title: '审核详情', hidden: true },
            },
          ],
        },
      ],
    },
  ],
})

const DEFAULT_TITLE = 'aldemohk'

router.afterEach((to) => {
  document.title = (to.meta?.title as string) || DEFAULT_TITLE
})

export default router
