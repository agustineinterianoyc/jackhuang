import { describe, expect, it } from 'vitest'
import { mount } from '@vue/test-utils'
import ArcoVue from '@arco-design/web-vue'
import { createPinia } from 'pinia'
import router from '@/router'
import Layout from '@/layout/Layout.vue'
import HomeView from '@/views/index.vue'
import { useNavigationStore } from '@/stores/navigation'

Object.defineProperty(window, 'matchMedia', {
  writable: true,
  value: (query: string) => ({
    matches: false,
    media: query,
    onchange: null,
    addListener: () => {},
    removeListener: () => {},
    addEventListener: () => {},
    removeEventListener: () => {},
    dispatchEvent: () => false,
  }),
})

describe('layout shell', () => {
  it('defines home as the only scaffold route', () => {
    const rootRoute = router.options.routes.find((route) => route.path === '/')

    expect(rootRoute?.children).toEqual([
      expect.objectContaining({
        path: '',
        name: 'home',
        meta: expect.objectContaining({ title: '首页' }),
      }),
    ])
    expect(rootRoute?.children?.[0]?.meta?.icon).toBeTruthy()
  })

  it('renders layout with home menu and collapsed title', async () => {
    await router.push('/')
    await router.isReady()

    const pinia = createPinia()
    const wrapper = mount(Layout, {
      global: {
        plugins: [pinia, router, ArcoVue],
      },
    })

    expect(wrapper.find('.arco-layout').exists()).toBe(true)
    expect(wrapper.find('[data-testid="layout-collapse-trigger"]').exists()).toBe(true)
    expect(wrapper.find('[data-testid="layout-breadcrumb"]').text()).toContain('首页')
    expect(wrapper.text()).toContain('首页')

    const navigationStore = useNavigationStore(pinia)
    navigationStore.setCollapsed(true)
    await wrapper.vm.$nextTick()

    expect(wrapper.find('[data-testid="sidebar-title"]').text()).toBe('AL')
  })

  it('renders empty scaffold home page', () => {
    const wrapper = mount(HomeView)

    expect(wrapper.text()).toContain('首页')
    expect(wrapper.text()).toContain('当前脚手架已移除演示业务模块')
  })
})
