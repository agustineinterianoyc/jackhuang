#!/usr/bin/env python3
"""Clean generated artifacts and optional demo residue for scaffold initialization."""

from __future__ import annotations

import argparse
import shutil
from datetime import datetime
from pathlib import Path


ARTIFACT_GLOBS = [
    ".idea",
    "code/frontend/.cache",
    "code/frontend/.turbo",
    "code/frontend/coverage",
    "code/frontend/dist",
    "code/frontend/node_modules",
    "code/backend/*/target",
    "code/backend/*/.idea",
]

REPORT_GLOBS = [
    "code/backend/*/src/test/resources/test-report",
]

DEMO_DOC_GLOBS = [
    "docs/superpowers/plans",
    "docs/superpowers/specs",
]

DEMO_DATASQL_GLOBS = [
    "code/backend/DataSQL/A业务",
]

CHANGE_RECORD_FILES = [
    "docs/变更记录/CHANGELOG.md",
    "docs/变更记录/需求变更.md",
    "docs/变更记录/架构变更.md",
    "docs/变更记录/规范变更.md",
    "docs/变更记录/bug修复.md",
]

CHANGE_RECORD_TEMPLATE = {
    "CHANGELOG.md": "# CHANGELOG\n\n按时间倒序记录项目的重要变更摘要。\n\n## 模板示例\n\n- [需求] 示例标题\n  - 详情：`需求变更.md`\n- [架构] 示例标题\n  - 详情：`架构变更.md`\n- [规范] 示例标题\n  - 详情：`规范变更.md`\n- [Bug] 示例标题\n  - 详情：`bug修复.md`\n",
    "需求变更.md": "# 需求变更\n\n## 模板示例 需求标题\n\n- 背景：\n  说明本次需求变更是在什么背景下提出的。\n\n- 变更内容：\n  说明需求新增、删减或口径调整的具体内容。\n\n- 影响范围：\n  说明会影响哪些模块、页面、接口或测试。\n\n- 涉及文件：\n  `code/frontend/...`\n  `code/backend/...`\n\n- 决策原因：\n  说明为什么要这样调整，而不是采用其他方案。\n\n- 风险或兼容性：\n  说明是否会影响历史行为、测试、数据或兼容性。\n\n- 备注：\n  可选补充。\n",
    "架构变更.md": "# 架构变更\n\n## 模板示例 架构标题\n\n- 背景：\n  说明为什么当前架构或目录结构需要调整。\n\n- 变更内容：\n  说明模块边界、目录结构、部署结构或接口边界的变化。\n\n- 影响范围：\n  说明受影响的模块、脚本、文档或协作方式。\n\n- 涉及文件：\n  `AGENTS.md`\n  `docs/...`\n  `code/...`\n\n- 决策原因：\n  说明为什么采用当前结构，而不是其他结构。\n\n- 风险或兼容性：\n  说明对旧路径、旧接口、旧部署流程是否有影响。\n\n- 备注：\n  可选补充。\n",
    "规范变更.md": "# 规范变更\n\n## 模板示例 规范标题\n\n- 背景：\n  说明为什么需要新增或调整规范。\n\n- 变更内容：\n  说明命名、目录、接口、提交、测试等规范的具体变化。\n\n- 影响范围：\n  说明哪些开发活动或模块会受到影响。\n\n- 涉及文件：\n  `AGENTS.md`\n  `docs/...`\n  `code/backend/...`\n  `code/frontend/...`\n\n- 决策原因：\n  说明这样约定的原因和目标。\n\n- 风险或兼容性：\n  说明是否只影响新增内容，或是否需要迁移存量内容。\n\n- 备注：\n  可选补充。\n",
    "bug修复.md": "# Bug 修复\n\n## 模板示例 Bug 标题\n\n- 问题现象：\n  说明用户看到的问题、触发条件和表现。\n\n- 根因分析：\n  说明问题的真实原因，避免只写表面症状。\n\n- 修复内容：\n  说明采取了什么修复措施。\n\n- 影响范围：\n  说明会影响哪些模块、页面、流程或接口。\n\n- 涉及文件：\n  `code/frontend/...`\n\n- 决策原因：\n  说明为什么采用当前修复方案。\n\n- 风险或兼容性：\n  说明需要关注的回归测试和兼容性问题。\n\n- 备注：\n  可选补充。\n",
}


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Clean scaffold artifacts and optional demo residue."
    )
    parser.add_argument("root", nargs="?", default=".", help="Project root.")
    parser.add_argument(
        "--execute",
        action="store_true",
        help="Actually perform the cleanup. Defaults to dry-run.",
    )
    parser.add_argument(
        "--dry-run",
        action="store_true",
        help="Preview cleanup actions without changing files.",
    )
    parser.add_argument(
        "--include-demo-docs",
        action="store_true",
        help="Also remove demo docs under docs/superpowers.",
    )
    parser.add_argument(
        "--include-demo-datasql",
        action="store_true",
        help="Also remove demo DataSQL placeholder directories like A业务.",
    )
    parser.add_argument(
        "--include-test-reports",
        action="store_true",
        help="Also remove generated backend test-report directories.",
    )
    parser.add_argument(
        "--archive-change-records",
        action="store_true",
        help="Archive docs/变更记录 detailed files, then recreate a clean skeleton.",
    )
    return parser.parse_args()


def expand_globs(root: Path, patterns: list[str]) -> list[Path]:
    matches: set[Path] = set()
    for pattern in patterns:
        matches.update(root.glob(pattern))
    return sorted(path for path in matches if path.exists())


def remove_path(path: Path, execute: bool):
    action = "REMOVE"
    print(f"{action}: {path}")
    if not execute:
        return
    if path.is_dir():
        shutil.rmtree(path)
    else:
        path.unlink()


def archive_change_records(root: Path, execute: bool):
    existing_files = [root / rel_path for rel_path in CHANGE_RECORD_FILES if (root / rel_path).exists()]
    if not existing_files:
        print("ARCHIVE: no change-record files found.")
        return

    timestamp = datetime.now().strftime("%Y%m%d-%H%M%S")
    archive_root = root / "docs" / "历史归档" / f"项目初始化前-{timestamp}" / "变更记录"
    print(f"ARCHIVE: {archive_root}")

    if execute:
        archive_root.mkdir(parents=True, exist_ok=True)

    for file_path in existing_files:
        print(f"  MOVE: {file_path}")
        if execute:
            target = archive_root / file_path.name
            shutil.move(str(file_path), str(target))

    if execute:
        change_dir = root / "docs" / "变更记录"
        change_dir.mkdir(parents=True, exist_ok=True)
        for file_name, content in CHANGE_RECORD_TEMPLATE.items():
            (change_dir / file_name).write_text(content, encoding="utf-8")


def main():
    args = parse_args()
    root = Path(args.root).resolve()
    execute = args.execute and not args.dry_run

    print("Scaffold Cleanup")
    print("================")
    print(f"Root: {root}")
    print(f"Mode: {'execute' if execute else 'dry-run'}")

    targets = expand_globs(root, ARTIFACT_GLOBS)
    if args.include_test_reports:
        targets.extend(expand_globs(root, REPORT_GLOBS))
    if args.include_demo_docs:
        targets.extend(expand_globs(root, DEMO_DOC_GLOBS))
    if args.include_demo_datasql:
        targets.extend(expand_globs(root, DEMO_DATASQL_GLOBS))

    seen: set[Path] = set()
    unique_targets: list[Path] = []
    for path in sorted(targets):
        if path not in seen:
            unique_targets.append(path)
            seen.add(path)

    if not unique_targets and not args.archive_change_records:
        print("No cleanup targets selected.")
        return

    for path in unique_targets:
        remove_path(path, execute=execute)

    if args.archive_change_records:
        archive_change_records(root, execute=execute)

    print()
    print("Done.")


if __name__ == "__main__":
    main()
