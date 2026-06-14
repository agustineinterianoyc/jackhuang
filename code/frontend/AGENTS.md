# AGENTS.md — 前端

## 技术栈

- **框架**: Vue 3 (Composition API + `<script setup>`)
- **构建**: Vite 8
- **语言**: TypeScript
- **路由**: Vue Router 5（哈希模式）
- **状态管理**: Pinia 3
- **测试**: Vitest 4 + @vue/test-utils
- **Lint**: ESLint 10 + oxlint 1.60 + Prettier 3
- **类型检查**: vue-tsc 3
- **CSS**: Less（Vite 原生支持，仅需 `less` 包）

## Skill 约束

- `code/frontend/` 下的正式前端开发，优先先读本文件，再按需遵守项目级 skill：
  `.agents/skills/默认执行层/frontend-development-standard/SKILL.md`
- 本文件负责说明前端工程事实；复杂前端执行规则、组件边界、状态管理约束、交付检查项和收尾要求由 skill 收口。

## 常用命令

| 命令 | 说明 |
|---|---|
| `npm run dev` | 启动开发服务器 |
| `npm run build` | 类型检查 + 生产构建 |
| `npm run preview` | 预览生产构建 |
| `npm run test:unit` | 运行单元测试 (Vitest) |
| `npm run type-check` | 类型检查 (vue-tsc) |
| `npm run lint` | oxlint + ESLint 自动修复 |
| `npm run format` | Prettier 格式化 src/ |

## 目录结构

```
src/
├── main.ts            # 应用入口
├── App.vue            # 根组件（仅渲染 Layout）
├── layout/
│   └── Layout.vue     # 全局布局：左侧菜单 + 顶部工具栏 + 内容区
├── router/
│   └── index.ts       # Vue Router 配置（哈希模式 + 全局守卫）
├── stores/            # Pinia store
├── components/        # 公共组件
├── views/             # 路由页面（index.vue 为首页）
├── types/
│   └── api.ts         # API 请求响应类型定义
├── utils/
│   └── request.ts     # axios 封装（拦截器 + 泛型方法）
└── assets/
    ├── main.less      # 全局样式入口
    └── base.less      # CSS 变量与 reset
```

## 路由

### 哈希模式
使用 `createWebHashHistory()`，URL 以 `#` 开头。

### 路由守卫
`router.afterEach` 全局后置守卫自动更新页面标题：
- 取 `to.meta.title` 作为 `document.title`
- 未匹配时回退为 `aldemohk`

### 路由配置示例
```ts
{
  path: '/example',
  name: 'example',
  component: () => import('../views/ExampleView.vue'),
  meta: { title: '示例页' },   // 菜单标签 + 页面标题
}
```

新增路由后菜单自动出现，无需手动修改 Layout。

## API 请求

### 统一响应结构 (`src/types/api.ts`)

```ts
ApiResponse<T>   // { code: number, message: string, data: T }
PageParams       // { page: number, pageSize: number }
PageResult<T>    // { list: T[], total: number, page: number, pageSize: number }
```

### 请求工具 (`src/utils/request.ts`)

基于 axios 封装，导出泛型方法直接获取 `data`：

```ts
import { get, post, put, del } from '@/utils/request'

const data = await get<SomeType>('/api/url', { params })
const result = await post<SomeType>('/api/url', { body })
```

**特性**：
- 自动注入 `Bearer token`（从 localStorage）
- 响应拦截统一解包 `data`
- 401 自动清除 token
- 超时 15s

### 环境变量

| 变量 | 说明 | 默认值 |
|---|---|---|
| `VITE_API_BASE_URL` | API 基础路径 | `http://localhost:3000/api` |

- `.env` — 开发默认配置（已 gitignore）
- `.env.example` — 配置模板
- `.env.local` — 本地覆盖（已 gitignore）

## 布局

```
┌──────────┬──────────────────────────┐
│          │       工具栏              │
│  侧边栏   ├──────────────────────────┤
│  (菜单)   │                          │
│          │       内容区              │
│  220px   │     (RouterView)          │
└──────────┴──────────────────────────┘
```

## CSS / Less

- 组件样式使用 `<style scoped lang="less">`
- 全局样式在 `assets/main.less` 中管理
- Less 变量（`@sidebar-width` 等）定义在各自组件中
- Vite 自动处理 Less 编译，无需额外插件配置

## 路径别名

`@` → `src/`（已在 vite.config.ts 和 tsconfig 中配置）

## 编码约定

- **组件**: Composition API + `<script setup lang="ts">`
- **样式**: 组件内使用 `lang="less"` + scoped，全局样式放 `assets/`
- **命名**: 组件 PascalCase，文件 kebab-case 或 PascalCase
- **类型**: 尽可能使用 TypeScript 类型推导，必要时显式声明
- **Store**: Pinia composition API 风格 (setup stores)
