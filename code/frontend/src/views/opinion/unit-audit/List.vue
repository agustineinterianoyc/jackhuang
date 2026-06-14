<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { IconRefresh, IconSearch } from '@arco-design/web-vue/es/icon'
import { OPINION_PAGE_SIZE_OPTIONS } from '@/constants/opinion'
import { listAuditTasks } from '@/api/opinion/unit-audit'
import type { UnitAuditListItem } from '@/types/opinion/unit-audit'

defineOptions({ name: 'OpinionUnitAuditList' })

const router = useRouter()

const yearOptions = (() => {
  const now = new Date().getFullYear()
  const list: { value: number; label: string }[] = []
  for (let y = now + 1; y >= now - 5; y--) {
    list.push({ value: y, label: `${y}年` })
  }
  return list
})()

const AUDIT_STATUS_OPTIONS = [
  { value: 'PENDING', label: '待审核' },
  { value: 'PASS', label: '已通过' },
  { value: 'REJECTED', label: '已退回' },
]

const filters = reactive<{
  assessYear: number | undefined
  name: string
  auditStatus: string[]
  page: number
  pageSize: number
}>({
  assessYear: undefined,
  name: '',
  auditStatus: [],
  page: 1,
  pageSize: 20,
})

const loading = ref(false)
const total = ref(0)
const records = ref<UnitAuditListItem[]>([])

async function fetchData() {
  loading.value = true
  try {
    const res = await listAuditTasks({
      assessYear: filters.assessYear,
      name: filters.name?.trim() || undefined,
      auditStatus: filters.auditStatus.length ? filters.auditStatus : undefined,
      page: filters.page,
      pageSize: filters.pageSize,
    })
    records.value = res.records || []
    total.value = res.total || 0
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  filters.page = 1
  fetchData()
}

function handleClear() {
  filters.assessYear = undefined
  filters.name = ''
  filters.auditStatus = []
  filters.page = 1
  fetchData()
}

function onPageChange(page: number) {
  filters.page = page
  fetchData()
}

function onPageSizeChange(size: number) {
  filters.pageSize = size
  filters.page = 1
  fetchData()
}

function goAudit(row: UnitAuditListItem) {
  router.push({ name: 'opinion-unit-audit-detail', params: { taskId: String(row.taskId) } })
}

function fillStatusColor(status: string) {
  const map: Record<string, string> = { PENDING: 'orange', SUBMITTED: 'green' }
  return map[status] || 'gray'
}

function auditStatusColor(status: string) {
  const map: Record<string, string> = { PENDING: 'orange', PASS: 'green', REJECTED: 'red' }
  return map[status] || 'gray'
}

function auditActionText(status: string) {
  return status === 'PENDING' ? '审核' : '查看'
}

const columns = computed(() => [
  { title: '序号', slotName: 'index', width: 70, align: 'center' as const },
  { title: '征集名称', dataIndex: 'surveyName', ellipsis: true, tooltip: true },
  { title: '考核年份', slotName: 'assessYear', width: 110 },
  { title: '填报单位', dataIndex: 'unitName', width: 180 },
  { title: '提交时间', slotName: 'submittedAt', width: 170 },
  { title: '填报状态', slotName: 'fillStatus', width: 110, align: 'center' as const },
  { title: '审核状态', slotName: 'auditStatus', width: 110, align: 'center' as const },
  { title: '操作', slotName: 'action', width: 120, align: 'center' as const, fixed: 'right' as const },
])

onMounted(fetchData)
</script>

<template>
  <section class="opinion-unit-audit-list">
    <div class="filter-bar">
      <a-form :model="filters" layout="inline" :auto-label-width="true">
        <a-form-item label="考核年份">
          <a-select
            v-model="filters.assessYear"
            placeholder="请选择"
            allow-clear
            style="width: 140px"
          >
            <a-option
              v-for="y in yearOptions"
              :key="y.value"
              :value="y.value"
              :label="y.label"
            />
          </a-select>
        </a-form-item>
        <a-form-item label="征集名称">
          <a-input
            v-model="filters.name"
            placeholder="请输入"
            allow-clear
            style="width: 220px"
            @press-enter="handleSearch"
          />
        </a-form-item>
        <a-form-item label="审核状态">
          <a-select
            v-model="filters.auditStatus"
            placeholder="请选择"
            multiple
            allow-clear
            style="width: 220px"
          >
            <a-option
              v-for="s in AUDIT_STATUS_OPTIONS"
              :key="s.value"
              :value="s.value"
              :label="s.label"
            />
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-space>
            <a-button type="primary" @click="handleSearch">
              <template #icon><IconSearch /></template>
              搜索
            </a-button>
            <a-button @click="handleClear">
              <template #icon><IconRefresh /></template>
              清空条件
            </a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </div>

    <div class="action-bar">
      <h3 class="page-title">基层单位意见征集审核</h3>
      <span class="total-tip">共 {{ total }} 条</span>
    </div>

    <a-table
      :columns="columns"
      :data="records"
      :loading="loading"
      :pagination="false"
      stripe
      row-key="taskId"
      :scroll="{ x: 1000 }"
    >
      <template #index="{ rowIndex }">
        {{ String((filters.page - 1) * filters.pageSize + rowIndex + 1).padStart(2, '0') }}
      </template>
      <template #assessYear="{ record }">{{ record.assessYear }}年</template>
      <template #submittedAt="{ record }">{{ record.submittedAt || '-' }}</template>
      <template #fillStatus="{ record }">
        <a-tag :color="fillStatusColor(record.fillStatus)" size="small">{{ record.fillStatusText }}</a-tag>
      </template>
      <template #auditStatus="{ record }">
        <a-tag :color="auditStatusColor(record.auditStatus)" size="small">{{ record.auditStatusText }}</a-tag>
      </template>
      <template #action="{ record }">
        <a-button type="text" size="small" @click="goAudit(record)">
          {{ auditActionText(record.auditStatus) }}
        </a-button>
      </template>
    </a-table>

    <div class="pagination-bar">
      <a-pagination
        :current="filters.page"
        :page-size="filters.pageSize"
        :total="total"
        show-total
        show-page-size
        :page-size-options="OPINION_PAGE_SIZE_OPTIONS"
        @change="onPageChange"
        @page-size-change="onPageSizeChange"
      />
    </div>
  </section>
</template>

<style scoped lang="less">
.opinion-unit-audit-list {
  background: #fff;
  padding: 16px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);

  .filter-bar {
    margin-bottom: 12px;
    padding-bottom: 12px;
    border-bottom: 1px solid #f0f0f0;
  }

  .action-bar {
    margin-bottom: 12px;
    display: flex;
    justify-content: space-between;
    align-items: center;

    .page-title {
      margin: 0;
      font-size: 15px;
      font-weight: 600;
    }

    .total-tip {
      color: #888;
      font-size: 13px;
    }
  }

  .pagination-bar {
    margin-top: 16px;
    display: flex;
    justify-content: flex-end;
  }
}
</style>
