# 脚手架清理清单

把内容分成三类：直接清理、默认确认后清理、默认保留并收敛。

## 直接清理

- `code/frontend/node_modules/`
- `code/frontend/dist/`
- `code/frontend/coverage/`
- `code/frontend/.cache/`
- `code/frontend/.turbo/`
- `code/backend/**/target/`
- `.idea/`
- `code/backend/*/.idea/`
- 临时测试脚本、临时缓存文件
- 本地 `.env` 中不应提交的个人配置

## 默认确认后清理

- 扫描发现的示例业务链路（如 CommonController、CommonService、DemoMessageRepository、DemoMessageMapper、DemoMessageDO 等）
- 扫描发现的字典管理示例（如 DictController、DictService、DictTypeMapper、DictEntryMapper 等）
- `DataSQL/` 扫描发现的示例目录（如 A业务/、字典管理/、公共基础/）
- 扫描发现的前端示例页面、示例菜单、示例 API 封装
- `docs/superpowers/` 下只服务于示例项目的方案/计划文档
- `docs/变更记录/` 下只服务于示例项目的历史记录

## 默认保留并收敛

- 多模块后端结构
- `backend-api/backend-core/backend-data` 的职责边界
- 统一返回结构、异常处理、traceId 机制
- 多数据库 profile 机制
- AI 协作规范文档
- 项目级 skill 目录
- 模板级约束文档与其规则主体
- 父 POM `pluginManagement` 中 `spring-boot-maven-plugin` 的 `<skip>true</skip>` 配置（多模块 Spring Boot 项目必须，否则非应用模块执行 `run`/`repackage` 时报错）

## 删除前必须确认的风险点

- 示例代码是否已经承载“最小可运行模板”的职责
- 字典管理是否被视为通用基础能力
- 示例 SQL 是否被用作初始化模板
- 某些示例目录是否已经被其他文档引用
- 旧变更记录是模板演进历史，还是示例项目业务历史
- 清理 `docs/` 历史记录后，是否需要先做归档

## 建议执行顺序

1. 先扫描残留
2. 再确认保留/清理边界
3. 先删生成物和本地缓存
4. 再处理示例业务代码
5. 再处理示例文档、旧记录和 DataSQL 示例目录
6. 最后同步文档与留痕
7. 生成 `script/` 目录下的开发启动脚本
