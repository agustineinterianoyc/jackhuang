---
name: jdk-maven-matrix
description: 当需要按后端 JDK 版本选择 Maven、Spring Boot、Spring Cloud、Spring Cloud Alibaba、数据库驱动、Nacos 和常用基础依赖版本时使用。适用于项目初始化、父 POM 生成、微服务技术基线收敛和依赖兼容性核对。
---

# JDK 与 Maven 版本矩阵

维护说明：

- 最后整理日期：2026-05-14
- 当前内置代表性基线覆盖 JDK 8 和 JDK 21
- 如果用户选择 JDK 11、JDK 17 或其他 LTS，必须按“官方来源”重新核对兼容矩阵；核对后如要沉淀为固定基线，再把对应版本和来源同步补回本 skill

## 概述

用于把后端技术基线收敛成统一流程：

1. 先选 JDK
2. 再定 Maven / Spring Boot / Spring Cloud / Spring Cloud Alibaba
3. 最后落数据库驱动、Nacos 及常用基础依赖

优先使用本 skill 提供的代表性基线。
如果用户选择的 JDK 不在清单内，再按官方兼容矩阵补查，不要凭印象拼版本。

## 何时使用

- 项目初始化时要给后端选 JDK 和 Maven 基线
- 需要生成或调整后端父 POM 的版本属性
- 需要选择 Spring Boot / Spring Cloud / Spring Cloud Alibaba / Nacos 对应版本
- 需要同时兼顾 MySQL / PostgreSQL / Oracle 等多数据库驱动
- 需要检查当前后端依赖组合是否可用

## 不适用场景

- 只是改单个业务依赖，且不影响后端技术基线
- 只是补一个普通 starter，且版本已经由当前 Spring Boot BOM 托管
- 明确要求完全沿用现有父 POM，不做任何版本决策

## 核心规则

1. JDK 决定大版本代际，Spring Boot / Spring Cloud / Spring Cloud Alibaba 必须按官方矩阵对齐。
2. 能交给 Spring Boot BOM 管理的依赖，不要重复手写版本。
3. 只要启用 Spring Cloud Alibaba / Nacos，就优先选官方明确适配的 Boot / Cloud / Alibaba 组合。
4. 多数据库驱动优先跟随所选 Spring Boot 代际的 BOM 版本，不主动跨代混搭。
5. 生成 POM 时至少同步 `java.version`、编译插件、Enforcer、BOM 导入和数据库驱动策略。

## 推荐工作流

1. 先确认用户选的是 `8`、`21` 还是其他 LTS。
2. 再确认是否必须使用 `Spring Cloud Alibaba / Nacos`。
3. 如果命中本 skill 的代表性基线，直接采用清单。
4. 如果没命中，再按“官方来源”一节逐项补查。
5. 生成父 POM 后，按“依赖可用性检查”执行一次兼容性核对。

## 代表性基线

### 基线 A1：JDK 8 且需要 Nacos / Spring Cloud Alibaba

这是 JDK 8 下优先采用的“官方对齐组合”。

| 层级 | 推荐版本 | 说明 |
| --- | --- | --- |
| JDK | 8 | 兼容老环境优先 |
| Maven | 3.9.15 | Maven 官方当前稳定版，运行要求 JDK 8+ |
| maven-compiler-plugin | 3.15.0 | 用 `maven.compiler.release=8` |
| maven-enforcer-plugin | 3.6.1 | 用 `requireJavaVersion [1.8,1.9)` |
| Spring Boot | 2.6.13 | Spring Cloud Alibaba 2021.0.6.0 官方适配版本 |
| Spring Cloud | 2021.0.5 | 与上面 Boot / Alibaba 官方对齐 |
| Spring Cloud Alibaba | 2021.0.6.0 | JDK 8 + Nacos 场景优先基线 |
| Nacos | 2.2.0 | 来自 Alibaba 官方组件矩阵 |
| Sentinel | 1.8.6 | 来自 Alibaba 官方组件矩阵 |
| RocketMQ | 4.9.4 | 来自 Alibaba 官方组件矩阵 |
| Seata | 1.6.1 | 来自 Alibaba 官方组件矩阵 |
| MyBatis-Plus | 3.5.15 | 使用 `mybatis-plus-boot-starter` |
| MySQL 驱动 | 8.0.31 | 由 Boot 2.6.13 BOM 托管 |
| PostgreSQL 驱动 | 42.3.7 | 由 Boot 2.6.13 BOM 托管 |
| Oracle 驱动 | ojdbc8 21.3.0.0 | 由 Boot 2.6.13 BOM 托管 |
| H2 | 1.4.200 | 本地验证/测试可选 |
| HikariCP | 4.0.3 | 由 Boot 2.6.13 BOM 托管 |
| Lettuce | 6.1.10.RELEASE | Redis 客户端由 Boot 2.6.13 BOM 托管 |
| Micrometer | 1.8.11 | 监控基础依赖由 Boot 2.6.13 BOM 托管 |
| springdoc-openapi | 1.6.x+ | Boot 2.7.x / 2.6.x 兼容线 |
| Knife4j | 4.0+ | Boot 2.4.0~2.7.x 可用 |

### 基线 A2：JDK 8 但不使用 Alibaba / Nacos BOM

如果项目只需要传统 Spring Boot + Spring Cloud，不要求引入 Spring Cloud Alibaba，可采用更靠后的 Boot 2.x 基线：

| 层级 | 推荐版本 | 说明 |
| --- | --- | --- |
| JDK | 8 | 兼容老环境优先 |
| Maven | 3.9.15 | Maven 官方当前稳定版 |
| Spring Boot | 2.7.18 | Boot 2.x 最后稳定线之一 |
| Spring Cloud | 2021.0.8 | 官方文档明确支持 Boot 2.6.15 |
| Spring Cloud Alibaba | 不引入 | 该备选线不用于 Nacos / Alibaba BOM |
| 数据库驱动 / Redis / Micrometer | 跟随 Boot 2.7.18 BOM | 不额外手写版本 |

如果用户说“JDK 8，但必须用 Nacos”，不要选这个备选线，回到基线 A1。

### 基线 B：JDK 21 现代微服务基线

这是当前仓库默认更接近的现代化基线。

| 层级 | 推荐版本 | 说明 |
| --- | --- | --- |
| JDK | 21 | 当前主流 LTS，优先推荐 |
| Maven | 3.9.15 | Maven 官方当前稳定版，运行要求 JDK 8+ |
| maven-compiler-plugin | 3.15.0 | 用 `maven.compiler.release=21` |
| maven-enforcer-plugin | 3.6.1 | 用 `requireJavaVersion [21,22)` |
| Spring Boot | 4.0.6 | 当前仓库默认值，属于 Boot 4.0.x 代际 |
| Spring Cloud | 2025.1.0 | 当前仓库默认值，属于 Cloud 2025.1.x 代际 |
| Spring Cloud Alibaba | 2025.1.0.0 | 官方对应 Cloud 2025.1.0 / Boot 4.0.x |
| Nacos | 3.1.1 | 来自 Alibaba 官方组件矩阵 |
| Sentinel | 1.8.9 | 来自 Alibaba 官方组件矩阵 |
| RocketMQ | 5.3.1 | 来自 Alibaba 官方组件矩阵 |
| SchedulerX | 1.13.3 | 来自 Alibaba 官方组件矩阵 |
| Seata | 2.5.0 | 来自 Alibaba 官方组件矩阵 |
| MyBatis-Plus | 3.5.15 | 使用 `mybatis-plus-spring-boot4-starter` |
| MySQL 驱动 | 9.7.0 | 由 Boot 4.0.6 BOM 托管 |
| PostgreSQL 驱动 | 42.7.10 | 由 Boot 4.0.6 BOM 托管 |
| Oracle 驱动 | ojdbc11 23.9.0.25.07 | 由 Boot 4.0.6 BOM 托管 |
| H2 | 2.4.240 | 本地验证/测试可选 |
| HikariCP | 7.0.2 | 由 Boot 4.0.6 BOM 托管 |
| Lettuce | 6.8.2.RELEASE | Redis 客户端由 Boot 4.0.6 BOM 托管 |
| Micrometer | 1.16.5 | 监控基础依赖由 Boot 4.0.6 BOM 托管 |
| springdoc-openapi | 3.x | Boot 4.x 兼容线；当前脚手架默认 `3.0.3` |
| Knife4j | 4.0+ | 当前脚手架默认 `4.5.0` |

说明：

- `Boot 4.0.x -> Cloud 2025.1.x` 来自 Spring Cloud 官方 release train。
- `Alibaba 2025.1.0.0 -> Cloud 2025.1.0 / Boot 4.0.x` 来自 Spring Cloud Alibaba 官方版本说明。
- `Boot 4.0.6` 是结合当前仓库现状采用的具体补丁版本，属于同一官方兼容代际内的实现选择。

## 生成 POM 时的默认规则

### 1. 必须落的属性

- `java.version`
- `spring.cloud.version`
- `spring.cloud.alibaba.version`
- `mybatis.plus.version`
- `maven.enforcer.version`

### 2. 必须落的插件约束

- `maven-compiler-plugin`
  - 统一使用 `maven.compiler.release`
- `maven-enforcer-plugin`
  - 至少校验 `requireJavaVersion`
  - 如用户要求统一构建工具，也可补 `requireMavenVersion`

### 3. 必须落的 BOM / 依赖管理策略

- Spring Boot 父 POM 或 `spring-boot-dependencies`
- `spring-cloud-dependencies`
- 使用 Alibaba 时再导入 `spring-cloud-alibaba-dependencies`
- 数据库驱动优先不写版本，直接吃 Boot BOM

### 4. MyBatis-Plus 选择规则

- Boot 2.x：`mybatis-plus-boot-starter`
- Boot 3.x：`mybatis-plus-spring-boot3-starter`
- Boot 4.x：`mybatis-plus-spring-boot4-starter`

不要同时再引入 `mybatis-spring-boot-starter` 或手工补一套 MyBatis 版本。

### 5. API 文档组件选择规则

- Boot 2.x：`springdoc-openapi 1.6.x+`
- Boot 3.x：`springdoc-openapi 2.x`
- Boot 4.x：`springdoc-openapi 3.x`
- Knife4j 默认按 OpenAPI 3 方案，优先选择 `4.0+`

如果仓库已有固定值，优先保持与现有模板一致。

## 当用户选的是其他 JDK 版本

如果用户选的不是 `8` 或 `21`，按下面顺序补查：

1. 查 Spring Boot 官方系统要求，确认该 JDK 能落到哪个 Boot 代际。
2. 查 Spring Cloud 官方 release train，确认对应 Boot 代际可用的 Cloud 版本。
3. 如果要用 Alibaba / Nacos，再查 Spring Cloud Alibaba 官方版本说明。
4. 查 Spring Boot 该版本的 `dependency versions`，获取数据库驱动、Lettuce、Micrometer 等托管版本。
5. 查 MyBatis-Plus 安装页，确认对应 starter。
6. 查 springdoc FAQ 和 Knife4j 版本参考，确认文档组件兼容线。

只有这几步都补齐，才开始生成 POM。

## 依赖可用性检查

生成或修改后端版本后，至少做以下检查：

1. 静态矩阵检查
   - JDK 与 `java.version` 是否一致
   - Spring Boot / Cloud / Alibaba 是否落在同一官方兼容代际
   - MyBatis-Plus starter 是否匹配 Boot 代际
   - 多数据库驱动是否来自同一 Boot BOM
2. Maven 结构检查
   - `./mvnw -q -N help:effective-pom`
   - 看父 POM、properties、dependencyManagement 是否按预期生效
3. 构建检查
   - `./mvnw -pl aldemo -am -DskipTests validate`
   - 如已配置 Enforcer，再补 `./mvnw -pl aldemo -am -DskipTests enforcer:enforce`
4. 依赖树检查
   - `./mvnw -pl aldemo -am -DskipTests dependency:tree`
   - 重点看 `spring-cloud*`、`spring-cloud-alibaba*`、数据库驱动、MyBatis-Plus 是否出现跨代冲突

如果当前仓库还没有真实业务模块，至少也要对父工程和唯一应用模块做一次检查结论说明。

## 给 `project-init` 的对接要求

当 `project-init` 询问 JDK 版本时：

1. 先优先让用户在 `8`、`21` 或其他 LTS 中选择。
2. 只要命中 `8` 或 `21`，优先直接使用本 skill 的版本矩阵。
3. 当生成 Maven 相关依赖和工具版本时，默认从本 skill 取值。
4. 如果用户选的 JDK 在本 skill 中没有现成约束，再自行查官方资料补齐，并把补查结果写回初始化事实摘要。
5. 初始化结束前，必须补一次“依赖可用性检查”。

## 常见错误

- 先拍脑袋选 Boot，再倒推 JDK
- JDK 8 还硬上 Boot 4 / Cloud 2025.x
- 用了 Spring Cloud Alibaba，却没有按官方矩阵收敛 Boot / Cloud
- 数据库驱动、Redis、Micrometer 手工乱写版本，覆盖 Boot BOM
- Boot 4 还沿用 Boot 2 的 MyBatis-Plus starter
- 初始化只改版本号，不做 `effective-pom` / `validate` / `dependency:tree` 检查

## 官方来源

- Maven 安装与 JDK 要求：
  - https://maven.apache.org/install.html
- Maven Compiler Plugin `release` 说明：
  - https://maven.apache.org/plugins/maven-compiler-plugin/examples/set-compiler-release.html
- Spring Boot 2.7.18 系统要求：
  - https://docs.spring.io/spring-boot/docs/2.7.x/reference/html/getting-started.html
- Spring Boot 2.6.13 依赖版本：
  - https://docs.spring.io/spring-boot/docs/2.6.13/reference/html/dependency-versions.html
- Spring Boot 4.0.6 系统要求：
  - https://docs.spring.io/spring-boot/system-requirements.html
- Spring Boot 4.0.6 托管依赖：
  - https://docs.spring.io/spring-boot/appendix/dependency-versions/coordinates.html
- Spring Cloud release train：
  - https://spring.io/projects/spring-cloud
- Spring Cloud 2021.0.8 参考文档：
  - https://docs.spring.io/spring-cloud/docs/2021.0.8/reference/html/
- Spring Cloud Alibaba 2021.x 版本说明：
  - https://sca.aliyun.com/en/docs/2021/overview/version-explain/
- Spring Cloud Alibaba 2025.x 版本说明：
  - https://sca.aliyun.com/docs/2025.x/overview/version-explain/
- MyBatis-Plus 安装说明：
  - https://baomidou.com/en/getting-started/install/
- springdoc 兼容矩阵：
  - https://springdoc.org/faq.html
- Knife4j 版本参考：
  - https://doc.xiaominfo.com/docs/quick-start/start-knife4j-version
