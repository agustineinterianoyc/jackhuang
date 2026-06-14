<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { Message, Modal } from '@arco-design/web-vue'
import {
  IconPlus,
  IconRefresh,
  IconSearch,
} from '@arco-design/web-vue/es/icon'
import {
  OPINION_LIST_STATUS_OPTIONS,
  OPINION_PAGE_SIZE_OPTIONS,
  statusColor,
  type OpinionMainStatus,
} from '@/constants/opinion'
import { deleteSurvey, listSurveys, startSurvey } from '@/api/opinion/survey'
import type { SurveyListItem } from '@/types/opinion/survey'

defineOptions({ name: 'OpinionSurveyList' })

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
  status: OpinionMainStatus[]
  page: number
  pageSize: number
}>({
  assessYear: undefined,
  name: '',
  status: [],
  page: 1,
  pageSize: 20,
})

const loading = ref(false)
const total = ref(0)
const records = ref<SurveyListItem[]>([])

async function fetchData() {
  loading.value = true
  try {
    const res = await listSurveys({
      assessYear: filters.assessYear,
      name: filters.name?.trim() || undefined,
      status: filters.status.length ? filters.status : undefined,
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
  filters.status = []
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

function goCreate() {
  router.push({ name: 'opinion-survey-edit', params: { id: 'new' } })
}

function goEdit(row: SurveyListItem) {
  router.push({ name: 'opinion-survey-edit', params: { id: String(row.id) } })
}

function goView(row: SurveyListItem) {
  router.push({ name: 'opinion-survey-view', params: { id: String(row.id) } })
}

function handleDelete(row: SurveyListItem) {
  Modal.warning({
    title: '确认删除',
    content: `确认删除「${row.name}」？删除后不可恢复。`,
    hideCancel: false,
    okText: '删除',
    cancelText: '取消',
    onOk: async () => {
      await deleteSurvey(row.id)
      Message.success('删除成功')
      fetchData()
    },
  })
}

function handleStart(row: SurveyListItem) {
  Modal.confirm({
    title: '确认开启征集',
    content: '确认开启征集吗？开启后不可再编辑意见征集信息。',
    okText: '开启',
    cancelText: '取消',
    onOk: async () => {
      const res = await startSurvey(row.id)
      Message.success(`征集已开启，已初始化 ${res.unitTaskCount} 个基层任务`)
      fetchData()
    },
  })
}

const columns = computed(() => [
  { title: '序号', slotName: 'index', width: 70, align: 'center' as const },
  { title: '考核周期', dataIndex: 'assessYear', width: 110, slotName: 'assessYear' },
  { title: '征集名称', dataIndex: 'name', ellipsis: true, tooltip: true },
  {
    title: '基层填报进度',
    slotName: 'unitProgress',
    width: 130,
    align: 'center' as const,
  },
  {
    title: '专业反馈进度',
    slotName: 'deptProgress',
    width: 130,
    align: 'center' as const,
  },
  { title: '状态', slotName: 'status', width: 110, align: 'center' as const },
  { title: '基层截止', dataIndex: 'unitDeadline', width: 170 },
  { title: '操作', slotName: 'action', width: 240, align: 'center' as const, fixed: 'right' as const },
])

onMounted(fetchData)
</script>

<template>
  <section class="opinion-survey-list">
    <div class="filter-bar">
      <a-form :model="filters" layout="inline" :auto-label-width="true">
        <a-form-item label="考核周期">
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
        <a-form-item label="状态">
          <a-select
            v-model="filters.status"
            placeholder="请选择"
            multiple
            allow-clear
            style="width: 220px"
          >
            <a-option
              v-for="s in OPINION_LIST_STATUS_OPTIONS"
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
      <a-space>
        <a-button type="primary" @click="goCreate">
          <template #icon><IconPlus /></template>
          新增意见征集
        </a-button>
      </a-space>
      <span class="total-tip">共 {{ total }} 条</span>
    </div>

    <a-table
      :columns="columns"
      :data="records"
      :loading="loading"
      :pagination="false"
      stripe
      row-key="id"
      :scroll="{ x: 1000 }"
    >
      <template #index="{ rowIndex }">
        {{ String((filters.page - 1) * filters.pageSize + rowIndex + 1).padStart(2, '0') }}
      </template>
      <template #assessYear="{ record }">{{ record.assessYear }}年</template>
      <template #unitProgress="{ record }">
        <span>{{ record.unitProgress }}</span>
      </template>
      <template #deptProgress="{ record }">
        <span>{{ record.deptProgress }}</span>
      </template>
      <template #status="{ record }">
        <a-tag :color="statusColor(record.status)" size="small">{{ record.statusText }}</a-tag>
      </template>
      <template #action="{ record }">
        <a-space>
          <a-button v-if="record.status === 'DRAFT'" type="text" size="small" @click="goEdit(record)">
            编辑
          </a-button>
          <a-button v-if="record.status === 'DRAFT'" type="text" size="small" @click="handleStart(record)">
            开启征集
          </a-button>
          <a-button type="text" size="small" @click="goView(record)">查看</a-button>
          <a-button
            v-if="record.status === 'DRAFT'"
            type="text"
            size="small"
            status="danger"
            @click="handleDelete(record)"
          >
            删除
          </a-button>
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
.opinion-survey-list {
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
