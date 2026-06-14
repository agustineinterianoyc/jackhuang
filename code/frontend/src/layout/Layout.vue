<script setup lang="ts">
import { computed, ref, watch, type Component } from 'vue'
import { IconMenuFold, IconMenuUnfold } from '@arco-design/web-vue/es/icon'
import { type RouteRecordRaw, RouterView, useRoute, useRouter } from 'vue-router'
import { useNavigationStore } from '@/stores/navigation'

defineOptions({
  name: 'AppLayout',
})

type MenuItem = {
  path: string
  name: string
  label: string
  icon?: Component
}

type MenuGroup = MenuItem & {
  children: MenuItem[]
}

const route = useRoute()
const router = useRouter()
const navigationStore = useNavigationStore()

function resolveChildPath(parentPath: string, childPath: string) {
  if (childPath.startsWith('/')) {
    return childPath
  }

  const normalizedParentPath = parentPath === '/' ? '' : parentPath.replace(/\/$/, '')
  const nextPath = childPath ? `${normalizedParentPath}/${childPath}` : normalizedParentPath || '/'

  return nextPath.startsWith('/') ? nextPath : `/${nextPath}`
}

function createMenuGroup(routeRecord: RouteRecordRaw, parentPath: string): MenuGroup | null {
  if (!routeRecord.meta?.title || routeRecord.meta?.hidden) {
    return null
  }

  const fullPath = resolveChildPath(parentPath, routeRecord.path)

  const children = (routeRecord.children ?? [])
    .filter((child) => child.meta?.title && !child.meta?.hidden)
    .map((child) => ({
      path: resolveChildPath(fullPath, child.path),
      name: child.name as string,
      label: child.meta!.title as string,
      icon: child.meta?.icon as Component | undefined,
    }))

  return {
    path: fullPath,
    name: routeRecord.name as string,
    label: routeRecord.meta.title as string,
    icon: routeRecord.meta.icon as Component | undefined,
    children,
  }
}

const rootPath = '/'

const menuGroups = computed(() =>
  (router.options.routes.find((routeRecord) => routeRecord.path === rootPath)?.children ?? [])
    .map((routeRecord) => createMenuGroup(routeRecord, rootPath))
    .filter((item): item is MenuGroup => item !== null),
)

const selectedMenuKeys = computed(() => [route.path])

const expandedSubMenus = ref<string[]>([])

function syncExpandedSubMenus(path: string) {
  const activeGroup = menuGroups.value.find(
    (group) => group.path === path || group.children.some((item) => item.path === path),
  )

  if (activeGroup && activeGroup.children.length > 0) {
    if (!expandedSubMenus.value.includes(activeGroup.path)) {
      expandedSubMenus.value = [...expandedSubMenus.value, activeGroup.path]
    }
  }
}

syncExpandedSubMenus(route.path)

watch(
  () => route.path,
  (path) => {
    syncExpandedSubMenus(path)
  },
)

function handleSubMenuClick(_key: string, openKeys: string[]) {
  expandedSubMenus.value = openKeys
}

function handleMenuItemClick(key: string) {
  if (route.path !== key) {
    router.push(key)
  }
}

const breadcrumbItems = computed(() =>
  route.matched
    .map((item) => item.meta?.title as string | undefined)
    .filter((item): item is string => Boolean(item)),
)

const isCollapsed = computed(() => navigationStore.collapsed)
const sidebarTitle = computed(() => (isCollapsed.value ? 'AL' : 'aldemohk'))

function toggleCollapsed() {
  navigationStore.toggleCollapsed()
}
</script>

<template>
  <a-layout class="layout-shell">
    <a-layout-sider
      :width="220"
      breakpoint="lg"
      :collapsed-width="56"
      :collapsed="isCollapsed"
    >
      <div class="sidebar-title" :class="{ collapsed: isCollapsed }" data-testid="sidebar-title">
        {{ sidebarTitle }}
      </div>
      <a-menu
        :selected-keys="selectedMenuKeys"
        :open-keys="expandedSubMenus"
        auto-open
        @menu-item-click="handleMenuItemClick"
        @sub-menu-click="handleSubMenuClick"
      >
        <template v-for="group in menuGroups" :key="group.name">
          <a-menu-item v-if="group.children.length === 0" :key="group.path" :data-testid="`menu-item-${group.name}`">
            <template #icon>
              <span class="menu-item-icon" data-testid="menu-icon">
                <component :is="group.icon" />
              </span>
            </template>
            {{ group.label }}
          </a-menu-item>

          <a-sub-menu v-else :key="group.path">
            <template #icon>
              <span class="menu-item-icon" data-testid="menu-icon">
                <component :is="group.icon" />
              </span>
            </template>
            <template #title>{{ group.label }}</template>
            <a-menu-item v-for="item in group.children" :key="item.path" :data-testid="`menu-item-${item.name}`">
              <template #icon>
                <span class="menu-item-icon" data-testid="menu-icon">
                  <component :is="item.icon" />
                </span>
              </template>
              {{ item.label }}
            </a-menu-item>
          </a-sub-menu>
        </template>
      </a-menu>
    </a-layout-sider>

    <a-layout class="main">
      <a-layout-header class="toolbar">
        <div class="toolbar-left">
          <a-button
            type="text"
            class="toolbar-collapse"
            data-testid="layout-collapse-trigger"
            @click="toggleCollapsed"
          >
            <IconMenuUnfold v-if="isCollapsed" />
            <IconMenuFold v-else />
          </a-button>

          <a-breadcrumb data-testid="layout-breadcrumb">
            <a-breadcrumb-item v-for="item in breadcrumbItems" :key="item">{{ item }}</a-breadcrumb-item>
          </a-breadcrumb>
        </div>

        <a-dropdown>
          <div class="toolbar-user" data-testid="layout-user-panel">
            <a-avatar :size="32">A</a-avatar>
            <span class="toolbar-user-name">管理员</span>
          </div>

          <template #content>
            <a-doption>个人设置</a-doption>
            <a-doption>退出登录</a-doption>
          </template>
        </a-dropdown>
      </a-layout-header>

      <a-layout-content class="content">
        <RouterView />
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<style scoped lang="less">
@toolbar-height: 48px;
@toolbar-border: #e4e7ed;
@content-bg: #f5f7fa;

.layout-shell {
  display: flex;
  height: 100vh;
}

.sidebar-title {
  padding: 20px 20px 12px;
  font-size: 18px;
  font-weight: 700;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;

  &.collapsed {
    padding-inline: 0;
    text-align: center;
  }
}

.menu-item-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

:deep(.arco-layout-sider-children) {
  display: flex;
  flex-direction: column;
}

.main {
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.toolbar {
  height: @toolbar-height;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  border-bottom: 1px solid @toolbar-border;

  &-left {
    display: flex;
    align-items: center;
    gap: 12px;
    min-width: 0;
  }

  &-collapse {
    font-size: 18px;
  }

  &-user {
    display: inline-flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;
  }

  &-user-name {
    font-size: 14px;
    font-weight: 500;
    color: #303133;
  }
}

:deep(.arco-breadcrumb) {
  margin: 0;
}

.content {
  flex: 1;
  padding: 20px;
  overflow: auto;
  background: @content-bg;
}
</style>
