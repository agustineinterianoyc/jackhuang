---
name: prototype-page-builder
description: 当需要为产品经理或评审方创建、扩展产品原型页面时使用。默认产出到 docs/原型站点，并复用统一布局、独立 menu 配置、iframe 页面加载和 html/css/js 模板。
---

# 产品原型页面构建

## 概述

用于创建和持续维护 `docs/原型站点/` 下的产品原型站点。
核心原则是把站点级壳、菜单关系、页面内容拆开管理，让后续 AI 能快速读懂结构并稳定增量追加页面。

这是一个面向产品经理和方案评审场景的直接原型产出 skill。
当任务目标是“出原型页面”而不是“做正式开发”时，应直接产出原型目录和页面文件，不进入 `docs/superpowers/specs/` 这类设计文档流程，也不按正式前端开发流程引入构建、测试、组件工程化等要求。

如果用户直接点名 `prototype-page-builder` skill，或明确要求“初始化原型站点”，应直接进入原型落地动作，不要求追加 superpower 工作流、spec 流程或前置方案文档。

## 何时使用

- 用户是产品经理、业务方、方案评审方，目标是直接看到原型页面
- 用户要求创建产品原型页面、流程演示页、方案评审页
- 用户要求在已有原型站点里新增页面、调整菜单、补充默认页
- 需要统一入口页、统一 layout、统一样式和通用方法
- 需要通过 `iframe` 加载 `pages/` 下的独立页面文件

## 不适用场景

- 正式业务前端开发
- 需要构建工具、组件库、接口联调、状态管理的工程项目
- 需要复杂前端路由、权限体系、缓存策略的正式系统

## 执行模式

当该 skill 适用时，默认采用“直接原型模式”：

1. 直接在 `docs/原型站点/` 下产出或修改原型文件
2. 首次初始化时直接复制 `template/prototype/` 整套模板
3. 后续优先修改 `menu.js` 和 `pages/*.html`

不要把这类任务转成以下流程，除非用户明确要求：

- 不进入 `docs/superpowers/specs/` 设计文档流程
- 不要求先走 superpower skill 编排或补充 superpower 产物
- 不先写实现计划再等待批准
- 不按正式前端工程任务要求引入 Vue、构建工具、测试框架
- 不把“原型页面”误判成“开发页面”

## 固定目录约定

这个 skill 的原型站点统一生成到：

```text
docs/原型站点/
```

推荐目录结构：

```text
docs/原型站点/
├── index.html
├── index.css
├── main.js
├── menu.js
└── pages/
    ├── home.html
    ├── empty.html
    ├── 404.html
    └── *.html
```

这里的目录名固定为 `原型站点`，不是业务主题名占位符。
也就是说，原型站点应统一落在 `docs/原型站点/`，再由 `menu.js` 和 `pages/` 管理不同页面与原型主题内容。

## 文件关系总览

```text
index.html
  -> 定义站点唯一入口和整体 layout
  -> 引入 index.css 和 main.js

main.js
  -> 读取 menu.js
  -> 渲染站点菜单和标题区
  -> 控制 iframe 加载 pages/*.html
  -> 维护默认页、当前激活项、空态、基于菜单 key 的 404 回退、轻量弹窗和消息提示

menu.js
  -> 页面菜单关系唯一维护点
  -> 只放菜单树、默认页、页面元信息
  -> 不放 DOM 操作

pages/*.html
  -> 只负责具体页面内容
  -> 被 index.html 中的 iframe 加载
  -> 不重复实现站点级导航和 layout
```

## 初始化方式

初始化原型站点时，默认直接复制 `template/prototype/` 下的整套模板文件到目标目录。

推荐顺序：

1. 先复制 `index.html`、`index.css`、`main.js`、`menu.js` 和 `pages/` 基础页面
2. 再按实际原型需求修改 `menu.js`
3. 最后新增或调整 `pages/*.html`

不要在初始化时手工逐个新建壳文件，除非用户明确要求从零自定义结构。

## 模板文件职责

### index.html

- 只负责站点壳结构
- 固定包含：顶部栏、左侧菜单、右侧标题区、iframe 内容区、通用弹窗挂载点
- 不写业务页面内容
- 不手写静态菜单 DOM

### index.css

- 负责站点级 layout 样式
- 负责通用原型样式，例如卡片、表单区块、表格占位、标签、空态、按钮
- 不堆积单个页面的专属样式

### main.js

- 负责原型站点初始化
- 负责根据 `menu.js` 渲染菜单
- 负责 `hash` 与页面切换
- 负责 `iframe` 地址设置、标题同步、按 key 回退到 `404.html`
- 负责轻量站点级通用方法
- 在 `http/https` 场景下可先检查页面文件可达性，再决定是否回退到 `404.html`

### menu.js

- 唯一维护菜单关系
- 推荐使用对象格式而不是纯数组
- 页面 key 全站唯一
- 菜单展示顺序以配置顺序为准
- 模板支持多层 `children` 嵌套菜单

### pages/*.html

- 每个页面一个文件
- 页面只负责内容区原型
- 页面优先复用 `index.css` 中已有 class
- 页面局部样式可写在页面内 `<style>`，仅在多个页面复用时再上提到 `index.css`

## 标准页面结构

页面推荐使用以下结构：

```html
<div class="proto-page">
  <div class="proto-page-header">
    <h1>页面标题</h1>
    <p>页面说明</p>
  </div>

  <section class="proto-section">
    ...
  </section>
</div>
```

推荐复用的通用 class：

- `proto-page`
- `proto-page-header`
- `proto-section`
- `proto-card`
- `proto-grid`
- `proto-form`
- `proto-actions`
- `proto-table`
- `proto-tag`
- `proto-empty`
- `proto-banner`
- `proto-kv`

## 菜单配置规范

`menu.js` 推荐结构：

```js
window.PROTOTYPE_MENU = {
  siteName: '产品创建原型',
  siteDesc: '用于演示产品创建流程与页面关系',
  defaultPage: 'home',
  items: [
    {
      key: 'home',
      label: '首页',
      file: 'pages/home.html',
      desc: '原型总览'
    },
    {
      key: 'product-create',
      label: '产品创建',
      children: [
        {
          key: 'product-basic',
          label: '基础信息',
          file: 'pages/product-basic.html',
          desc: '名称、分类、描述'
        }
      ]
    }
  ]
}
```

规则：

- 有 `children` 的父节点默认不直接绑定 `file`
- 叶子节点必须提供 `file`
- `file` 必须指向同级 `pages/` 目录下的 html 文件
- 新增页面时必须同时新增菜单配置

## 新增页面标准流程

1. 在 `pages/` 下新增 `xxx.html`
2. 在 `menu.js` 中注册页面 key、label、file、desc
3. 检查默认页、父子菜单关系、顺序是否合理
4. 仅在确有站点级复用需求时，才修改 `main.js` 或 `index.css`

如果是首次初始化原型站点，不要直接从第 1 步开始手工搭空目录；应先完整复制模板，再做增量修改。

## 通用方法约定

`main.js` 推荐内置这些轻量方法：

- `loadPage(key)`
- `getMenuItem(key)`
- `setActiveMenu(key)`
- `renderMenu(items)`
- `updatePageHeader(item)`
- `showToast(message, type)`
- `openDialog(options)`
- `closeDialog()`

不要在初版内置复杂事件总线、权限控制、主题切换、多标签缓存。

## 样式约定

- 全局壳样式放 `index.css`
- 业务页面局部样式优先写在对应 `pages/*.html` 的 `<style>` 中
- 如果两个以上页面共用一套样式，再上提到 `index.css`
- 命名优先通用化，不写大量业务专属 class 到全局文件

## 常见错误

1. 把菜单硬编码进 `main.js`
2. 把业务页面内容直接写进 `index.html`
3. 每个页面重复实现站点导航和外壳
4. 页面文件不放进 `pages/`
5. 把单页专属样式大量堆进 `index.css`
6. 没有准备 `empty.html` 和 `404.html`，导致初始化和按 key 回退能力薄弱
7. 追加页面时只改 html，不同步更新 `menu.js`

## 强约束

- 原型任务默认直接产出页面，不进入 `docs/superpowers/specs/` 设计文档流程
- 用户明确要求直接初始化时，不追加 superpower 流程或其配套文档
- 菜单关系只能在 `menu.js` 维护
- 业务页面只能放在 `pages/`
- 首次初始化时，优先复制 `template/prototype/` 整套模板文件，不手工逐个新建壳文件
- `index.html` 只负责站点壳，不负责业务页面内容
- 非站点级需求，不修改 `index.html` 主结构
- 非公共行为，不修改 `main.js` 主流程
- 非复用样式，不修改 `index.css` 全局规则

## 支撑模板文件

本 skill 自带可复制模板：

- `template/prototype/index.html`
- `template/prototype/index.css`
- `template/prototype/main.js`
- `template/prototype/menu.js`
- `template/prototype/pages/home.html`
- `template/prototype/pages/empty.html`
- `template/prototype/pages/404.html`
- `template/prototype/pages/_tailwind.js` — Tailwind CSS 本地副本（仅原型使用）
- `template/prototype/pages/_list-common.css` — 列表页统一组件样式
- `template/prototype/pages/_fa/` — Font Awesome 6.5.1 本地副本（仅原型使用）
- `template/prototype/pages/js/` — 通用 JS 方法库（proto-toast / proto-modal / proto-page / proto-pagination / proto-table / proto-data / proto-confirm）

初始化时直接复制以上模板文件到 `docs/原型站点/`。
复制模板后，优先调整 `menu.js` 和 `pages/`，而不是先改壳文件。

## 原型页面约定

**新增页面头部按以下顺序引入：**

```html
<link rel="stylesheet" href="_fa/all.min.css">
<link rel="stylesheet" href="_list-common.css">
<script src="_tailwind.js"></script>
<script src="js/proto-toast.js"></script>
<script src="js/proto-modal.js"></script>
<script src="js/proto-page.js"></script>
<script src="js/proto-pagination.js"></script>
<script src="js/proto-table.js"></script>
    <script src="js/proto-data.js"></script>
    <script src="js/proto-confirm.js"></script>
```

注意事项：
- 以上资源均为原型专用，不用于正式交付代码
- `_fa/` 是 Font Awesome 6.5.1 本地副本
- `_tailwind.js` 是 Tailwind CSS 本地副本
- `_list-common.css` 提供列表页共用组件样式（表格、表单、按钮、分页、弹窗等），页面不应重复定义这些 class
- `js/proto-*.js` 提供通用 JS 方法，页面不应重复实现以下能力：
  - Toast 提示 → `ProtoToast.success/error/info(msg)`
  - 弹窗开关 → `ProtoModal.open/close(id)`
  - 页面切换 → `ProtoPage.switchTo(pageId)` / `ProtoPage.back(listPageId)`
  - 分页渲染 → `ProtoPagination.render({containerId, total, current, pageSize, onChange})`
  - 表格排序/筛选 → `ProtoTable.sort(data, field, direction)` / `ProtoTable.filter(data, filters)`
  - 数据映射 → `ProtoData.statusText(status)` / `ProtoData.deptName(dept)`
  - 确认弹窗 → `ProtoConfirm.show({title, message, danger, onConfirm, onCancel})` 替代原生 `confirm()`
- 页面可在 `<style>` 中写局部样式，仅在两个以上页面复用时再上提到 `_list-common.css`
- 页面作为 iframe 内容被加载，不应重复实现站点导航、标题等壳功能
- 不引入 Google Fonts 等无法本地化的外部资源，字体统一使用系统栈
