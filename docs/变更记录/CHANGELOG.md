# CHANGELOG

## 2026-06-14 — 项目重新初始化（aldemo → aldemohk）

- **变更类型**：项目级身份重命名
- **变更摘要**：基于 `project-init` 流程，将项目从 `aldemo`（公司培训项目）重新初始化为 `aldemohk`。
- **改动范围**：
  - 业务模块物理目录重命名：`code/backend/aldemo/` → `code/backend/aldemohk/`
  - 全仓库文本替换：`aldemo` → `aldemohk`，`Aldemo` → `Aldemohk`
  - 启动类重命名：`AldemoApplication` → `AldemohkApplication`
  - Nacos 配置重命名：`aldemo.yaml` → `aldemohk.yaml`
  - 前端标题/项目名统一为 `aldemohk`
  - 数据库名/Maven artifactId/spring.application.name/Nacos namespace 统一为 `aldemohk`
  - 清理全部本地产物（target/、node_modules/、dist/、.opencode/node_modules）
  - 旧文档归档至 `docs/历史归档/项目初始化前-20260614105111/` 并重建最小骨架
  - 修复批量替换引入的 UTF-8 BOM
- **影响模块**：全部
- **验证结果**：
  - Maven 5/5 模块 validate SUCCESS
  - 依赖树版本完全匹配基线 B（JDK 21 + Boot 4.0.6 + Cloud 2025.1.0 + Alibaba 2025.1.0.0）
  - 前端 type-check 通过，build 成功
- **剩余待人工补充**：
  - MySQL 数据库 `aldemohk` 建库与授权
  - Nacos 环境配置（namespace: aldemohk, dataId: aldemohk.yaml）
