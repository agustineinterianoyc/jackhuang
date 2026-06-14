---
name: backend-development-standard
description: 在当前仓库中修改 `code/backend/` 下的 Java 后端代码时使用。适用于新增或修改 controller、service、repository、DTO/VO/DO、配置、SQL、测试，以及多模块代码归属判断。使用时先阅读 `code/backend/AGENTS.md`，并按该工程的模块边界、目录层级、最小改动和中文注释规范执行。
---

# 后端开发规范

在当前仓库修改 `code/backend/` 下的后端代码时，按本 skill 执行。

## 开始前

先阅读：

- `code/backend/AGENTS.md`

如果改动涉及数据库表、字段、索引、DDL、初始化 SQL 或 Mapper SQL，还要同时遵守：

- `.agents/skills/条件触发层/database-schema-standard/SKILL.md`

先确认：

- 这次改动属于 `backend-api`、`backend-core`、`backend-data` 还是 `aldemohk`
- 如果是业务需求开发，是否已读取 `docs/需求/模块总表.md` 并确认模块编码
- 是否真的需要抽共享，还是应该先留在业务应用模块
- 周边文件使用的是哪种返回包装、异常风格、配置方式和测试方式

默认优先做最小一致性改动，不为了“更高级的结构”主动扩大重构范围。

## 模块归属规则

按以下顺序判断代码落点：

1. 公共契约：
   放 `backend-api`
2. 基础设施：
   放 `backend-core`
3. 通用数据访问能力：
   放 `backend-data`
4. 当前业务应用逻辑：
   放 `aldemohk`

如果不确定未来是否复用，默认先放 `aldemohk`。

不要把“可能以后会复用”的代码提前抽到共享模块。

## 业务模块目录规则

业务应用模块中的新增业务代码，优先按 `docs/需求/模块总表.md` 中登记的模块编码建立子目录。
模块编码、后端目录名和 DataSQL 目录名的唯一事实源是 `docs/需求/模块总表.md`，其维护规则由 `.agents/skills/条件触发层/requirement-implementation-tracking/SKILL.md` 负责。

示例：`部门管理 = dept`

```text
controller/dept/
service/dept/
service/dept/impl/
repository/dept/
repository/dept/impl/
mapper/dept/
model/request/dept/
model/response/dept/
model/vo/dept/
model/dataobject/dept/
resources/mapper/dept/
src/test/java/.../app/dept/
src/test/resources/test-report/dept/
```

规则：

- 新模块先登记 `docs/需求/模块总表.md`，再落代码目录。
- 已有模块继续使用原模块编码，不因为新需求改名。
- 业务私有 Controller、Service、Repository、Mapper、Model 和 Mapper XML 默认都按模块编码归档。
- 历史代码如果尚未按模块编码分目录，不要求无关搬迁；后续改到该模块时逐步收敛。
- 如果本节与 `requirement-implementation-tracking` 中的模块编码规则冲突，以 `requirement-implementation-tracking` 和模块总表为准，本 skill 只负责后端落位执行。

## 分层规则

默认遵循以下职责边界：

- `controller`
  负责接参、轻量校验、调用 service、返回统一结果；新增业务优先放到 `controller/<module>/`
- `service`
  负责业务规则、流程编排、异常抛出、事务边界；新增业务优先放到 `service/<module>/`
- `repository`
  负责数据访问、查询条件、SQL 调用；新增业务优先放到 `repository/<module>/`
- `mapper`
  负责映射，不承载业务判断；新增业务优先放到 `mapper/<module>/`
- `model/dataobject`
  负责数据库落地对象；新增业务优先放到 `model/dataobject/<module>/`
- `model/dto`
  负责模块内数据传输；新增业务优先放到 `model/dto/<module>/`
- `model/request`
  负责接口请求体；新增业务优先放到 `model/request/<module>/`
- `model/response` / `model/vo`
  负责接口返回模型和组合展示对象；新增业务优先放到 `model/response/<module>/` 或 `model/vo/<module>/`
- `config`
  负责当前层级的配置类和属性绑定类
- `task`
  负责定时任务
- `assembler`
  负责对象组装和转换

保持 controller 轻量，业务规则优先放在 service。

## API 规范

新增或修改接口时：

- 复用统一返回结构
- 复用现有错误码和业务异常
- 在 API 边界做参数校验
- 请求参数优先使用 request/dto 对象，不直接传散乱 map
- 响应优先使用 response/vo 对象
- 如果已有 Swagger / OpenAPI 风格，保持注解风格一致

除非用户明确要求，不要随意破坏已有接口兼容性。

## Service 规范

修改 service 时：

- 先明确输入、输出、规则、副作用
- 将核心业务流程放在 service
- 把查询细节留在 repository
- 保持辅助方法私有，除非明确需要复用
- 只在状态一致性确有要求时添加事务
- 遇到失败路径时，优先抛出统一业务异常

## 数据访问规范

修改 repository / mapper / SQL 时：

- 先确认查询逻辑属于共享能力还是业务私有能力
- 复杂 SQL 优先放在业务模块自己的 `resources/mapper`
- 业务私有 Mapper XML 优先按模块编码放到 `resources/mapper/<module>/`
- 只有明确跨业务复用的 SQL 才考虑放到 `backend-data`
- 注意 null 处理、分页一致性、批量更新、全表误操作风险
- 如果使用 `JdbcClient` / mapper XML / ORM，保持与周边文件一致，不主动混入新流派

## 配置规范

修改配置类和配置文件时：

- 共享基础配置放 `backend-core`
- 数据访问公共配置放 `backend-data`
- 当前应用专有配置放 `aldemohk`
- 环境配置优先写入对应 profile 文件
- 不要把业务环境配置沉到共享模块

## 注释与编辑护栏

默认遵循这些护栏：

- 保留已有中文注释和中文文本
- 不要为统一风格而改写无关注释
- 不要为了顺手整理而重排无关代码格式
- 对新增的重要类、字段、方法、关键逻辑补充中文注释
- 注释优先解释业务意图、步骤和约束，不要逐行翻译代码

如果原注释因本次改动失真，可以只更新那一处注释。

## 测试规范

修改后端功能时，默认要求为每个功能点补测试，不能把“只改了一点点”作为不写测试的理由。

这里的“功能点”默认指：

- 同一个 controller 中的一组同领域接口
- 同一个业务模块中的一组 service / repository 改动
- 同一次需求里围绕同一业务对象的接口、服务和持久层变更

修改完成后，尽量补最贴近改动层级的测试：

- 共享基础设施逻辑：
  优先在共享模块补单测
- 分页、响应工厂、工具类：
  优先补单测
- 业务接口、数据库初始化链路、Spring 装配：
  优先补集成测试

至少覆盖：

- 一条正常路径
- 一条代表性的异常或失败路径
- 边界条件或空值场景

### 测试目录约束

业务应用模块的测试目录，优先按“接口所属业务模块”组织，而不是只按技术层分散摆放。

默认理解为：

- 能放在同一个 controller 类中的接口，通常属于同一个业务模块
- 围绕这个 controller 的 service、repository、assembler 测试，也应跟着这个业务模块归档

推荐目录方式：

```text
src/main/java/com/hk/demo/app/controller/order/OrderController.java

src/test/java/com/hk/demo/app/order/OrderControllerTests.java
src/test/java/com/hk/demo/app/order/OrderServiceTests.java
src/test/java/com/hk/demo/app/order/OrderRepositoryTests.java
```

不要把同一个业务模块的测试拆得到处都是，导致后续无法快速看出“这个接口相关测试到底在哪”。

### 测试命名规范

测试类命名保持稳定可检索：

- controller 测试：
  `XxxControllerTests`
- service 测试：
  `XxxServiceTests`
- repository 测试：
  `XxxRepositoryTests`
- assembler 测试：
  `XxxAssemblerTests`
- 基础设施或工具类测试：
  `XxxTests`

不要混用模糊命名，例如：

- `Test1`
- `TempTest`
- `IntegrationCheck`
- `DemoCase`

### 最小覆盖要求

每个功能点至少覆盖以下场景：

- 正常路径
- 参数校验失败或非法输入
- 一条代表性的业务异常路径
- 一条边界条件或空值场景

如果这次改动涉及查询逻辑、分页逻辑或状态流转，还应补：

- 分页参数或筛选条件校验
- 空结果集处理
- 状态分支切换后的结果验证

### 测试结果输出约束

完成测试后，需要在同业务测试目录对应的位置输出执行详细情况，便于回看。

推荐放置方式：

```text
src/test/resources/test-report/order/execution-report.md
```

报告内容至少包括：

- 本次测试覆盖的功能点
- 相关代码文件
- 测试文件
- 执行命令
- 测试结果摘要
- 关键断言点
- 未覆盖项或遗留风险

如果没有新增测试报告文件的明确要求，至少要保证同业务目录下存在可追踪的测试执行说明，不要只在终端里跑完就结束。

可直接复用：

- `.agents/skills/默认执行层/backend-development-standard/templates/execution-report.md`

### 无法补测试时的例外处理

如果因为以下原因暂时无法补测试：

- 依赖外部系统
- 历史模块缺少可测入口
- 当前环境缺少必需中间件
- 变更点受限于老旧代码结构

则不能直接跳过，必须在测试执行说明中明确写出：

- 为什么当前无法补
- 哪些场景未覆盖
- 建议后续如何补齐
- 当前采取了什么替代验证方式

## 配置与 SQL 变更的额外验证

如果本次改动涉及以下文件：

- `application*.yaml`
- `bootstrap*.yaml`
- `schema.sql`
- `data.sql`
- `src/main/resources/mapper/*.xml`
- `backend-data/src/main/resources/mapper/*.xml`

除补测试外，还应补充至少一种额外验证：

- 应用启动验证
- 对应接口回归验证
- SQL 结果校验
- 数据初始化校验
- Mapper 参数与返回映射校验

## 变更前检查

动手前确认：

- 读过相邻文件
- 理解当前模块边界
- 确认代码应该放在哪个模块
- 如果是业务需求，确认模块编码来自 `docs/需求/模块总表.md`
- 明确该功能点对应的测试目录应该落在哪个业务模块下

## 变更后检查

结束前检查：

- 模块归属是否正确
- 业务目录是否与模块总表中的模块编码一致
- 分层职责是否清晰
- 是否复用了现有返回包装和异常类型
- 是否保持了最小必要改动
- 是否误改了无关注释、中文文本或格式
- 是否补了必要的中文注释
- 是否为功能点补了测试
- 测试目录是否跟着接口所属业务模块走
- 测试命名是否符合约定
- 是否覆盖了正常、校验失败、业务异常、边界条件
- 是否输出了同业务目录下的测试执行详细情况
- 如果无法补测试，是否在执行说明中写清原因和替代验证
- 如果改了配置或 SQL，是否补了额外验证
- 是否运行了相关测试，或者明确说明未运行原因
- 如果同一次任务也修改了 `code/frontend/`，是否补充使用 `frontend-development-standard`
- 如果本次改动涉及需求、架构、规范或跨模块 Bug，是否已按 `.agents/skills/默认执行层/change-tracking/SKILL.md` 更新对应变更记录
- 如果同一次任务同时修改了 `code/frontend/` 和 `code/backend/`，是否已按项目级变更处理并更新 `docs/变更记录/`
- 如果只是后端单模块改动，是否已更新统一的 `code/backend/CHANGELOG.md` 并标明模块名
- 是否已经实际完成留痕，而不是只输出“需要记录”或“建议记录”

## 禁止事项

- 不要把业务 Controller、业务 Service 实现塞进 `backend-core` 或 `backend-data`
- 不要把所有模型对象都提前沉到共享模块
- 不要让共享模块反向依赖业务应用
- 不要在没有必要时引入新的框架风格或大范围重构
- 不要为了“整理代码”顺手修改无关文件
