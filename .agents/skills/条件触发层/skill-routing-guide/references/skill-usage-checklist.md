# 项目级 Skill 使用清单

## 通用触发方式

优先使用两种提问方式：

- 自然描述任务
  例：`读取 docs/需求/原始需求/20260513批次需求 和 docs/原型/20260513批次原型，先拆分模块，再生成本次需求包计划。`
- 显式点名 skill
  例：`这次按 @.agents\skills\条件触发层\requirement-implementation-tracking\SKILL.md 来做，读取 docs/需求/原始需求/20260513批次需求 和 docs/原型/20260513批次原型，先拆模块，再生成需求包。`

如果任务很明确，直接描述任务通常就够。
如果你想把执行口径钉死，建议显式带上 skill 路径。

## 场景清单

### 1. 需求拆分、模块归类、需求包计划

- 推荐 skill：`requirement-implementation-tracking`
- 什么时候用：
  只要任务要先读需求和原型、判断模块边界、维护模块总表、生成或更新需求包，就应使用。
- 推荐说法：
  `按 @.agents\skills\条件触发层\requirement-implementation-tracking\SKILL.md 执行，读取 docs/需求/原始需求/<批次>/ 和 docs/原型/<批次>/，先判断这是小改动、阶段性变更还是新需求包，再拆分模块、更新模块总表，并生成本次需求包的 README、实现计划、实现进度。`

### 2. 多角色协作开发、交接提示词、来回复核

- 推荐 skill：`multi-model-delivery-loop`
- 什么时候用：
  只要你希望把任务拆成“规划复核角色”和“实现修复角色”两类责任，并要求每轮交接都有固定文案和进度记录，就应使用。
- 推荐说法：
  `这次按 @.agents\skills\条件触发层\multi-model-delivery-loop\SKILL.md 执行，把当前需求包改成多角色协作模式。请先确定当前责任角色、下一责任角色、当前执行工具，并输出标准交接包；到达切换点时先停下来，不要替下一角色继续执行。`

### 3. 正式后端开发

- 推荐 skill：`backend-development-standard`
- 什么时候用：
  只要改 `code/backend/` 下的 controller、service、repository、DTO、VO、DO、配置、测试，就应使用。
- 推荐说法：
  `按 @.agents\skills\默认执行层\backend-development-standard\SKILL.md 执行，在 code/backend/ 下做最小改动。先判断模块归属，再实现接口/服务/持久层，并补必要验证和留痕。`

### 4. 正式前端开发

- 推荐 skill：`frontend-development-standard`
- 什么时候用：
  只要改 `code/frontend/` 下的页面、组件、路由、Pinia、类型、请求封装、样式或前端测试，就应使用。
- 推荐说法：
  `按 @.agents\skills\默认执行层\frontend-development-standard\SKILL.md 执行，在 code/frontend/ 下做最小改动。请先判断页面、组件、store、类型和请求逻辑的落点，再补必要验证和留痕。`

### 5. 数据库表、字段、索引、Mapper SQL、DataSQL

- 推荐 skill：`database-schema-standard`
- 什么时候用：
  只要涉及数据库结构、DDL、初始化 SQL、Mapper XML、数据回填或 DataSQL 留痕，就应使用；通常和后端 skill 一起用。
- 推荐说法：
  `按 @.agents\skills\条件触发层\database-schema-standard\SKILL.md 执行，这次涉及数据库变更。请先判断数据库方言，再同步更新表结构、Mapper SQL 和 code/backend/DataSQL 留痕。`

### 6. 变更记录、规范调整、架构决策补录

- 推荐 skill：`change-tracking`
- 什么时候用：
  只要本次改动会影响需求口径、架构边界、公共规范、跨模块 Bug 修复，或者后续需要反查“为什么这样改”，就应使用。
- 推荐说法：
  `按 @.agents\skills\默认执行层\change-tracking\SKILL.md 执行，判断本次应该写模块 CHANGELOG 还是 docs/变更记录，并把背景、变更内容、影响范围和决策原因补齐。`

### 7. 原型页面、评审稿、演示站点

- 推荐 skill：`prototype-page-builder`
- 什么时候用：
  目标是给产品经理或评审方看原型，而不是正式落到 `code/frontend/` 时使用。
- 推荐说法：
  `按 @.agents\skills\人工触发层\prototype-page-builder\SKILL.md 执行，在 docs/原型站点/ 下产出原型站点，不要进入正式前端工程。`

### 8. 项目初始化、清理示例残留、替换脚手架事实

- 推荐 skill：`project-init`
- 什么时候用：
  拿到示例工程后，准备收敛为真实项目时使用。
- 推荐说法：
  `按 @.agents\skills\人工触发层\project-init\SKILL.md 执行，先扫描脚手架残留，再收集项目事实，最后生成初始化改造清单和需要同步更新的文档。`

### 9. JDK、Maven、Spring Boot、Spring Cloud 版本基线

- 推荐 skill：`jdk-maven-matrix`
- 什么时候用：
  新建后端父 POM、收敛版本矩阵或核对依赖兼容性时使用。
- 推荐说法：
  `按 @.agents\skills\人工触发层\jdk-maven-matrix\SKILL.md 执行，基于当前 JDK 版本给我一套 Maven、Spring Boot、Spring Cloud、数据库驱动和 Nacos 的兼容版本清单。`

### 10. 构建、打包、发布脚本

- 推荐 skill：`generating-release-scripts`
- 什么时候用：
  需要前端静态产物、后端 jar/war、Docker 镜像，或分层 publish/register/deploy 脚本时使用。
- 推荐说法：
  `按 @.agents\skills\人工触发层\generating-release-scripts\SKILL.md 执行，为当前前后端工程生成构建和发布脚本，并说明每个脚本的适用场景。`

### 11. 不确定该用哪个 skill

- 推荐 skill：`skill-routing-guide`
- 什么时候用：
  任务还没开做，只想先判断路由和提问方式时使用。
- 推荐说法：
  `按 @.agents\skills\条件触发层\skill-routing-guide\SKILL.md 帮我判断，这个任务该走哪些项目级 skill，推荐我一条适合直接复制的提示话术。`

## 常用组合

### 需求开发标准组合

- 顺序：`requirement-implementation-tracking` -> `frontend-development-standard` / `backend-development-standard` / `prototype-page-builder` -> `change-tracking`
- 适用：从需求输入开始，到开发实现和留痕收尾

### 含数据库的需求开发

- 顺序：`requirement-implementation-tracking` -> `backend-development-standard` + `database-schema-standard` -> `change-tracking`
- 适用：涉及表结构、字段、索引、Mapper SQL 或 DataSQL

### 多角色协作开发

- 顺序：`requirement-implementation-tracking` -> `multi-model-delivery-loop` -> 具体实现 skill -> `change-tracking`
- 适用：希望把规划复核和实现修复分给两类角色执行

### 页面现象排查但最终涉及前后端联动

- 顺序：`frontend-development-standard` + `backend-development-standard` -> `change-tracking`
- 适用：起点是浏览器现象、页面显示异常、联调问题、字段展示不对，但最终同时修改了 `code/frontend/` 和 `code/backend/`
- 推荐说法：
  `按前后端联动修复处理。先用 frontend-development-standard 和 backend-development-standard 完成最小改动；如果同一次任务同时修改了 code/frontend/ 和 code/backend/，收尾时必须再按 change-tracking 补 docs/变更记录。`

## 提问习惯建议

- 需求类任务，尽量带上“读取哪个批次的需求和原型”
- 后端类任务，尽量带上“改哪个模块、哪个接口、是否涉及数据库”
- 多角色协作任务，尽量带上“当前责任角色、下一责任角色、是否需要输出交接包”
- 文档类任务，尽量带上“这是规范变更、架构变更还是需求补录”

## 不推荐的问法

- `你帮我随便看看怎么做`
- `顺便把相关的都改了`
- `这个你自己判断就行，不用记文档`

这三类说法都会让范围和留痕边界变得不稳定。
