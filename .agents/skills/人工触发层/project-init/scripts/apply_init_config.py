#!/usr/bin/env python3
"""Apply scaffold init config in dry-run or execute mode."""

from __future__ import annotations

import argparse
import json
import os
import shutil
from pathlib import Path, PurePosixPath

import cleanup_scaffold


PROTECTED_ASSET_PATHS = [
    ".agents/skills",
    ".agents/description",
    "docs/规范/AI开发SOP.md",
    "docs/规范",
]

SKIP_DIR_NAMES = {
    ".cache",
    ".git",
    ".idea",
    ".mvn",
    ".turbo",
    ".vite",
    ".vite-temp",
    "__pycache__",
    "coverage",
    "dist",
    "node_modules",
    "target",
}

TEXT_FILE_SUFFIXES = {
    ".java",
    ".kt",
    ".xml",
    ".yaml",
    ".yml",
    ".properties",
    ".sql",
    ".md",
    ".txt",
    ".ts",
    ".tsx",
    ".js",
    ".jsx",
    ".vue",
    ".json",
    ".html",
    ".css",
    ".less",
    ".env",
    ".gitignore",
    ".gitattributes",
    ".sh",
    ".ps1",
    ".cmd",
    ".bat",
    ".py",
}

MAX_FILE_SIZE = 1_000_000


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Apply project-init config to scaffold files."
    )
    parser.add_argument("config", help="Path to init config JSON.")
    parser.add_argument("root", nargs="?", default=".", help="Project root.")
    parser.add_argument(
        "--execute",
        action="store_true",
        help="Actually apply changes. Defaults to dry-run.",
    )
    parser.add_argument(
        "--dry-run",
        action="store_true",
        help="Preview changes without writing files.",
    )
    return parser.parse_args()


def rel_posix(path: Path, root: Path) -> str:
    return path.relative_to(root).as_posix()


def normalize_rel_path(path: str) -> str:
    return path.replace("\\", "/").strip("/")


def is_same_or_child(rel_path: str, target: str) -> bool:
    rel_parts = PurePosixPath(rel_path).parts
    target_parts = PurePosixPath(target).parts
    return len(rel_parts) >= len(target_parts) and rel_parts[: len(target_parts)] == target_parts


def is_protected(rel_path: str) -> bool:
    return any(is_same_or_child(rel_path, target) for target in PROTECTED_ASSET_PATHS)


def should_scan_file(path: Path) -> bool:
    if not path.is_file():
        return False
    if path.stat().st_size > MAX_FILE_SIZE:
        return False
    return path.suffix.lower() in TEXT_FILE_SUFFIXES or path.name in {
        "AGENTS.md",
        "pom.xml",
        "package.json",
        "package-lock.json",
    }


def read_text(path: Path) -> tuple[str, str]:
    for encoding in ("utf-8", "utf-8-sig", "gbk"):
        try:
            return path.read_text(encoding=encoding), encoding
        except UnicodeDecodeError:
            continue
    raise UnicodeDecodeError("unknown", b"", 0, 1, f"Unable to decode {path}")


def iter_text_files(root: Path, include_roots: list[str] | None, allow_protected: bool):
    for current_root, dir_names, file_names in os.walk(root):
        dir_names[:] = [name for name in dir_names if name not in SKIP_DIR_NAMES]
        current_path = Path(current_root)
        for file_name in file_names:
            path = current_path / file_name
            if not should_scan_file(path):
                continue
            rel_path = rel_posix(path, root)
            if include_roots and not any(
                is_same_or_child(rel_path, include_root) for include_root in include_roots
            ):
                continue
            if not allow_protected and is_protected(rel_path):
                continue
            yield path


def normalize_replacements(items: list[dict]) -> list[tuple[str, str]]:
    pairs: list[tuple[str, str]] = []
    for item in items:
        from_text = item.get("from", "")
        to_text = item.get("to", "")
        if not from_text or from_text == to_text:
            continue
        pairs.append((from_text, to_text))
    return pairs


def apply_replacements_to_file(path: Path, replacements: list[tuple[str, str]], execute: bool):
    content, encoding = read_text(path)
    updated = content
    replacement_count = 0
    for from_text, to_text in replacements:
        count = updated.count(from_text)
        if count:
            updated = updated.replace(from_text, to_text)
            replacement_count += count
    if replacement_count == 0:
        return 0
    print(f"UPDATE: {path} ({replacement_count} replacements)")
    if execute:
        path.write_text(updated, encoding=encoding)
    return replacement_count


def apply_text_replacements(
    root: Path,
    replacements: list[tuple[str, str]],
    include_roots: list[str] | None,
    allow_protected: bool,
    execute: bool,
) -> tuple[int, int]:
    changed_files = 0
    total_replacements = 0
    for path in iter_text_files(root, include_roots=include_roots, allow_protected=allow_protected):
        replacement_count = apply_replacements_to_file(path, replacements, execute=execute)
        if replacement_count:
            changed_files += 1
            total_replacements += replacement_count
    return changed_files, total_replacements


def remap_relative_path(path_rel: str, applied_renames: list[tuple[str, str]]) -> str:
    for old_prefix, new_prefix in reversed(applied_renames):
        if is_same_or_child(path_rel, old_prefix):
            path_parts = PurePosixPath(path_rel).parts
            prefix_parts = PurePosixPath(old_prefix).parts
            suffix_parts = path_parts[len(prefix_parts):]
            return str(PurePosixPath(new_prefix, *suffix_parts))
    return path_rel


def prune_empty_parent_dirs(start: Path, stop: Path):
    current = start
    while current.exists() and current != stop:
        if any(current.iterdir()):
            break
        current.rmdir()
        current = current.parent


def apply_path_renames(root: Path, renames: list[dict], execute: bool):
    ordered = sorted(
        renames,
        key=lambda item: (
            len(PurePosixPath(normalize_rel_path(item.get("from", ""))).parts),
            len(normalize_rel_path(item.get("from", ""))),
        ),
    )
    changed = 0
    applied_renames: list[tuple[str, str]] = []
    for item in ordered:
        from_rel = normalize_rel_path(item.get("from", ""))
        to_rel = normalize_rel_path(item.get("to", ""))
        if not from_rel or not to_rel or from_rel == to_rel:
            continue
        resolved_from_rel = from_rel
        if execute or not (root / from_rel).exists():
            resolved_from_rel = remap_relative_path(from_rel, applied_renames)

        source = root / resolved_from_rel
        target = root / to_rel
        if not source.exists():
            print(f"SKIP RENAME: missing source {source}")
            continue
        if target.exists():
            print(f"SKIP RENAME: target exists {target}")
            continue
        print(f"RENAME: {source} -> {target}")
        changed += 1
        if execute:
            target.parent.mkdir(parents=True, exist_ok=True)
            shutil.move(str(source), str(target))
            prune_empty_parent_dirs(source.parent, root)
        applied_renames.append((from_rel, to_rel))
    return changed


def format_list(items: list[str]) -> str:
    if not items:
        return "-"
    return "\n".join(f"- {item}" for item in items)


def render_init_document(facts: dict) -> str:
    return f"""# 项目初始化信息

## 项目身份
- 中文名：{facts.get('project_name_cn', '')}
- 英文名：{facts.get('project_name_en', '')}
- 简称：{facts.get('project_code', '')}
- 后端包根：{facts.get('backend_package', '')}
- 业务模块名：{facts.get('app_module_name', '')}
- 业务模块物理目录名：{facts.get('app_directory_name', '')}

## 技术基线
- JDK：{facts.get('jdk_version', '')}
- Maven：{facts.get('maven_version', '')}
- Spring Boot：{facts.get('spring_boot_version', '')}
- Spring Cloud：{facts.get('spring_cloud_version', '')}
- Spring Cloud Alibaba：{facts.get('spring_cloud_alibaba_version', '')}
- MyBatis-Plus：{facts.get('mybatis_plus_version', '')}
- Node：{facts.get('node_version', '')}
- 默认数据库：{facts.get('database_platform', '')}
- 保留环境 profile：
{format_list(facts.get('retained_env_profiles', facts.get('retained_db_profiles', [])))}
- 保留数据库 profile：
{format_list(facts.get('retained_database_profiles', []))}
- 依赖矩阵来源：{facts.get('dependency_matrix_source', '')}
- 中间件和数据库驱动版本：
{format_list(facts.get('middleware_versions', []))}

## 运行与前端项目名称
- spring.application.name：{facts.get('spring_application_name', '')}
- 服务名：{facts.get('service_name', '')}
- 配置中心 dataId：{facts.get('config_data_id', '')}
- 数据库名：{facts.get('database_name', '')}
- 表名前缀：{facts.get('table_prefix', '')}
- 前端标题：{facts.get('frontend_title', '')}
- API 前缀：{facts.get('api_prefix', '')}

## 初始化策略
- 保留共享模块名：{facts.get('preserve_shared_module_names', '')}
- 保留最小演示链路：{facts.get('preserve_demo_chain', '')}
- 保留字典管理示例：{facts.get('preserve_dictionary_demo', '')}
- 文档历史处理策略：{facts.get('doc_history_strategy', '')}

## 备注
{format_list(facts.get('notes', []))}
"""


def write_init_document(root: Path, init_document: dict, execute: bool):
    if not init_document.get("enabled", False):
        return False
    target_rel = init_document.get("path", "docs/项目信息/项目初始化信息.md")
    target = root / target_rel
    content = render_init_document(init_document.get("facts", {}))
    print(f"WRITE: {target}")
    if execute:
        target.parent.mkdir(parents=True, exist_ok=True)
        target.write_text(content, encoding="utf-8")
    return True


def run_cleanup(root: Path, cleanup_config: dict, execute: bool):
    if not cleanup_config.get("enabled", False):
        return

    targets = cleanup_scaffold.expand_globs(root, cleanup_scaffold.ARTIFACT_GLOBS)
    if cleanup_config.get("include_test_reports", False):
        targets.extend(cleanup_scaffold.expand_globs(root, cleanup_scaffold.REPORT_GLOBS))
    if cleanup_config.get("include_demo_docs", False):
        targets.extend(cleanup_scaffold.expand_globs(root, cleanup_scaffold.DEMO_DOC_GLOBS))
    if cleanup_config.get("include_demo_datasql", False):
        targets.extend(cleanup_scaffold.expand_globs(root, cleanup_scaffold.DEMO_DATASQL_GLOBS))

    seen: set[Path] = set()
    print("CLEANUP:")
    for path in sorted(targets):
        if path in seen:
            continue
        seen.add(path)
        cleanup_scaffold.remove_path(path, execute=execute)

    if cleanup_config.get("archive_change_records", False):
        cleanup_scaffold.archive_change_records(root, execute=execute)


def scan_residual_tokens(root: Path, tokens: list[str]) -> int:
    normalized_tokens = [token for token in tokens if token]
    if not normalized_tokens:
        return 0

    hits = 0
    print()
    print("Residual token scan")
    print("-------------------")
    for path in iter_text_files(root, include_roots=None, allow_protected=True):
        rel_path = rel_posix(path, root)
        try:
            content, _ = read_text(path)
        except UnicodeDecodeError:
            continue
        matched_tokens = [token for token in normalized_tokens if token in content]
        if matched_tokens:
            hits += 1
            print(f"RESIDUAL: {rel_path} -> {', '.join(matched_tokens)}")
    if hits == 0:
        print("No residual tokens found.")
    return hits


def load_config(path: Path) -> dict:
    return json.loads(path.read_text(encoding="utf-8"))


def split_protected_targets(targets: list[str]) -> tuple[list[str], list[str]]:
    protected: list[str] = []
    non_protected: list[str] = []
    for target in targets:
        if is_protected(target):
            protected.append(target)
        else:
            non_protected.append(target)
    return protected, non_protected


def main():
    args = parse_args()
    root = Path(args.root).resolve()
    config_path = Path(args.config).resolve()
    execute = args.execute and not args.dry_run
    config = load_config(config_path)

    print("Apply Project Init Config")
    print("=========================")
    print(f"Root: {root}")
    print(f"Config: {config_path}")
    print(f"Mode: {'execute' if execute else 'dry-run'}")

    rename_count = apply_path_renames(root, config.get("path_renames", []), execute=execute)

    global_replacements = normalize_replacements(config.get("global_replacements", []))
    protected_fact_replacements = normalize_replacements(
        config.get("protected_fact_replacements", [])
    )

    global_changed_files, global_replacement_count = apply_text_replacements(
        root,
        replacements=global_replacements,
        include_roots=config.get("global_include_roots"),
        allow_protected=False,
        execute=execute,
    )

    protected_changed_files = 0
    protected_replacement_count = 0
    protected_targets, non_protected_targets = split_protected_targets(
        config.get("protected_fact_targets", [])
    )
    for target in non_protected_targets:
        print(
            "WARN: protected_fact_targets contains non-protected path, "
            f"skip special handling: {target}"
        )
    if protected_fact_replacements and protected_targets:
        protected_changed_files, protected_replacement_count = apply_text_replacements(
            root,
            replacements=protected_fact_replacements,
            include_roots=protected_targets,
            allow_protected=True,
            execute=execute,
        )

    init_doc_written = write_init_document(
        root, init_document=config.get("init_document", {}), execute=execute
    )

    run_cleanup(root, cleanup_config=config.get("cleanup", {}), execute=execute)

    residual_hit_count: int | None = None
    if execute:
        residual_hit_count = scan_residual_tokens(
            root,
            tokens=config.get(
                "residual_scan_tokens",
                [
                    "com.hzzenith.ai.training",
                    "com/hzzenith/ai/training",
                    "ai-training-project",
                    "process-ex",
                    "Process-Ex",
                    "ai_drill_project",
                    "'PE'",
                ],
            ),
        )
    else:
        print()
        print("Residual token scan")
        print("-------------------")
        print("Skipped in dry-run mode. Run with --execute to scan rewritten files.")

    print()
    print("Summary")
    print("-------")
    print(f"Path renames: {rename_count}")
    print(
        "Global text replacements: "
        f"{global_replacement_count} across {global_changed_files} files"
    )
    print(
        "Protected fact replacements: "
        f"{protected_replacement_count} across {protected_changed_files} files"
    )
    print(f"Init document planned: {'yes' if init_doc_written else 'no'}")
    print(
        "Residual files: "
        f"{residual_hit_count if residual_hit_count is not None else 'not scanned'}"
    )


if __name__ == "__main__":
    main()
