<script setup lang="ts">
import { computed, onMounted, reactive, ref, watch } from 'vue'
import { Message } from '@arco-design/web-vue'
import { IconSearch } from '@arco-design/web-vue/es/icon'
import { OPINION_UNIT_FILL_STATUS_OPTIONS } from '@/constants/opinion'
import {
  batchRemind,
  queryDeptProgress,
  queryUnitProgress,
  singleRemind,
} from '@/api/opinion/survey'
import type { DeptProgressVO, UnitProgressVO } from '@/types/opinion/survey'

defineOptions({ name: 'OpinionSurveyProgressModal' })

const props = defineProps<{
  visible: boolean
  surveyId: number
  mode: 'unit' | 'dept'
}>()

const emit = defineEmits<{
  (e: 'update:visible', v: boolean): void
  (e: 'close'): void
}>()

const isUnit = computed(() => props.mode === 'unit')

const filters = reactive({
  keyword: '',
  submitStatus: undefined as string | undefined,
  page: 1,
  pageSize: 10,
})

const loading = ref(false)
const total = ref(0)
const records = ref<(UnitProgressVO | DeptProgressVO)[]>([])

async function fetchData() {
  loading.value = true
  try {
    const params = {
      keyword: filters.keyword?.trim() || undefined,
      submitStatus: filters.submitStatus,
      page: filters.page,
      pageSize: filters.pageSize,
    }
    if (isUnit.value) {
      const res = await queryUnitProgress(props.surveyId, params)
      records.value = res.records ?? []
      total.value = res.total ?? 0
    } else {
      const res = await queryDeptProgress(props.surveyId, params)
      records.value = res.records ?? []
      total.value = res.total ?? 0
    }
  } finally {
    loading.value = false
  }
}

function handleSearch() {
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

async function handleBatchRemind() {
  const targetType = isUnit.value ? 'UNIT' : 'DEPT'
  try {
    await batchRemind(props.surveyId, targetType)
    Message.success('已发送提醒')
  } catch {
    Message.error('操作失败')
  }
}

async function handleSingleRemind(row: UnitProgressVO | DeptProgressVO) {
  const targetType = isUnit.value ? 'UNIT' : 'DEPT'
  const targetId = isUnit.value ? (row as UnitProgressVO).unitId : (row as DeptProgressVO).departmentId
  try {
    await singleRemind(props.surveyId, targetType, targetId)
    Message.success('已发送提醒')
  } catch {
    Message.error('操作失败')
  }
}

function isUnsubmitted(row: UnitProgressVO | DeptProgressVO): boolean {
  if (isUnit.value) {
    return (row as UnitProgressVO).fillStatus === 'PENDING'
  }
  return (row as DeptProgressVO).submitStatus === 'PENDING'
}

function statusColor(status: string): string {
  return status === 'SUBMITTED' || status === 'PASS' ? 'green' : 'orange'
}

const pageSizeOptions = [10, 20, 50]

watch(
  () => props.visible,
  (v) => {
    if (v) {
      filters.keyword = ''
      filters.submitStatus = undefined
      filters.page = 1
      filters.pageSize = 10
      fetchData()
    }
  },
)

function handleClose() {
  emit('update:visible', false)
  emit('close')
}
</script>

<template>
  <a-modal
    :visible="visible"
    :title="isUnit ? '基层填报进度' : '专业部门反馈进度'"
    :width="960"
    :footer="false"
    @cancel="handleClose"
    @close="handleClose"
  >
    <div class="progress-modal">
      <div class="filter-bar">
        <a-input
          v-model="filters.keyword"
          placeholder="请输入单位/部门名称"
          allow-clear
          style="width: 200px"
          @press-enter="handleSearch"
        />
        <a-select
          v-model="filters.submitStatus"
          placeholder="提交状态"
          allow-clear
          style="width: 140px; margin-left: 8px"
          @change="handleSearch"
        >
          <a-option
            v-for="opt in OPINION_UNIT_FILL_STATUS_OPTIONS"
            :key="opt.value"
            :value="opt.value"
            :label="opt.label"
          />
        </a-select>
        <a-button type="outline" style="margin-left: 8px" @click="handleSearch">
          <template #icon><IconSearch /></template>
          搜索
        </a-button>
        <a-button type="primary" style="margin-left: 8px" @click="handleBatchRemind">
          一键提醒
        </a-button>
      </div>

      <a-table
        :data="records"
        :loading="loading"
        :pagination="false"
        stripe
        row-key="taskId"
        size="small"
        class="progress-table"
      >
        <template #columns>
          <a-table-column title="序号" :width="60" align="center">
            <template #cell="{ rowIndex }">{{ rowIndex + 1 }}</template>
          </a-table-column>
          <a-table-column
            :title="isUnit ? '单位名称' : '部门名称'"
            :data-index="isUnit ? 'unitName' : 'departmentName'"
            :width="180"
          />
          <a-table-column
            :title="isUnit ? '单位类型' : '模块'"
            :data-index="isUnit ? 'unitTypeText' : 'moduleName'"
            :width="120"
          />
          <a-table-column :title="isUnit ? '填报状态' : '提交状态'" :width="100" align="center">
            <template #cell="{ record }">
              <a-tag
                :color="statusColor(isUnit ? record.fillStatus : record.submitStatus)"
                size="small"
              >
                {{ isUnit ? record.fillStatusText : record.submitStatusText }}
              </a-tag>
            </template>
          </a-table-column>
          <a-table-column title="审核状态" :width="100" align="center">
            <template #cell="{ record }">
              <a-tag :color="statusColor(record.auditStatus)" size="small">
                {{ record.auditStatusText }}
              </a-tag>
            </template>
          </a-table-column>
          <a-table-column title="操作" :width="80" align="center">
            <template #cell="{ record }">
              <a-button
                v-if="isUnsubmitted(record)"
                type="text"
                size="small"
                @click="handleSingleRemind(record)"
              >
                提醒
              </a-button>
              <span v-else>-</span>
            </template>
          </a-table-column>
        </template>
      </a-table>

      <div class="pagination-bar">
        <a-pagination
          :current="filters.page"
          :page-size="filters.pageSize"
          :total="total"
          show-total
          show-page-size
          :page-size-options="pageSizeOptions"
          @change="onPageChange"
          @page-size-change="onPageSizeChange"
        />
      </div>
    </div>
  </a-modal>
</template>

<style scoped lang="less">
.progress-modal {
  .filter-bar {
    margin-bottom: 12px;
    display: flex;
    align-items: center;
  }

  .progress-table {
    margin-bottom: 12px;
  }

  .pagination-bar {
    display: flex;
    justify-content: flex-end;
  }
}
</style>
