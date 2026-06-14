# DataSQL — 指标体系意见征集（opinion）

## 文档目的

记录本模块 13 张表（`ad_opinion_*`）的物理变更流水。完整 DDL 与设计逻辑以 `docs/设计/指标体系意见征集模块/数据库设计说明书 指标体系意见征集模块.md` 为准；建表 SQL 落 `code/backend/aldemohk/src/main/resources/db/mysql/schema.sql`。

## 目录

- `业务变更明细.md` — 表清单、变更流水、枚举映射
- `表设计/` — 每张表的设计说明（与设计文档保持一致；如需补字段级别注释可在此追加）
- `migration/` — 后续上线后的迁移脚本（首次落地不使用）

## 与汇总清单的关系

- 模块级流水：本目录 `业务变更明细.md`
- 项目级汇总：`code/backend/DataSQL/业务变更汇总清单.md`（已登记 `OPINION-DDL-001` 流水）
