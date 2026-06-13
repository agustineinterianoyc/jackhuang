#!/usr/bin/env python3
"""Scan scaffold placeholders, demo residue, and local artifacts for project init."""

from __future__ import annotations

import argparse
import os
from pathlib import Path


PLACEHOLDER_TOKENS = [
    "com.hzzenith.ai.training",
    "com/hzzenith/ai/training",
    "com\\hzzenith\\ai\\training",
    "ai-training-project",
    "process-ex",
    "Process-Ex",
    "ai_drill_project",
    "'PE'",
    "A业务",
]

DEMO_TOKENS = [
    "CommonController",
    "CommonService",
    "DemoMessage",
    "DictController",
    "DictService",
    "DictTypeMapper",
    "DictEntryMapper",
    "DICT_TYPE_CODE_DUPLICATE",
    "DICT_TYPE_NOT_FOUND",
    "字典管理",
    "公共基础",
]

ARTIFACT_DIR_NAMES = {".cache", ".idea", ".turbo", "coverage", "dist", "node_modules", "target"}
SKIP_DIR_NAMES = {
    ".git",
    ".mvn",
    ".vite",
    ".vite-temp",
    "__pycache__",
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
MAX_MATCHES_PER_FILE = 3
PROJECT_INIT_SKILL_PREFIX = ".agents/skills/人工触发层/project-init"
PROTECTED_ASSET_PREFIXES = (
    ".agents/skills",
    ".agents/description",
    "docs/规范/AI开发SOP.md",
    "docs/规范",
)


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Scan this scaffold for init placeholders and removable residue."
    )
    parser.add_argument(
        "root",
        nargs="?",
        default=".",
        help="Project root to scan. Defaults to the current directory.",
    )
    parser.add_argument(
        "--token",
        action="append",
        default=[],
        help="Additional token to scan for. Can be provided multiple times.",
    )
    return parser.parse_args()


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


def find_token_matches(path: Path, token: str) -> list[int]:
    try:
        content = path.read_text(encoding="utf-8")
    except UnicodeDecodeError:
        try:
            content = path.read_text(encoding="utf-8-sig")
        except UnicodeDecodeError:
            return []

    matches: list[int] = []
    for index, line in enumerate(content.splitlines(), start=1):
        if token in line:
            matches.append(index)
            if len(matches) >= MAX_MATCHES_PER_FILE:
                break
    return matches


def iter_files(root: Path):
    for current_root, dir_names, file_names in os.walk(root):
        dir_names[:] = [
            name
            for name in dir_names
            if name not in SKIP_DIR_NAMES and name not in ARTIFACT_DIR_NAMES
        ]
        current_path = Path(current_root)
        for file_name in file_names:
            path = current_path / file_name
            if should_scan_file(path):
                yield path


def relative(path: Path, root: Path) -> str:
    return path.relative_to(root).as_posix()


def hit_category(file_path: str) -> str:
    if file_path.startswith(PROJECT_INIT_SKILL_PREFIX):
        return "Skill self/reference hits"
    if file_path == "docs/规范/AI开发SOP.md" or any(
        file_path.startswith(f"{prefix}/") for prefix in PROTECTED_ASSET_PREFIXES
    ):
        return "Protected template fact hits"
    return "Project file hits"


def collect_artifacts(root: Path) -> list[str]:
    artifacts: list[str] = []
    for current_root, dir_names, _ in os.walk(root):
        current_path = Path(current_root)
        for dir_name in list(dir_names):
            path = current_path / dir_name
            if dir_name in ARTIFACT_DIR_NAMES:
                artifacts.append(relative(path, root))
                dir_names.remove(dir_name)
            if dir_name in SKIP_DIR_NAMES:
                dir_names.remove(dir_name)
    return sorted(set(artifacts))


def scan_tokens(root: Path, tokens: list[str]) -> dict[str, list[tuple[str, list[int]]]]:
    results: dict[str, list[tuple[str, list[int]]]] = {token: [] for token in tokens}
    files = list(iter_files(root))
    for token in tokens:
        for file_path in files:
            match_lines = find_token_matches(file_path, token)
            if match_lines:
                results[token].append((relative(file_path, root), match_lines))
    return results


def print_section(title: str):
    print()
    print(title)
    print("-" * len(title))


def print_token_results(title: str, results: dict[str, list[tuple[str, list[int]]]]):
    print_section(title)
    any_hit = False
    for token, hits in results.items():
        if not hits:
            continue
        any_hit = True
        print(f"* {token} ({len(hits)} files)")
        grouped_hits: dict[str, list[tuple[str, list[int]]]] = {}
        for file_path, lines in hits:
            grouped_hits.setdefault(hit_category(file_path), []).append((file_path, lines))
        for category, category_hits in grouped_hits.items():
            print(f"  {category}:")
            for file_path, lines in category_hits:
                line_text = ", ".join(str(line) for line in lines)
                print(f"    - {file_path}: lines {line_text}")
    if not any_hit:
        print("No matches found.")


def main():
    args = parse_args()
    root = Path(args.root).resolve()

    placeholder_tokens = PLACEHOLDER_TOKENS + args.token
    demo_tokens = DEMO_TOKENS

    print("Project Init Scan")
    print("=================")
    print(f"Root: {root}")

    artifacts = collect_artifacts(root)
    print_section("Generated/local artifact directories")
    if artifacts:
        for item in artifacts:
            print(f"- {item}")
    else:
        print("No artifact directories found.")

    placeholder_results = scan_tokens(root, placeholder_tokens)
    demo_results = scan_tokens(root, demo_tokens)

    print_token_results("Placeholder identifiers", placeholder_results)
    print_token_results("Demo residue candidates", demo_results)

    print_section("Suggested next actions")
    print("1. Confirm which demo chains should be removed versus retained as template capability.")
    print("2. Collect project facts in batches before renaming package/module/app identifiers.")
    print("3. Clean artifact directories before running validation after the init rewrite.")


if __name__ == "__main__":
    main()
