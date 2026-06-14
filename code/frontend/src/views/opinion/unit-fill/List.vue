<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { IconRefresh, IconSearch } from '@arco-design/web-vue/es/icon'
import {
  OPINION_PAGE_SIZE_OPTIONS,
  OPINION_UNIT_FILL_STATUS_OPTIONS,
  OPINION_UNIT_FILL_STATUS_COLOR,
  OPINION_UNIT_AUDIT_STATUS_COLOR,
} from '@/constants/opinion'
import { listUnitTasks } from '@/api/opinion/unit-fill'
import type { UnitFillListItem } from '@/types/opinion/unit-fill'

defineOptions({ name: 'OpinionUnitFillList' })

const router = useRouter()

const yearOptions = (() => {
  const now = new Date().getFullYear()
  const list: { value: number; label: string }[] = []
  for (let y = now + 1; y >= now - 5; y--) {
    list.push({ value: y, label: `${y}年` })
  }
  return list
})()

const filters = reactive<{
  assessYear: number | undefined
  name: string
  fillStatus: string[]
  page: number
  pageSize: number
}>({
  assessYear: undefined,
  name: '',
  fillStatus: [],
  page: 1,
  pageSize: 20,
})

const loading = ref(false)
const total = ref(0)
const records = ref<UnitFillListItem[]>([])

async function fetchData() {
  loading.value = true
  try {
    const res = await listUnitTasks({
      assessYear: filters.assessYear,
      name: filters.name?.trim() || undefined,
      fillStatus: filters.fillStatus.length ? filters.fillStatus : undefined,
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
  filters.fillStatus = []
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

function goDetail(row: UnitFillListItem) {
  router.push({ name: 'opinion-unit-fill-detail', params: { taskId: String(row.taskId) } })
}

function fillStatusColor(status: string): string {
  return OPINION_UNIT_FILL_STATUS_COLOR[status as keyof typeof OPINION_UNIT_FILL_STATUS_COLOR] ?? 'gray'
}

function auditStatusColor(status: string): string {
  return OPINION_UNIT_AUDIT_STATUS_COLOR[status as keyof typeof OPINION_UNIT_AUDIT_STATUS_COLOR] ?? 'gray'
}

const columns = computed(() => [
  { title: '序号', slotName: 'index', width: 70, align: 'center' as const },
  { title: '征集名称', dataIndex: 'surveyName', ellipsis: true, tooltip: true },
  { title: '考核年份', dataIndex: 'assessYear', width: 100, slotName: 'assessYear' },
  { title: '本单位名称', dataIndex: 'unitName', width: 200, ellipsis: true, tooltip: true },
  { title: '填报状态', slotName: 'fillStatus', width: 100, align: 'center' as const },
  { title: '审核状态', slotName: 'auditStatus', width: 100, align: 'center' as const },
  { title: '基层截止时间', dataIndex: 'unitDeadline', width: 170 },
  {
    title: '操作',
    slotName: 'action',
    width: 160,
    align: 'center' as const,
    fixed: 'right' as const,
  },
])

onMounted(fetchData)
</script>

<template>
  <section class="opinion-unit-fill-list">
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
        <a-form-item label="填报状态">
          <a-select
            v-model="filters.fillStatus"
            placeholder="请选择"
            multiple
            allow-clear
            style="width: 220px"
          >
            <a-option
              v-for="s in OPINION_UNIT_FILL_STATUS_OPTIONS"
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
      <template #fillStatus="{ record }">
        <a-tag :color="fillStatusColor(record.fillStatus)" size="small">
          {{ record.fillStatusText }}
        </a-tag>
      </template>
      <template #auditStatus="{ record }">
        <a-tag :color="auditStatusColor(record.auditStatus)" size="small">
          {{ record.auditStatusText }}
        </a-tag>
      </template>
      <template #action="{ record }">
        <a-space>
          <a-button
            v-if="record.fillStatus === 'PENDING'"
            type="text"
            size="small"
            @click="goDetail(record)"
          >
            填报
          </a-button>
          <a-button type="text" size="small" @click="goDetail(record)">查看</a-button>
        </a-space>
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
.opinion-unit-fill-list {
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
