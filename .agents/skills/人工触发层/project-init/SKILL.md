---
name: project-init
description: 当开发者拿到这个支架项目，希望Codex通过收集项目事实、清理演示残留物、重命名包/模块/应用标识符，以及同步AGENTS和变更记录来初始化为真正的项目时，才会在正式开发开始前使用。
---

# 项目初始化

## 概述

用于把当前“AI 开发流程示例工程”初始化成某个真实项目的起点。
核心目标不是简单替换几个名字，而是先识别脚手架残留，再收集项目个性化事实，最后把代码、配置、文档和留痕一起收敛到新项目状态。

## 何时使用

- 开发者刚拿到这个仓库，准备把它作为新项目脚手架使用
- 用户要求执行“项目初始化”“脚手架初始化”“收敛示例工程”“开始新项目”
- 需要批量替换包根、模块名、应用名、数据库标识、前端项目名称等项目事实
- 需要清理演示业务代码、演示 SQL、示例文档、本地产物和无关缓存
- 需要把初始化结果同步到 `AGENTS.md`、后端/前端约束文档和变更记录

## 不适用场景

- 已经明确是日常前端开发、后端开发、数据库开发或原型开发
- 只改单个配置项，不涉及脚手架整体身份切换
- 只是新增一个业务模块，不是把整个仓库从示例工程初始化成新项目

## 执行原则

1. 先扫描，再提问，不要上来就全局替换。
2. 先区分“必须替换的占位信息”和“可选保留的演示能力”。
3. 提问分批进行，优先给推荐默认值，降低开发者回答成本。
4. 初始化属于全局规范变更，完成后必须同步更新 `docs/变更记录/`。
5. 初始化不是只改代码；还包括清理残留、更新文档、补留痕、补验证。
6. 默认保护脚手架模板规范，除项目事实外，不要重写 skill 规则本身。
7. 技术基线涉及后端 JDK / Maven / Spring Boot / Spring Cloud / Spring Cloud Alibaba / 数据库驱动 / Nacos 时，优先从 `../jdk-maven-matrix/SKILL.md` 取值。
8. 本 skill 自带脚本为 Python 脚本，执行前需要确认本地可用 `python` 或等价 Python 3 命令。
9. 澄清完成后必须生成初始化任务清单，每完成一步更新清单状态，防止任务走偏或遗漏。

## 推荐工作流

### 第 1 步：扫描脚手架残留

先运行：

```powershell
python .agents/skills/人工触发层/project-init/scripts/scan_init_targets.py
```

用途：

- 扫描项目中的占位标识，例如包根、模块名、示例业务名
- 识别演示业务残留（如 DemoMessage、DictController 等，具体以扫描结果为准）
- 识别不应进入新项目模板的本地产物，例如 `.idea`、`node_modules`、`target`

如需追加自定义关键词，可使用：

```powershell
python .agents/skills/人工触发层/project-init/scripts/scan_init_targets.py --token 自定义关键词
```

如果扫描结果显示示例业务残留较多，不要直接删除所有内容；先进入第 2 步确认保留策略。

如需脚本化清理生成物和示例残留，可先预览：

```powershell
python .agents/skills/人工触发层/project-init/scripts/cleanup_scaffold.py --dry-run
```

确认后再执行：

```powershell
python .agents/skills/人工触发层/project-init/scripts/cleanup_scaffold.py --execute
```

如果要额外处理示例文档、DataSQL 示例目录或旧变更记录，使用对应开关，详见脚本帮助。

如需根据初始化问答结果执行半自动改造，可先复制配置模板，再预览：

```powershell
python .agents/skills/人工触发层/project-init/scripts/apply_init_config.py .agents/skills/人工触发层/project-init/references/init-config.template.json --dry-run
```

确认后执行：

```powershell
python .agents/skills/人工触发层/project-init/scripts/apply_init_config.py E:/work/project/init-config.json --execute
```

### 第 2 步：分批收集项目信息

按 [references/init-questionnaire.md](references/init-questionnaire.md) 的顺序提问，不要一次性抛出所有问题。

涉及后端技术基线时，先读取：

```text
.agents/skills/人工触发层/jdk-maven-matrix/SKILL.md
```

要求如下：

- 先让开发者在 `JDK 8`、`JDK 21` 或其他长期维护版本中选择
- 如果选中 `8` 或 `21`，生成 Maven 与常用依赖版本时，优先直接采用版本矩阵中的代表性基线
- 如果开发者选的是清单外 JDK，再按该 skill 中的“官方来源”补查对应版本
- 只要项目明确要用 `Nacos / Spring Cloud Alibaba`，就不要自行跨代拼装 Boot / Cloud / Alibaba 版本

推荐分四批：

1. 项目身份
2. 技术基线
3. 脚手架保留/清理策略（根据扫描结果动态裁剪，已不存在的残留不再追问）
4. 文档与运行约定

能从仓库现状推断的内容，先给默认建议，再让开发者确认或改动。

### 第 3 步：生成初始化任务清单

澄清完成后，必须立即在 `docs/项目信息/` 下创建初始化任务清单，作为整个初始化过程的执行锚点。

推荐落点：

```text
docs/项目信息/初始化任务清单.md
```

任务清单必须包含：

1. 从第 1 步扫描结果和第 2 步问答结果推导出的全部待执行任务
2. 每个任务明确写出：做什么、涉及哪些文件/目录、预期结果
3. 每个任务有一个状态：`待执行` / `执行中` / `已完成` / `已跳过（原因）`
4. 任务按依赖顺序排列，例如先改名再改引用、先清理残留再同步文档

推荐任务分类：

- 命名替换（包根、模块名、前端项目名称等）
- 残留清理（本地产物、示例代码、示例 DataSQL 等）
- 目录重命名（物理目录、包路径目录等）
- 配置更新（pom.xml、application.yaml、package.json、.env 等）
- 文档同步（AGENTS.md、约束文档、规范中的项目事实等）
- 验证与留痕（构建验证、依赖检查、变更记录等）

**每完成一个任务，必须立即更新清单中对应条目的状态。**
如果执行中发现某个任务需要调整（新增、拆分、合并、跳过），也要先更新清单再继续。

### 第 4 步：形成初始化事实源

收集完信息后，先整理一份“初始化事实摘要”再开始执行。

推荐落点：

```text
docs/项目信息/项目初始化信息.md
```

如果目录不存在，初始化时创建。

这份文档至少记录：

- 项目中文名、英文名、简称
- 后端包根
- 业务应用模块名
- JDK / Maven / Spring Boot / Spring Cloud / Spring Cloud Alibaba / Node 基线
- MyBatis-Plus、数据库驱动、多数据库 profile、Nacos 及其他中间件的版本来源
- 默认数据库类型、保留的环境 profile 与保留的数据库类型 profile
- 表名前缀、数据库名、服务名
- 前端标题、前端项目名称
- 需要清理的示例残留
- 明确保留的公共能力

可直接参考 [references/init-deliverables.md](references/init-deliverables.md) 中的模板。
如果准备半自动执行初始化，可同时把这些事实整理进 [references/init-config.template.json](references/init-config.template.json) 对应结构。
完成后回到任务清单，确认"事实源已生成"并更新状态。

### 第 5 步：执行初始化改造

**执行前先确认任务清单中所有任务已被识别和确认。**

改造时至少覆盖以下几类内容：

- 命名替换：
  包根、模块目录名、共享模块包路径物理目录、业务模块物理目录名、`artifactId`、`spring.application.name`、页面标题、前端项目名称、数据库名、服务名、Nacos dataId 文件名
- 残留清理：
  示例业务、示例 SQL、示例 DataSQL、示例测试报告、本地产物、缓存目录
- 文档同步：
  `AGENTS.md`、`code/backend/AGENTS.md`、`code/frontend/AGENTS.md`、`docs/规范/AI开发SOP.md`、`docs/规范/`
- 结构确认：
   明确哪些共享模块保留，哪些演示能力转为真实默认骨架
- 插件配置修正：
   父 POM 的 `pluginManagement` 中必须为 `spring-boot-maven-plugin` 添加 `<configuration><skip>true</skip></configuration>`，防止在非应用模块上执行 `run`/`repackage` 时报 `Unable to find a suitable main class` 错误；业务应用模块（如 `indexdemo-app`）在自己的 `pom.xml` 中显式声明该插件即可覆盖跳过

如果业务应用模块名发生变化，不要只改 Maven `artifactId` 或文档描述；还要同步改物理目录名、父 POM `modules`、启动命令示例、测试路径说明和引用该模块名的变更记录。

清理策略参考 [references/scaffold-cleanup-checklist.md](references/scaffold-cleanup-checklist.md)。
脚本化执行方式参考 [references/apply-init-config.md](references/apply-init-config.md)。

**每完成一个改造任务，立即更新任务清单状态，确保不遗漏、不偏离。**
如果改造过程中发现清单未覆盖的新任务，先补充到清单再执行。

### 第 6 步：补验证、依赖检查和留痕

初始化完成后，至少做两类收尾：

1. 验证
   - 前端：按改动范围执行 `npm run type-check`、`npm run build` 或其他必要命令
   - 后端：按改动范围执行 `./mvnw test`、`./mvnw clean verify` 或最小可行校验
   - 后端依赖矩阵：按 `../jdk-maven-matrix/SKILL.md` 至少补一次 JDK / Boot / Cloud / Alibaba / MyBatis-Plus / 数据库驱动兼容性核对
   - 如果本次改了父 POM 或技术基线，优先补：
     - `./mvnw -q -N help:effective-pom`
     - `./mvnw -pl ai-training-project -am -DskipTests validate`
     - `./mvnw -pl ai-training-project -am -DskipTests dependency:tree`
2. 留痕
   - 更新 `docs/变更记录/CHANGELOG.md`
   - 更新 `docs/变更记录/规范变更.md`
   - 如果初始化改变了目录边界或模块结构，也同步补 `架构变更.md`
3. 生成开发启动脚本
   - 在项目根目录创建 `script/` 目录
   - 生成以下脚本，所有路径和模块名使用初始化后的真实值：
     - `script/start-backend.sh`：`cd code/backend && ./mvnw -pl <业务模块名> -am spring-boot:run`
     - `script/start-frontend.sh`：`cd code/frontend && npm run dev`
     - `script/start-all.sh`：并行启动前后端，Ctrl+C 同时停止
    - 脚本必须可执行（`chmod +x`）
    - 脚本中注入项目信息（项目名、模块名、端口等）作为启动提示
    - **脚本生成完成后必须逐条执行验证**：在脚本所在目录直接运行（如 `bash script/start-backend.sh`、`bash script/start-frontend.sh`、`bash script/start-all.sh`），确认每个脚本能正常启动无报错，启动失败则调试修正后重新验证，直到全部通过
4. 关闭任务清单
   - 确认所有任务均为"已完成"或"已跳过（含原因）"
   - 在清单末尾记录初始化完成时间、最终结论和剩余待人工补充项

## 初始化时默认要关注的事实

- 后端包根是否需要整体替换
- `ai-training-project` 是否改成真实业务模块名
- 业务应用模块的物理目录名是否与 `artifactId` 完全一致
- `backend-api/backend-core/backend-data` 中的包路径物理目录是否随包根一起改名
- `backend-api` / `backend-core` / `backend-data` 是否保持现名
- JDK 版本、Spring Boot 版本、Maven 版本是否沿用模板默认值
- 如果要改 JDK 版本，Maven / Spring Boot / Spring Cloud / Spring Cloud Alibaba / Nacos / 数据库驱动是否已按 `../jdk-maven-matrix/SKILL.md` 收敛
- 前端 `package.json` 名称、页面标题、接口前缀是否要改
- 默认数据库是否保留 MySQL / Oracle / PostgreSQL 三套 profile
- 环境 profile 与数据库类型 profile 是否已分开记录，避免把 `dev/test/prod` 与 `mysql/postgresql/oracle` 混成一组
- `DataSQL` 中的示例目录（如扫描发现的 A业务、公共基础、字典管理等）是否保留、重命名或清理
- 演示链路（如扫描发现的 CommonController -> CommonService -> DemoMessageRepository -> DemoMessageMapper）是否保留为最小示例
- 字典管理示例能力（如扫描发现）是否保留为通用基础能力
- `docs/` 下历史记录、旧方案文档、旧变更记录采用“保留 / 归档 / 重建”哪种策略
- 本地生成物和 IDE 目录是否需要清理
- 父 POM 的 `spring-boot-maven-plugin` 是否已配置 `<skip>true</skip>`：Spring Boot 多模块项目中，父 POM 和共享模块（`backend-api`/`backend-core`/`backend-data`）没有 main class，必须在该插件的 `pluginManagement` 中设置跳过，仅业务应用模块显式声明并执行 `run`/`repackage`

## 决策提示

### 关于演示业务

遇到以下内容时，默认不要擅自保留：

- 仅用于示例说明的 controller / service / mapper / DO
- 仅用于演示的首页文案、示例路由、示例菜单
- 仅用于演示的 SQL、DataSQL 业务目录

遇到以下内容时，默认优先保留并改名收敛：

- 共享模块拆分结构
- 通用响应、异常、trace、分页等基础设施
- 多数据库 profile 机制
- AI 协作流程文档和项目级 skill

### 关于模板规范保护边界

以下内容默认视为“脚手架规则资产”，初始化时不要因为项目个性化而改写规则本身：

- `.agents/skills/`
- `.agents/description/`
- `docs/规范/AI开发SOP.md`
- `docs/规范/`

这意味着新增加的项目级 skill，例如 `multi-model-delivery-loop`，默认也自动处于保护范围内，不需要每次新增 skill 后再单独补一条保护规则。

这些文件只允许做两类调整：

1. 项目事实替换
   例如默认业务模块名、默认数据库、示例项目名、启动模块名
2. 与当前仓库结构强绑定的目录事实修正
   例如业务模块目录名、默认 profile 名、`DataSQL` 默认示例目录名

不要做的事：

- 不要顺手改这些 skill 的核心工作流
- 不要把某个真实项目的业务规则写回模板级 skill
- 不要把一次项目初始化时的临时决定，提升成所有后续项目都必须遵守的模板规则

如果发现某条模板规则真的应该升级为通用能力，先单独作为模板演进任务处理，不要混在某个项目初始化里静默改掉。

### 关于 `docs/` 历史记录

`docs/` 下的旧记录默认不能一刀切地直接保留。
初始化时必须显式确认以下策略之一：

1. 保留：
   继续把旧记录当作模板演进历史保存在当前仓库
2. 归档：
   移到 `docs/历史归档/项目初始化前-时间戳/`
3. 重建：
   清理旧的示例记录，仅保留初始化后的最小文档骨架

推荐默认值：

- 对模板演进历史有价值的记录，优先归档，不直接删除
- 对明显属于示例项目业务过程的记录，可在确认后清理

### 关于提问方式

不要一次性问二十多个问题。
优先使用“推荐默认值 + 是否修改”的方式，例如：

- “后端共享模块名默认继续保留 `backend-api/backend-core/backend-data`，是否沿用？”
- “默认数据库建议保留 `mysql`，同时暂时保留 `oracle/postgresql` profile 作为扩展位，是否这样处理？”

### 关于删除动作

以下内容通常可直接清理：

- `node_modules/`
- `target/`
- `.idea/`
- 临时测试脚本
- 与新项目无关的本地缓存

以下内容删除前应确认：

- 字典管理等可能被视为“基础能力”的示例模块
- `DataSQL/` 中已承载规范示例作用的业务目录
- 演示接口链路是否要保留为最小模板
- `docs/变更记录/`、`docs/superpowers/` 等历史文档是删除、归档还是重建
- 即使选择归档或重建 `docs/变更记录/`，也要保留各分类文档中的“模板示例”段落，不要把初始化后的骨架重建成只有标题的空文件

## 输出要求

当使用这个 skill 真正执行初始化时，最终结果至少应包含：

- 已确认的项目初始化事实
- `docs/项目信息/初始化任务清单.md`（所有条目已关闭）
- 已清理的示例残留和本地产物
- 已替换的命名与配置
- 已更新的约束文档
- 已补的变更记录
- 已执行的验证、依赖兼容检查或未执行原因
- 剩余待人工补充项

## 关联资源

- [references/init-questionnaire.md](references/init-questionnaire.md)
- [references/scaffold-cleanup-checklist.md](references/scaffold-cleanup-checklist.md)
- [references/init-deliverables.md](references/init-deliverables.md)
- [references/init-config.template.json](references/init-config.template.json)
- [references/apply-init-config.md](references/apply-init-config.md)
- [references/protected-template-assets.md](references/protected-template-assets.md)
- `../jdk-maven-matrix/SKILL.md`
- `scripts/scan_init_targets.py`
- `scripts/cleanup_scaffold.py`
- `scripts/apply_init_config.py`
