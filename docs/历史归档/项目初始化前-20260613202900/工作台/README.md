# 工作台说明

`docs/工作台/` 是面向人工阅读的静态进度工作台。

## 目录职责

- `index.html`、`index.css`、`main.js`：工作台页面本体
- `scripts/build-workbench-data.js`：扫描 `docs/需求/` 下的 Markdown，生成展示数据
- `data/workbench-data.js`：供页面直接加载的结构化数据文件

## 使用规则

1. Markdown 仍然是正式事实源。
2. 工作台只做总览、筛选、表格展示和跳转，不替代需求文档、模块文档或变更记录。
3. 需求包、模块或原型批次有变更后，先运行：

```powershell
node docs/工作台/scripts/build-workbench-data.js
```

再打开或刷新 `index.html` 查看最新结果。

如果希望手动刷新更省事，也可以直接双击：

```text
docs/工作台/scripts/refresh-workbench.cmd
```
