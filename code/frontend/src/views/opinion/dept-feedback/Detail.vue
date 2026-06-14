<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Message, Modal } from '@arco-design/web-vue'
import { IconArrowLeft, IconSave } from '@arco-design/web-vue/es/icon'
import {
  OPINION_DEPT_SUBMIT_STATUS_COLOR,
  OPINION_DEPT_AUDIT_STATUS_COLOR,
  OPINION_MAIN_STATUS_TEXT,
  OPINION_MAIN_STATUS_COLOR,
  OPINION_DEPT_SUBMIT_STATUS_TEXT,
  OPINION_DEPT_AUDIT_STATUS_TEXT,
} from '@/constants/opinion'
import {
  getDeptFeedbackDetail,
  saveDeptFeedback,
  submitDeptFeedback,
} from '@/api/opinion/dept-feedback'
import type {
  DeptFeedbackDetail,
  DeptFeedbackItem,
} from '@/types/opinion/dept-feedback'

interface DeptFeedbackItemEdit {
  itemId: number
  moduleCode: string
  indicatorCategory: string | null
  indicatorName: string | null
  factorName: string | null
  unitName: string
  opinionCategory: string | null
  opinionContent: string | null
  reason: string | null
  isAdopted: boolean
  adoptionRemark?: string
}

defineOptions({ name: 'OpinionDeptFeedbackDetail' })

const route = useRoute()
const router = useRouter()

const taskId = computed(() => Number(route.params.taskId))

const detail = ref<DeptFeedbackDetail | null>(null)
const loading = ref(false)
const saving = ref(false)
const submitting = ref(false)
const activeTab = ref('')

const isEditMode = computed(() => {
  if (!detail.value) return false
  const { submitStatus, surveyStatus } = detail.value.task
  return submitStatus === 'PENDING' && surveyStatus !== 'PUBLISHED'
})

const localItems = ref<DeptFeedbackItemEdit[]>([])

function filteredItems(moduleCode: string): DeptFeedbackItemEdit[] {
  return localItems.value.filter((item) => item.moduleCode === moduleCode)
}

async function load() {
  loading.value = true
  try {
    const res = await getDeptFeedbackDetail(taskId.value)
    detail.value = res
    localItems.value = (res.items || []).map<DeptFeedbackItemEdit>((item: DeptFeedbackItem) => ({
      itemId: item.itemId,
      moduleCode: item.moduleCode,
      indicatorCategory: item.indicatorCategory,
      indicatorName: item.indicatorName,
      factorName: item.factorName,
      unitName: item.unitName,
      opinionCategory: item.opinionCategory,
      opinionContent: item.opinionContent,
      reason: item.reason,
      isAdopted: item.isAdopted ?? false,
      adoptionRemark: item.adoptionRemark ?? undefined,
    }))
    if (res.modules.length > 0 && res.modules[0]) {
      activeTab.value = res.modules[0].moduleCode
    }
  } finally {
    loading.value = false
  }
}

function buildSavePayload() {
  return {
    items: localItems.value.map((item) => ({
      itemId: item.itemId,
      isAdopted: item.isAdopted,
      adoptionRemark: item.isAdopted ? (item.adoptionRemark || undefined) : undefined,
    })),
  }
}

async function handleSave() {
  saving.value = true
  try {
    await saveDeptFeedback(taskId.value, buildSavePayload())
    Message.success('保存成功')
  } finally {
    saving.value = false
  }
}

function handleSubmit() {
  Modal.confirm({
    title: '确认提交',
    content: '确认提交专业部门反馈吗？提交后将无法修改。',
    okText: '确认提交',
    cancelText: '取消',
    onOk: async () => {
      submitting.value = true
      try {
        await saveDeptFeedback(taskId.value, buildSavePayload())
        await submitDeptFeedback(taskId.value)
        Message.success('专业部门反馈已提交，等待审核')
        router.push({ name: 'opinion-dept-feedback-list' })
      } finally {
        submitting.value = false
      }
    },
  })
}

function goBack() {
  router.push({ name: 'opinion-dept-feedback-list' })
}

function submitStatusColor(status: string): string {
  return OPINION_DEPT_SUBMIT_STATUS_COLOR[status as keyof typeof OPINION_DEPT_SUBMIT_STATUS_COLOR] ?? 'gray'
}

function auditStatusColor(status: string): string {
  return OPINION_DEPT_AUDIT_STATUS_COLOR[status as keyof typeof OPINION_DEPT_AUDIT_STATUS_COLOR] ?? 'gray'
}

function surveyStatusText(status: string): string {
  return OPINION_MAIN_STATUS_TEXT[status as keyof typeof OPINION_MAIN_STATUS_TEXT] ?? status
}

function surveyStatusColor(status: string): string {
  return OPINION_MAIN_STATUS_COLOR[status as keyof typeof OPINION_MAIN_STATUS_COLOR] ?? 'gray'
}

const submitStatusTextMap: Record<string, string> = OPINION_DEPT_SUBMIT_STATUS_TEXT
const auditStatusTextMap: Record<string, string> = OPINION_DEPT_AUDIT_STATUS_TEXT

function submitText(s: string): string {
  return submitStatusTextMap[s] || s
}
function auditText(s: string): string {
  return auditStatusTextMap[s] || s
}

onMounted(load)
</script>

<template>
  <section class="opinion-dept-feedback-detail">
    <header class="page-header">
      <a-button type="text" @click="goBack">
        <template #icon><IconArrowLeft /></template>
        返回列表
      </a-button>
      <h2>专业部门反馈</h2>
    </header>

    <a-spin :loading="loading" tip="加载中">
      <div v-if="detail" class="detail-content">
        <a-descriptions :column="3" :data="[]" layout="inline-vertical" class="info-block" bordered>
          <a-descriptions-item label="征集名称">{{ detail.task.surveyName }}</a-descriptions-item>
          <a-descriptions-item label="考核年份">{{ detail.task.assessYear }}年</a-descriptions-item>
          <a-descriptions-item label="征集状态">
            <a-tag :color="surveyStatusColor(detail.task.surveyStatus)" size="small">
              {{ surveyStatusText(detail.task.surveyStatus) }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="提交状态">
            <a-tag :color="submitStatusColor(detail.task.submitStatus)" size="small">
              {{ submitText(detail.task.submitStatus) }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="审核状态">
            <a-tag :color="auditStatusColor(detail.task.auditStatus)" size="small">
              {{ auditText(detail.task.auditStatus) }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="专业截止时间">{{ detail.task.deptDeadline }}</a-descriptions-item>
        </a-descriptions>

        <a-tabs v-model:active-key="activeTab" class="module-tabs">
          <a-tab-pane
            v-for="mod in detail.modules"
            :key="mod.moduleCode"
            :title="mod.moduleName"
          >
            <div class="tab-content">
              <div v-if="mod.attachments && mod.attachments.length" class="attachments-block">
                <h4>附件资料</h4>
                <ul class="attachment-list">
                  <li v-for="att in mod.attachments" :key="att.fileId" class="attachment-item">
                    <span class="att-name">{{ att.fileName }}</span>
                  </li>
                </ul>
              </div>

              <h4>反馈意见</h4>

              <div class="items-table">
                <div class="items-header">
                  <div class="col-cat">指标类别</div>
                  <div class="col-name">指标名称</div>
                  <div class="col-factor">因素名称</div>
                  <div class="col-unit">单位名称</div>
                  <div class="col-opcat">意见分类</div>
                  <div class="col-content">意见内容</div>
                  <div class="col-reason">理由</div>
                  <div class="col-adopt">是否采纳</div>
                  <div class="col-remark">采纳备注</div>
                </div>

                <div
                  v-for="(item, idx) in filteredItems(mod.moduleCode)"
                  :key="idx"
                  class="items-row"
                >
                  <div class="col-cat">
                    <span>{{ item.indicatorCategory || '-' }}</span>
                  </div>
                  <div class="col-name">
                    <span>{{ item.indicatorName || '-' }}</span>
                  </div>
                  <div class="col-factor">
                    <span>{{ item.factorName || '-' }}</span>
                  </div>
                  <div class="col-unit">
                    <span>{{ item.unitName || '-' }}</span>
                  </div>
                  <div class="col-opcat">
                    <span>{{ item.opinionCategory || '-' }}</span>
                  </div>
                  <div class="col-content">
                    <span>{{ item.opinionContent || '-' }}</span>
                  </div>
                  <div class="col-reason">
                    <span>{{ item.reason || '-' }}</span>
                  </div>
                  <div class="col-adopt">
                    <a-switch v-if="isEditMode" v-model="item.isAdopted" size="small" />
                    <a-tag v-else :color="item.isAdopted ? 'green' : 'gray'" size="small">
                      {{ item.isAdopted ? '是' : '否' }}
                    </a-tag>
                  </div>
                  <div class="col-remark">
                    <template v-if="item.isAdopted">
                      <a-textarea
                        v-if="isEditMode"
                        v-model="item.adoptionRemark"
                        placeholder="请输入采纳备注"
                        :auto-size="{ minRows: 1, maxRows: 3 }"
                      />
                      <span v-else>{{ item.adoptionRemark || '-' }}</span>
                    </template>
                    <span v-else>-</span>
                  </div>
                </div>

                <div v-if="filteredItems(mod.moduleCode).length === 0" class="items-empty">
                  暂无数据
                </div>
              </div>
            </div>
          </a-tab-pane>
        </a-tabs>

        <div v-if="isEditMode" class="bottom-actions">
          <a-space>
            <a-button type="primary" :loading="saving" @click="handleSave">
              <template #icon><IconSave /></template>
              保存
            </a-button>
            <a-button type="primary" status="success" :loading="submitting" @click="handleSubmit">
              提交
            </a-button>
            <a-button @click="goBack">返回</a-button>
          </a-space>
        </div>
      </div>
    </a-spin>
  </section>
</template>

<style scoped lang="less">
.opinion-dept-feedback-detail {
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
      h4 {
        margin: 0 0 8px;
        font-size: 14px;
        font-weight: 600;
        color: #333;
      }

      .attachments-block {
        margin-bottom: 16px;

        .attachment-list {
          margin: 0;
          padding: 0;
          list-style: none;

          .attachment-item {
            display: flex;
            align-items: center;
            gap: 6px;
            font-size: 13px;

            .att-name {
              color: #1890ff;
            }
          }
        }
      }

      .items-table {
        border: 1px solid #e8e8e8;
        border-radius: 4px;
        overflow: auto;

        .items-header,
        .items-row {
          display: grid;
          grid-template-columns: 120px 140px 120px 120px 120px 1fr 1fr 100px 1fr;
          gap: 8px;
          padding: 8px 12px;
          border-bottom: 1px solid #f0f0f0;
          align-items: flex-start;
          min-width: 1300px;
        }

        .items-header {
          background: #fafafa;
          font-weight: 500;
          color: #555;
          font-size: 13px;
          align-items: center;
        }

        .items-row {
          font-size: 13px;
          color: #333;

          &:last-child {
            border-bottom: 0;
          }

          span {
            display: block;
            padding: 4px 0;
            word-break: break-all;
          }
        }

        .items-empty {
          padding: 24px;
          text-align: center;
          color: #999;
          font-size: 13px;
        }
      }
    }
  }

  .bottom-actions {
    margin-top: 24px;
    padding-top: 16px;
    border-top: 1px solid #f0f0f0;
  }
}

:deep(.arco-tabs-content) {
  padding-top: 8px;
}
</style>
