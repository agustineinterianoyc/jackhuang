const statusFilter = document.getElementById("status-filter")
const changeFilter = document.getElementById("change-filter")
const packageTable = document.getElementById("package-table")
const refreshButton = document.getElementById("refresh-data")
const autoRefreshCheckbox = document.getElementById("auto-refresh")

let workbenchData = {
  generatedAt: "",
  updatedAt: "",
  quickLinks: [],
  summary: {},
  packages: [],
  modules: [],
  prototypes: [],
}

let refreshTimer = null

function readField(row, ...keys) {
  for (const key of keys) {
    if (row?.[key]) {
      return row[key]
    }
  }
  return ""
}

function createOptions(select, values, defaultLabel) {
  if (!select) {
    return
  }
  const previousValue = select.value
  const options = [`<option value="">${defaultLabel}</option>`]
  values.filter(Boolean).forEach((value) => {
    options.push(`<option value="${value}">${value}</option>`)
  })
  select.innerHTML = options.join("")
  if (values.includes(previousValue)) {
    select.value = previousValue
  }
}

function getChipClass(value) {
  if (!value) {
    return "chip"
  }
  if (value.includes("阻塞")) {
    return "chip danger"
  }
  if (value.includes("完成")) {
    return "chip success"
  }
  if (value.includes("待") || value.includes("阶段") || value.includes("持续")) {
    return "chip warning"
  }
  return "chip"
}

function getStatusClass(value) {
  if (!value) {
    return ""
  }
  if (value.includes("阻塞")) {
    return "danger"
  }
  if (value.includes("完成")) {
    return "success"
  }
  if (value.includes("待") || value.includes("阶段")) {
    return "warning"
  }
  return ""
}

function escapeHtml(value) {
  return String(value ?? "")
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#39;")
}

async function loadWorkbenchData() {
  const scriptId = "workbench-data-script"
  const previousScript = document.getElementById(scriptId)
  if (previousScript) {
    previousScript.remove()
  }
  delete window.WORKBENCH_DATA

  await new Promise((resolve, reject) => {
    const script = document.createElement("script")
    script.id = scriptId
    script.src = `data/workbench-data.js?v=${Date.now()}`
    script.onload = resolve
    script.onerror = reject
    document.body.appendChild(script)
  })

  workbenchData = window.WORKBENCH_DATA || workbenchData
}

function renderQuickLinks() {
  const container = document.getElementById("quick-links")
  if (!container) {
    return
  }
  container.innerHTML = (workbenchData.quickLinks || [])
    .map((item) => `<a href="${escapeHtml(item.href)}">${escapeHtml(item.title)}</a>`)
    .join("")
}

function renderSummary() {
  const updatedAt = document.getElementById("updated-at")
  const buildAt = document.getElementById("build-at")
  if (updatedAt) {
    updatedAt.textContent = `业务数据最近更新时间：${workbenchData.updatedAt || "待补充"}`
  }
  if (buildAt) {
    buildAt.textContent = `工作台构建时间：${workbenchData.generatedAt || "待补充"}`
  }

  const summary = workbenchData.summary || {}
  const percent = summary.overallProgressPercent || 0

  const overallProgress = document.getElementById("overall-progress")
  if (overallProgress) {
    overallProgress.innerHTML = `
    <div class="progress-ring" style="--progress:${percent}">
      <div class="progress-ring-inner">
        <span class="progress-ring-value">${percent}%</span>
        <span class="progress-ring-label">总步骤完成率</span>
      </div>
    </div>
    <div class="summary-cards">
      <article class="summary-card">
        <strong>${summary.totalPackages || 0}</strong>
        <span>正式需求包</span>
      </article>
      <article class="summary-card">
        <strong>${summary.activePackages || 0}</strong>
        <span>进行中 / 持续维护</span>
      </article>
      <article class="summary-card">
        <strong>${summary.blockedPackages || 0}</strong>
        <span>阻塞需求包</span>
      </article>
      <article class="summary-card">
        <strong>${summary.completedSteps || 0}/${summary.totalSteps || 0}</strong>
        <span>已完成步骤 / 总步骤</span>
      </article>
    </div>
  `
  }

  const statusChart = document.getElementById("status-chart")
  if (statusChart) {
    statusChart.innerHTML = (summary.packageStatusBreakdown || [])
      .map(
        (item) => `
        <div class="status-item ${getStatusClass(item.label)}">
          <div class="status-row">
            <span>${escapeHtml(item.label)}</span>
            <span>${item.count} 个</span>
          </div>
          <div class="bar"><span style="width:${item.percent}%"></span></div>
        </div>
      `
      )
      .join("")
  }

  const packageProgressChart = document.getElementById("package-progress-chart")
  if (packageProgressChart) {
    packageProgressChart.innerHTML = (workbenchData.packages || [])
      .map(
        (item) => `
        <div class="progress-item">
          <div class="progress-row">
            <span>${escapeHtml(item.id)}</span>
            <span>${item.progressPercent}%</span>
          </div>
          <div class="bar"><span style="width:${item.progressPercent}%"></span></div>
        </div>
      `,
      )
      .join("")
  }
}

function renderLinks(links) {
  return (links || [])
    .map((link) => `<a href="${escapeHtml(link.href)}">${escapeHtml(link.label)}</a>`)
    .join("")
}

function renderDetailTable(columns, rows) {
  if (!rows?.length) {
    return '<div class="empty-state">暂无数据。</div>'
  }

  const head = columns.map((column) => `<th>${escapeHtml(column)}</th>`).join("")
  const body = rows
    .map(
      (row) => `
        <tr>
          ${columns.map((column) => `<td>${escapeHtml(row[column] || "")}</td>`).join("")}
        </tr>
      `,
    )
    .join("")

  return `
    <table class="detail-table">
      <thead><tr>${head}</tr></thead>
      <tbody>${body}</tbody>
    </table>
  `
}

function renderPackageTable() {
  if (!packageTable) {
    return
  }
  const status = statusFilter?.value || ""
  const changeType = changeFilter?.value || ""
  const filtered = (workbenchData.packages || []).filter((item) => {
    return (!status || item.status === status) && (!changeType || item.changeType === changeType)
  })

  if (!filtered.length) {
    packageTable.innerHTML = '<div class="empty-state">当前筛选条件下没有需求包。</div>'
    return
  }

  const rows = filtered
    .map((item, index) => {
      const detailId = `detail-${index}`
      const currentStep = item.currentStep || {}
      const stepColumns = [
        "步骤编号",
        "步骤内容",
        "当前阶段",
        "当前状态",
        "当前责任角色",
        "当前执行工具",
        "下一责任角色",
        "验证结果",
        "问题说明或修复建议",
      ]
      const riskColumns = ["编号", "内容", "当前状态", "说明"]

      return `
        <tr>
          <td>
            <div class="package-name">${escapeHtml(item.id)}</div>
            <div class="chip-row">
              <span class="${getChipClass(item.status)}">${escapeHtml(item.status)}</span>
              <span class="${getChipClass(item.changeType)}">${escapeHtml(item.changeType)}</span>
            </div>
          </td>
          <td>${escapeHtml((item.modules || []).join(", "))}</td>
          <td>
            <div>${item.completedSteps}/${item.totalSteps}</div>
            <div class="bar"><span style="width:${item.progressPercent}%"></span></div>
          </td>
          <td>${escapeHtml(currentStep["步骤内容"] || item.goal || "待补充")}</td>
          <td>${escapeHtml(readField(currentStep, "当前责任角色", "当前责任模型") || "待补充")}</td>
          <td>${escapeHtml(currentStep["当前执行工具"] || "待补充")}</td>
          <td>${escapeHtml(item.prototypeBatch || "待补充")}</td>
          <td>${escapeHtml(item.latestUpdate || "待补充")}</td>
          <td>
            <button type="button" data-target="${detailId}">展开详情</button>
          </td>
        </tr>
        <tr id="${detailId}" class="detail-row" hidden>
          <td colspan="9">
            <div class="detail-panel">
              <div class="detail-grid">
                <section class="detail-section">
                  <h3>当前需求包摘要</h3>
                  <p><strong>目标：</strong>${escapeHtml(item.goal || "待补充")}</p>
                  <p><strong>当前步骤：</strong>${escapeHtml(currentStep["步骤编号"] || "待补充")} / ${escapeHtml(currentStep["步骤内容"] || "待补充")}</p>
                  <p><strong>问题说明：</strong>${escapeHtml(currentStep["问题说明或修复建议"] || "无")}</p>
                  <div class="detail-links">${renderLinks(item.links)}</div>
                </section>
                <section class="detail-section">
                  <h3>风险与阻塞</h3>
                  ${renderDetailTable(riskColumns, item.risks)}
                </section>
              </div>
              <section class="detail-section">
                <h3>步骤进度表</h3>
                ${renderDetailTable(stepColumns, item.stepRows)}
              </section>
            </div>
          </td>
        </tr>
      `
    })
    .join("")

  packageTable.innerHTML = `
    <table class="package-table">
      <thead>
        <tr>
          <th>需求包</th>
          <th>模块</th>
          <th>完成度</th>
          <th>当前步骤</th>
          <th>当前责任角色</th>
          <th>当前执行工具</th>
          <th>原型批次</th>
          <th>最近更新时间</th>
          <th>详情</th>
        </tr>
      </thead>
      <tbody>${rows}</tbody>
    </table>
  `

  packageTable.querySelectorAll("button[data-target]").forEach((button) => {
    button.addEventListener("click", () => {
      const detailRow = document.getElementById(button.dataset.target)
      const isHidden = detailRow.hasAttribute("hidden")
      if (isHidden) {
        detailRow.removeAttribute("hidden")
        button.textContent = "收起详情"
      } else {
        detailRow.setAttribute("hidden", "")
        button.textContent = "展开详情"
      }
    })
  })
}

function renderModules() {
  const container = document.getElementById("module-table")
  if (!container) {
    return
  }
  if (!workbenchData.modules?.length) {
    container.innerHTML = '<div class="empty-state">暂无模块数据。</div>'
    return
  }
  const rows = workbenchData.modules
    .map(
      (item) => `
        <tr>
          <td>${escapeHtml(item.name)}</td>
          <td><code>${escapeHtml(item.code)}</code></td>
          <td>${escapeHtml(item.status)}</td>
          <td>${escapeHtml(item.latestPackage)}</td>
          <td>${escapeHtml(item.prototypeBatch)}</td>
          <td>${escapeHtml(item.note)}</td>
          <td><a href="${escapeHtml(item.readme)}">模块文档</a></td>
        </tr>
      `
    )
    .join("")

  container.innerHTML = `
    <table>
      <thead>
        <tr>
          <th>模块中文名</th>
          <th>模块编码</th>
          <th>当前状态</th>
          <th>最近需求包</th>
          <th>原型批次</th>
          <th>说明</th>
          <th>跳转</th>
        </tr>
      </thead>
      <tbody>${rows}</tbody>
    </table>
  `
}

function renderPrototypes() {
  const container = document.getElementById("prototype-table")
  if (!container) {
    return
  }
  if (!workbenchData.prototypes?.length) {
    container.innerHTML = '<div class="empty-state">暂无原型批次。</div>'
    return
  }
  const rows = workbenchData.prototypes
    .map(
      (item) => `
        <tr>
          <td>${escapeHtml(item.batch)}</td>
          <td>${escapeHtml(item.requirementBatch)}</td>
          <td>${escapeHtml(item.note)}</td>
          <td><a href="${escapeHtml(item.readme)}">原型说明</a></td>
          <td><a href="${escapeHtml(item.requirement)}">需求批次</a></td>
        </tr>
      `,
    )
    .join("")

  container.innerHTML = `
    <table>
      <thead>
        <tr>
          <th>原型批次</th>
          <th>关联需求</th>
          <th>说明</th>
          <th>原型说明</th>
          <th>需求批次</th>
        </tr>
      </thead>
      <tbody>${rows}</tbody>
    </table>
  `
}

function applyFilters() {
  const packages = workbenchData.packages || []
  const statuses = [...new Set(packages.map((item) => item.status).filter(Boolean))]
  const changeTypes = [...new Set(packages.map((item) => item.changeType).filter(Boolean))]

  createOptions(statusFilter, statuses, "全部状态")
  createOptions(changeFilter, changeTypes, "全部类型")
}

function startAutoRefresh() {
  clearInterval(refreshTimer)
  refreshTimer = null
  if (autoRefreshCheckbox?.checked) {
    refreshTimer = setInterval(() => {
      reloadData(false)
    }, 30000)
  }
}

async function reloadData(showFeedback = true) {
  try {
    await loadWorkbenchData()
    renderSummary()
    renderQuickLinks()
    applyFilters()
    renderPackageTable()
    renderModules()
    renderPrototypes()
    if (showFeedback && refreshButton) {
      refreshButton.textContent = "已刷新"
      setTimeout(() => {
        refreshButton.textContent = "刷新页面数据"
      }, 1200)
    }
  } catch (error) {
    console.error(error)
    if (packageTable) {
      packageTable.innerHTML = '<div class="empty-state">工作台数据加载失败，请先运行构建脚本后再刷新。</div>'
    }
  }
}

async function init() {
  statusFilter?.addEventListener("change", renderPackageTable)
  changeFilter?.addEventListener("change", renderPackageTable)
  refreshButton?.addEventListener("click", () => reloadData())
  autoRefreshCheckbox?.addEventListener("change", startAutoRefresh)

  await reloadData(false)
}

init()
