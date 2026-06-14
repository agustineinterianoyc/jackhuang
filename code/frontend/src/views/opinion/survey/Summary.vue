<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Message, Modal } from '@arco-design/web-vue'
import { IconArrowLeft, IconSave, IconDownload, IconUpload } from '@arco-design/web-vue/es/icon'
import {
  OPINION_MODULE_CODE,
  OPINION_MODULE_CODE_TEXT,
  statusColor,
} from '@/constants/opinion'
import { getSurveyDetail, publishSurvey as apiPublishSurvey } from '@/api/opinion/survey'
import { listSummaryItems, saveSummaryItems } from '@/api/opinion/summary'
import type { SurveyDetail } from '@/types/opinion/survey'

defineOptions({ name: 'OpinionSurveySummary' })

interface SummaryItemEdit {
  id?: number
  moduleCode: string
  departmentName: string
  indicatorCategory: string
  indicatorName: string
  factorName: string
  unitName: string
  opinionCategory: string
  opinionContent: string
  isAdopted: boolean
  adoptionRemark: string
  finalIsAdopted: boolean
  adjustedContent?: string
}

const route = useRoute()
const router = useRouter()
const surveyId = computed(() => Number(route.params.id))

const survey = ref<SurveyDetail | null>(null)
const loading = ref(false)
const saving = ref(false)
const publishing = ref(false)

const activeTab = ref(OPINION_MODULE_CODE.KPI)
const items = ref<SummaryItemEdit[]>([])

const moduleTabs = Object.keys(OPINION_MODULE_CODE_TEXT).map((code) => ({
  code,
  text: OPINION_MODULE_CODE_TEXT[code as keyof typeof OPINION_MODULE_CODE_TEXT],
}))

const filteredItems = computed(() =>
  items.value.filter((item) => item.moduleCode === activeTab.value),
)

const canPublish = computed(() => survey.value?.status === 'DONE')

async function loadSurvey() {
  try {
    survey.value = await getSurveyDetail(surveyId.value)
  } catch {
    Message.error('加载征集信息失败')
  }
}

async function loadItems() {
  loading.value = true
  try {
    const res = await listSummaryItems(surveyId.value, {
      page: 1,
      pageSize: 9999,
    })
    items.value = ((res.records ?? []) as any[]).map<SummaryItemEdit>((item) => ({
      id: item.id,
      moduleCode: item.moduleCode ?? '',
      departmentName: item.departmentName ?? '',
      indicatorCategory: item.indicatorCategory ?? '',
      indicatorName: item.indicatorName ?? '',
      factorName: item.factorName ?? '',
      unitName: item.unitName ?? '',
      opinionCategory: item.opinionCategory ?? '',
      opinionContent: item.opinionContent ?? '',
      isAdopted: item.isAdopted ?? false,
      adoptionRemark: item.adoptionRemark ?? '',
      finalIsAdopted: item.finalIsAdopted ?? false,
      adjustedContent: item.adjustedContent ?? undefined,
    }))
  } catch {
    Message.error('加载汇总数据失败')
  } finally {
    loading.value = false
  }
}

async function handleSave() {
  saving.value = true
  try {
    const payload = items.value.map((item) => ({
      id: item.id,
      moduleCode: item.moduleCode,
      finalIsAdopted: item.finalIsAdopted,
      adjustedContent: item.adjustedContent ?? undefined,
    }))
    await saveSummaryItems(surveyId.value, payload)
    Message.success('保存成功')
  } catch {
    Message.error('保存失败')
  } finally {
    saving.value = false
  }
}

function handlePublish() {
  Modal.confirm({
    title: '确认发布',
    content: '确认发布本次意见征集汇总吗？发布后将不可再修改。',
    okText: '确认发布',
    cancelText: '取消',
    onOk: async () => {
      publishing.value = true
      try {
        await apiPublishSurvey(surveyId.value)
        Message.success('发布成功')
        loadSurvey()
      } catch {
        Message.error('发布失败')
      } finally {
        publishing.value = false
      }
    },
  })
}

function handleExport() {
  window.open(`/api/opinion/summary/${surveyId.value}/export`)
}

function handleImport() {
  const input = document.createElement('input')
  input.type = 'file'
  input.accept = '.csv,.xlsx,.xls'
  input.onchange = () => {
    Message.info('导入功能开发中')
  }
  input.click()
}

function goBack() {
  router.push({ name: 'opinion-survey-list' })
}

onMounted(async () => {
  await loadSurvey()
  await loadItems()
})

watch(activeTab, () => {})
</script>

<template>
  <section class="opinion-survey-summary">
    <header class="page-header">
      <a-button type="text" @click="goBack">
        <template #icon><IconArrowLeft /></template>
        返回列表
      </a-button>
      <h2>汇总发布</h2>
    </header>

    <a-spin :loading="loading" tip="加载中">
      <div v-if="survey" class="detail-content">
        <a-descriptions :column="3" :data="[]" layout="inline-vertical" class="info-block" bordered>
          <a-descriptions-item label="征集名称">{{ survey.name }}</a-descriptions-item>
          <a-descriptions-item label="考核周期">{{ survey.assessYear }}年</a-descriptions-item>
          <a-descriptions-item label="状态">
            <a-tag :color="statusColor(survey.status)" size="small">{{ survey.statusText }}</a-tag>
          </a-descriptions-item>
        </a-descriptions>

        <a-tabs v-model:active-key="activeTab" class="module-tabs">
          <a-tab-pane v-for="mod in moduleTabs" :key="mod.code" :title="mod.text">
            <div class="tab-content">
              <div class="table-scroll">
                <a-table
                  :data="filteredItems"
                  :pagination="false"
                  stripe
                  row-key="id"
                  size="small"
                  :scroll="{ x: 1600 }"
                >
                  <template #columns>
                    <a-table-column title="序号" :width="60" align="center">
                      <template #cell="{ rowIndex }">{{ rowIndex + 1 }}</template>
                    </a-table-column>
                    <a-table-column title="部门" data-index="departmentName" :width="120" />
                    <a-table-column title="指标类别" data-index="indicatorCategory" :width="120">
                      <template #cell="{ record }">{{ record.indicatorCategory || '-' }}</template>
                    </a-table-column>
                    <a-table-column title="指标名称" data-index="indicatorName" :width="140">
                      <template #cell="{ record }">{{ record.indicatorName || '-' }}</template>
                    </a-table-column>
                    <a-table-column title="要素" data-index="factorName" :width="120">
                      <template #cell="{ record }">{{ record.factorName || '-' }}</template>
                    </a-table-column>
                    <a-table-column title="单位" data-index="unitName" :width="120" />
                    <a-table-column title="意见分类" data-index="opinionCategory" :width="100">
                      <template #cell="{ record }">{{ record.opinionCategory || '-' }}</template>
                    </a-table-column>
                    <a-table-column title="意见内容" data-index="opinionContent" :width="200" ellipsis tooltip>
                      <template #cell="{ record }">{{ record.opinionContent || '-' }}</template>
                    </a-table-column>
                    <a-table-column title="是否采纳" :width="90" align="center">
                      <template #cell="{ record }">
                        <a-tag :color="record.isAdopted ? 'green' : 'gray'" size="small">
                          {{ record.isAdopted ? '是' : '否' }}
                        </a-tag>
                      </template>
                    </a-table-column>
                    <a-table-column title="采纳说明" data-index="adoptionRemark" :width="150" ellipsis tooltip>
                      <template #cell="{ record }">{{ record.adoptionRemark || '-' }}</template>
                    </a-table-column>
                    <a-table-column title="最终采纳" :width="100" align="center">
                      <template #cell="{ record }">
                        <a-switch
                          v-model="record.finalIsAdopted"
                          size="small"
                          :disabled="!record.isAdopted"
                        />
                      </template>
                    </a-table-column>
                    <a-table-column title="调整后内容" :width="200">
                      <template #cell="{ record }">
                        <a-textarea
                          v-model="record.adjustedContent"
                          placeholder="请输入"
                          :auto-size="{ minRows: 1, maxRows: 3 }"
                        />
                      </template>
                    </a-table-column>
                  </template>
                </a-table>
              </div>

              <div v-if="filteredItems.length === 0" class="table-empty">
                暂无数据
              </div>
            </div>
          </a-tab-pane>
        </a-tabs>

        <div class="bottom-bar">
          <a-space>
            <a-button type="primary" :loading="saving" @click="handleSave">
              <template #icon><IconSave /></template>
              保存
            </a-button>
            <a-button
              type="primary"
              status="success"
              :loading="publishing"
              :disabled="!canPublish"
              @click="handlePublish"
            >
              发布
            </a-button>
            <a-button @click="handleExport">
              <template #icon><IconDownload /></template>
              导出CSV
            </a-button>
            <a-button @click="handleImport">
              <template #icon><IconUpload /></template>
              导入
            </a-button>
          </a-space>
        </div>
      </div>
    </a-spin>
  </section>
</template>

<style scoped lang="less">
.opinion-survey-summary {
  background: #fff;
  padding: 16px 24px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);

  .page-header {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 16px;
    padding-bottom: 8px;
    border-bottom: 1px solid #f0f0f0;

    h2 {
      margin: 0;
      font-size: 16px;
      font-weight: 600;
    }
  }

  .info-block {
    margin-bottom: 16px;
  }

  .module-tabs {
    .tab-content {
      .table-scroll {
        margin-bottom: 12px;
      }

      .table-empty {
        padding: 24px;
        text-align: center;
        color: #999;
        font-size: 13px;
      }
    }
  }

  .bottom-bar {
    margin-top: 24px;
    padding-top: 16px;
    border-top: 1px solid #f0f0f0;
  }
}

:deep(.arco-tabs-content) {
  padding-top: 8px;
}
</style>
