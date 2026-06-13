# `apply_init_config.py` 使用说明

## 目标

把初始化问答结果收敛成一份结构化配置，再由脚本统一执行：

- 目录重命名
- 文本替换
- 受保护文件中的项目事实替换
- 初始化信息文档生成
- 可选清理动作

## 推荐流程

1. 复制 [init-config.template.json](init-config.template.json) 为一份真实配置
2. 先执行 `dry-run`
3. 检查输出的重命名、替换和清理计划
4. 确认后再执行 `--execute`

## 命令示例

```powershell
python .agents/skills/人工触发层/project-init/scripts/apply_init_config.py .agents/skills/人工触发层/project-init/references/init-config.template.json --dry-run
```

```powershell
python .agents/skills/人工触发层/project-init/scripts/apply_init_config.py E:/work/project/init-config.json --execute
```

## 字段说明

### `path_renames`

用于物理目录或文件改名。

典型场景：

- `code/backend/ai-training-project` 改成真实业务模块目录名
- 各后端模块的 `src/main/java/com/hzzenith/ai/training` 改成真实包路径
- 各后端模块的 `src/test/java/com/hzzenith/ai/training` 改成真实测试包路径
- `src/main/resources/nacos/ai-training-project.yaml` 改成真实 Nacos dataId 文件名

脚本会按父路径优先的顺序执行重命名，并在后续嵌套路径上自动映射已经改名的父目录。

### `global_include_roots`

定义全局文本替换的扫描范围。

推荐至少包括：

- `AGENTS.md`
- `code/backend`
- `code/frontend`
- `docs`

### `global_replacements`

用于普通项目文件中的批量替换。

默认不会进入受保护模板资产。

短缩写要尽量写成带上下文的替换值，例如把前端标题中的 `'PE'` 替换为 `'YP'`，不要直接替换裸词 `PE`，避免误改依赖锁文件中的完整性摘要。

### `protected_fact_targets`

显式指定哪些受保护文件允许做“项目事实替换”。

推荐只放：

- `docs/规范/AI开发SOP.md`
- `docs/规范/`
- 或你明确允许做“项目事实替换”的模板级 skill / 规范文档

普通项目文件例如 `AGENTS.md`、`code/backend/AGENTS.md`、`code/frontend/AGENTS.md`，默认应交给 `global_replacements` 处理，不要重复放到这里。

不要直接把整个 `.agents/skills/` 都放进去。

### `protected_fact_replacements`

只用于受保护文件中的项目事实替换。

典型例子：

- 示例业务模块名
- 示例项目标题
- 默认数据库 profile 名

不要放过于宽泛、可能改变规则语义的替换项，例如单独替换 `training`、`service` 这类短词。

### `init_document`

用于生成 `docs/项目信息/项目初始化信息.md`。

建议把问答结果都落到这里，作为初始化事实源。

数据库 profile 建议拆成两组：

- `retained_env_profiles`：`dev/test/prod` 等环境 profile
- `retained_database_profiles`：`mysql/postgresql/oracle` 等数据库类型 profile

### `cleanup`

控制是否在应用配置后顺带跑脚手架清理。

推荐做法：

- 第一轮 `dry-run` 时打开看计划
- 真实执行时按需逐项开启
- 对 `archive_change_records` 保持谨慎，只在确认旧记录应归档时启用

### `residual_scan_tokens`

用于配置应用后复扫旧项目标识。

推荐至少保留：

- 旧包根
- 旧业务模块名
- 旧项目名或前端项目名称
- Nacos namespace / 数据库名等旧配置标识

复扫结果不自动删除内容，只把仍然命中的文件列出来，便于人工判断它们是脚手架事实、模板示例，还是必须继续替换的残留。
