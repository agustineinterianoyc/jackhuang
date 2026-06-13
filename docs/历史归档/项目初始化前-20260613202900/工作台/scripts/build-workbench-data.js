const fs = require("node:fs")
const path = require("node:path")

const projectRoot = path.resolve(__dirname, "..", "..", "..")
const docsRoot = path.join(projectRoot, "docs")
const requirementRoot = path.join(docsRoot, "需求")
const prototypeRoot = path.join(docsRoot, "原型")
const workbenchDataFile = path.join(projectRoot, "docs", "工作台", "data", "workbench-data.js")

function readText(filePath) {
  return fs.readFileSync(filePath, "utf8")
}

function writeText(filePath, content) {
  fs.mkdirSync(path.dirname(filePath), { recursive: true })
  fs.writeFileSync(filePath, content, "utf8")
}

function normalizeCell(value) {
  return value
    .trim()
    .replaceAll("`", "")
}

function normalizeRoleFields(row) {
  if (!row || typeof row !== "object") {
    return row
  }
  if (!row["当前责任角色"] && row["当前责任模型"]) {
    row["当前责任角色"] = row["当前责任模型"]
  }
  if (!row["下一责任角色"] && row["下一责任模型"]) {
    row["下一责任角色"] = row["下一责任模型"]
  }
  if (!row["复核角色 / 工具"] && row["复核模型 / 工具"]) {
    row["复核角色 / 工具"] = row["复核模型 / 工具"]
  }
  if (!row["执行角色 / 工具"] && row["执行模型 / 工具"]) {
    row["执行角色 / 工具"] = row["执行模型 / 工具"]
  }
  return row
}

function parseFrontmatter(content) {
  const match = content.match(/^---\r?\n([\s\S]*?)\r?\n---/)
  if (!match) {
    return {}
  }

  const result = {}
  match[1]
    .split(/\r?\n/)
    .map((line) => line.trim())
    .filter(Boolean)
    .forEach((line) => {
      const index = line.indexOf(":")
      if (index === -1) {
        return
      }
      const key = line.slice(0, index).trim()
      let value = line.slice(index + 1).trim()
      if (value.startsWith("[") && value.endsWith("]")) {
        value = value
          .slice(1, -1)
          .split(",")
          .map((item) => item.trim())
          .filter(Boolean)
        result[key] = value
        return
      }
      result[key] = value
    })
  return result
}

function splitTableRow(row) {
  return row
    .trim()
    .replace(/^\|/, "")
    .replace(/\|$/, "")
    .split("|")
    .map((cell) => normalizeCell(cell))
}

function parseTable(lines, startIndex) {
  const header = splitTableRow(lines[startIndex])
  const rows = []

  for (let i = startIndex + 2; i < lines.length; i += 1) {
    const line = lines[i].trim()
    if (!line.startsWith("|")) {
      break
    }
    const cells = splitTableRow(line)
    const row = {}
    header.forEach((column, index) => {
      row[column] = cells[index] || ""
    })
    rows.push(normalizeRoleFields(row))
  }

  return rows
}

function findTableByHeading(content, headingKeyword) {
  const lines = content.split(/\r?\n/)
  const headingIndex = lines.findIndex((line) => line.trim().startsWith("#") && line.includes(headingKeyword))
  if (headingIndex === -1) {
    return []
  }

  for (let i = headingIndex + 1; i < lines.length; i += 1) {
    const line = lines[i].trim()
    if (line.startsWith("|") && lines[i + 1] && lines[i + 1].includes("---")) {
      return parseTable(lines, i)
    }
    if (line.startsWith("#")) {
      break
    }
  }

  return []
}

function extractBullet(content, prefix) {
  const line = content
    .split(/\r?\n/)
    .find((item) => item.trim().startsWith(`- ${prefix}：`))
  if (!line) {
    return ""
  }
  return line.split("：").slice(1).join("：").trim()
}

function toNumber(value) {
  const parsed = Number.parseInt(String(value || "").replace(/[^\d]/g, ""), 10)
  return Number.isNaN(parsed) ? 0 : parsed
}

function listDirectories(directory) {
  if (!fs.existsSync(directory)) {
    return []
  }
  return fs
    .readdirSync(directory, { withFileTypes: true })
    .filter((entry) => entry.isDirectory())
    .map((entry) => entry.name)
}

function parsePackage(packageName) {
  const packageDir = path.join(requirementRoot, "需求包", packageName)
  const readmePath = path.join(packageDir, "README.md")
  const planPath = path.join(packageDir, "实现计划.md")
  const progressPath = path.join(packageDir, "实现进度.md")

  const readmeContent = readText(readmePath)
  const progressContent = readText(progressPath)

  const frontmatter = parseFrontmatter(readmeContent)
  const overviewRows = findTableByHeading(progressContent, "进度总览")
  const stepRows = findTableByHeading(progressContent, "分步骤进度")
  const riskRows = findTableByHeading(progressContent, "风险与阻塞")

  const overview = overviewRows[0] || {}
  const totalSteps = toNumber(overview["总步骤数"])
  const completedSteps = toNumber(overview["已完成"])
  const progressPercent = totalSteps > 0 ? Math.round((completedSteps / totalSteps) * 100) : 0
  const currentStep =
    stepRows.find((row) => !["已完成", "已取消"].includes(row["当前状态"])) ||
    stepRows[stepRows.length - 1] ||
    {}

  return {
    id: packageName,
    status: frontmatter.status || overview["当前状态"] || "待补充",
    changeType: frontmatter.change_type || "待补充",
    prototypeBatch: frontmatter.prototype_batch || "待补充",
    modules: frontmatter.modules || [],
    goal: extractBullet(readmeContent, "本次要解决的问题"),
    totalSteps,
    completedSteps,
    progressPercent,
    latestUpdate: overview["最近更新时间"] || "待补充",
    currentStep,
    stepRows,
    risks: riskRows,
    links: [
      { label: "README", href: `../需求/需求包/${packageName}/README.md` },
      { label: "实现计划", href: `../需求/需求包/${packageName}/实现计划.md` },
      { label: "实现进度", href: `../需求/需求包/${packageName}/实现进度.md` },
    ],
  }
}

function parseModuleSummary() {
  const moduleTableContent = readText(path.join(requirementRoot, "模块总表.md"))
  const rows = findTableByHeading(moduleTableContent, "模块总表")
  return rows
    .filter((row) => row["模块中文名"] && row["模块中文名"] !== "暂无" && row["模块编码"])
    .map((row) => ({
      name: row["模块中文名"],
      code: row["模块编码"],
      status: row["当前状态"],
      latestPackage: row["主要需求包"],
      prototypeBatch: row["涉及原型"],
      note: row["说明"],
      readme: `../需求/模块/${row["模块中文名"]}/README.md`,
    }))
}

function parsePrototypes() {
  return listDirectories(prototypeRoot).map((batch) => {
    const readmePath = path.join(prototypeRoot, batch, "README.md")
    const requirementBatch = batch.replace("原型", "需求")
    let note = "待补充"
    if (fs.existsSync(readmePath)) {
      const content = readText(readmePath)
      note = content.split(/\r?\n/).find((line) => line.trim() && !line.startsWith("#"))?.trim() || note
    }
    return {
      batch,
      requirementBatch,
      note,
      readme: `../原型/${batch}/README.md`,
      requirement: `../需求/原始需求/${requirementBatch}/README.md`,
    }
  })
}

function buildSummary(packages) {
  const totalPackages = packages.length
  const blockedPackages = packages.filter((item) => item.status.includes("阻塞")).length
  const activePackages = packages.filter((item) => !item.status.includes("已完成")).length
  const totalSteps = packages.reduce((sum, item) => sum + item.totalSteps, 0)
  const completedSteps = packages.reduce((sum, item) => sum + item.completedSteps, 0)
  const overallProgressPercent = totalSteps > 0 ? Math.round((completedSteps / totalSteps) * 100) : 0
  const statusMap = new Map()

  packages.forEach((item) => {
    statusMap.set(item.status, (statusMap.get(item.status) || 0) + 1)
  })

  const packageStatusBreakdown = [...statusMap.entries()].map(([label, count]) => ({
    label,
    count,
    percent: totalPackages > 0 ? Math.round((count / totalPackages) * 100) : 0,
  }))

  return {
    totalPackages,
    activePackages,
    blockedPackages,
    totalSteps,
    completedSteps,
    overallProgressPercent,
    packageStatusBreakdown,
  }
}

function buildWorkbenchData() {
  const packageDirectories = listDirectories(path.join(requirementRoot, "需求包"))
  const packages = packageDirectories
    .filter((name) => fs.existsSync(path.join(requirementRoot, "需求包", name, "README.md")))
    .map(parsePackage)
    .sort((left, right) => right.id.localeCompare(left.id))

  return {
    generatedAt: new Date().toISOString().slice(0, 19).replace("T", " "),
    updatedAt: packages
      .map((item) => item.latestUpdate)
      .filter(Boolean)
      .sort()
      .at(-1) || "",
    quickLinks: [
      { title: "需求总入口", href: "../需求/readme.md" },
      { title: "模块总表", href: "../需求/模块总表.md" },
      { title: "原型输入目录", href: "../原型/readme.md" },
    ],
    summary: buildSummary(packages),
    packages,
    modules: parseModuleSummary(),
    prototypes: parsePrototypes(),
  }
}

function main() {
  const data = buildWorkbenchData()
  const content = `window.WORKBENCH_DATA = ${JSON.stringify(data, null, 2)}\n`
  writeText(workbenchDataFile, content)
  console.log(`Workbench data generated: ${workbenchDataFile}`)
}

main()
