<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Message, Modal } from '@arco-design/web-vue'
import { IconArrowLeft, IconPlus, IconDelete, IconSave } from '@arco-design/web-vue/es/icon'
import {
  OPINION_UNIT_FILL_STATUS_COLOR,
  OPINION_UNIT_AUDIT_STATUS_COLOR,
} from '@/constants/opinion'
import {
  getUnitTaskDetail,
  saveUnitOpinion,
  submitUnitOpinion,
} from '@/api/opinion/unit-fill'
import type {
  UnitFillDetail,
} from '@/types/opinion/unit-fill'

interface UnitFillItemEdit {
  id?: number
  moduleCode: string
  indicatorCategory?: string
  indicatorName?: string
  factorName?: string
  extraField?: string
  opinionCategory?: string
  opinionContent?: string
  reason?: string
  displayOrder?: number
}

defineOptions({ name: 'OpinionUnitFillDetail' })

const route = useRoute()
const router = useRouter()

const taskId = computed(() => Number(route.params.taskId))

const detail = ref<UnitFillDetail | null>(null)
const loading = ref(false)
const saving = ref(false)
const submitting = ref(false)
const activeTab = ref('')

const isEditMode = computed(() => {
  if (!detail.value) return false
  const { fillStatus, surveyStatus } = detail.value.task
  return fillStatus === 'PENDING' && surveyStatus !== 'PUBLISHED'
})

const isPublished = computed(() => {
  return detail.value?.task.surveyStatus === 'PUBLISHED'
})

const localItems = ref<UnitFillItemEdit[]>([])

function filteredItems(moduleCode: string): UnitFillItemEdit[] {
  return localItems.value.filter((item) => item.moduleCode === moduleCode)
}

function adjustedItems(moduleCode: string): UnitFillItemEdit[] {
  if (!detail.value?.adjustedItems) return []
  return detail.value.adjustedItems.filter((item) => item.moduleCode === moduleCode) as UnitFillItemEdit[]
}

async function load() {
  loading.value = true
  try {
    const res = await getUnitTaskDetail(taskId.value)
    detail.value = res
    localItems.value = (res.items || []).map<UnitFillItemEdit>((item) => ({
      id: item.id,
      moduleCode: item.moduleCode,
      indicatorCategory: item.indicatorCategory ?? undefined,
      indicatorName: item.indicatorName ?? undefined,
      factorName: item.factorName ?? undefined,
      extraField: item.extraField ?? undefined,
      opinionCategory: item.opinionCategory ?? undefined,
      opinionContent: item.opinionContent ?? undefined,
      reason: item.reason ?? undefined,
      displayOrder: item.displayOrder,
    }))
    if (res.modules.length > 0 && res.modules[0]) {
      activeTab.value = res.modules[0].moduleCode
    }
  } finally {
    loading.value = false
  }
}

function addItem(moduleCode: string) {
  localItems.value.push({
    moduleCode,
    indicatorCategory: undefined,
    indicatorName: undefined,
    factorName: undefined,
    extraField: undefined,
    opinionCategory: undefined,
    opinionContent: undefined,
    reason: undefined,
    displayOrder: localItems.value.filter((i) => i.moduleCode === moduleCode).length + 1,
  })
}

function removeItem(index: number) {
  localItems.value.splice(index, 1)
}

function buildSavePayload(): UnitFillItemEdit[] {
  return localItems.value
}

async function handleSave() {
  saving.value = true
  try {
    await saveUnitOpinion(taskId.value, { items: buildSavePayload() })
    Message.success('保存成功')
  } finally {
    saving.value = false
  }
}

function handleSubmit() {
  Modal.confirm({
    title: '确认提交',
    content: '确认提交意见征集吗？提交后将无法修改。',
    okText: '确认提交',
    cancelText: '取消',
    onOk: async () => {
      submitting.value = true
      try {
        await saveUnitOpinion(taskId.value, { items: buildSavePayload() })
        await submitUnitOpinion(taskId.value)
        Message.success('意见征集已提交，等待审核')
        router.push({ name: 'opinion-unit-fill-list' })
      } finally {
        submitting.value = false
      }
    },
  })
}

function goBack() {
  router.push({ name: 'opinion-unit-fill-list' })
}

function handleExport() {
  window.open(`/api/opinion/unit-fill/${taskId.value}/export`)
}

function fillStatusColor(status: string): string {
  return OPINION_UNIT_FILL_STATUS_COLOR[status as keyof typeof OPINION_UNIT_FILL_STATUS_COLOR] ?? 'gray'
}

function auditStatusColor(status: string): string {
  return OPINION_UNIT_AUDIT_STATUS_COLOR[status as keyof typeof OPINION_UNIT_AUDIT_STATUS_COLOR] ?? 'gray'
}

onMounted(load)
</script>

<template>
  <section class="opinion-unit-fill-detail">
    <header class="page-header">
      <a-button type="text" @click="goBack">
        <template #icon><IconArrowLeft /></template>
        返回列表
      </a-button>
      <h2>基层单位意见征集</h2>
      <a-button size="small" @click="handleExport">导出CSV</a-button>
    </header>

    <a-spin :loading="loading" tip="加载中">
      <div v-if="detail" class="detail-content">
        <a-descriptions :column="3" :data="[]" layout="inline-vertical" class="info-block" bordered>
          <a-descriptions-item label="征集名称">{{ detail.task.surveyName }}</a-descriptions-item>
          <a-descriptions-item label="考核年份">{{ detail.task.assessYear }}年</a-descriptions-item>
          <a-descriptions-item label="征集状态">
            <a-tag :color="fillStatusColor(detail.task.surveyStatus)" size="small">
              {{ detail.task.surveyStatusText }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="填报状态">
            <a-tag :color="fillStatusColor(detail.task.fillStatus)" size="small">
              {{ detail.task.fillStatusText }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="审核状态">
            <a-tag :color="auditStatusColor(detail.task.auditStatus)" size="small">
              {{ detail.task.auditStatusText }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="基层截止时间">{{ detail.task.unitDeadline }}</a-descriptions-item>
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

              <h4>{{ isEditMode ? '意见建议' : '已提交意见' }}</h4>

              <div class="items-table">
                <div class="items-header">
                  <div class="col-cat">指标类别</div>
                  <div class="col-name">指标名称</div>
                  <div class="col-factor">因素名称</div>
                  <div class="col-extra">扩展字段</div>
                  <div class="col-opcat">意见分类</div>
                  <div class="col-content">意见内容</div>
                  <div class="col-reason">理由</div>
                  <div v-if="isEditMode" class="col-del">操作</div>
                </div>

                <div
                  v-for="(item, idx) in filteredItems(mod.moduleCode)"
                  :key="idx"
                  class="items-row"
                >
                  <div class="col-cat">
                    <a-input
                      v-if="isEditMode"
                      v-model="item.indicatorCategory"
                      placeholder="请输入"
                      size="small"
                    />
                    <span v-else>{{ item.indicatorCategory || '-' }}</span>
                  </div>
                  <div class="col-name">
                    <a-input
                      v-if="isEditMode"
                      v-model="item.indicatorName"
                      placeholder="请输入"
                      size="small"
                    />
                    <span v-else>{{ item.indicatorName || '-' }}</span>
                  </div>
                  <div class="col-factor">
                    <a-input
                      v-if="isEditMode"
                      v-model="item.factorName"
                      placeholder="请输入"
                      size="small"
                    />
                    <span v-else>{{ item.factorName || '-' }}</span>
                  </div>
                  <div class="col-extra">
                    <a-input
                      v-if="isEditMode"
                      v-model="item.extraField"
                      placeholder="请输入"
                      size="small"
                    />
                    <span v-else>{{ item.extraField || '-' }}</span>
                  </div>
                  <div class="col-opcat">
                    <a-input
                      v-if="isEditMode"
                      v-model="item.opinionCategory"
                      placeholder="请输入"
                      size="small"
                    />
                    <span v-else>{{ item.opinionCategory || '-' }}</span>
                  </div>
                  <div class="col-content">
                    <a-textarea
                      v-if="isEditMode"
                      v-model="item.opinionContent"
                      placeholder="请输入"
                      :auto-size="{ minRows: 1, maxRows: 3 }"
                    />
                    <span v-else>{{ item.opinionContent || '-' }}</span>
                  </div>
                  <div class="col-reason">
                    <a-textarea
                      v-if="isEditMode"
                      v-model="item.reason"
                      placeholder="请输入"
                      :auto-size="{ minRows: 1, maxRows: 3 }"
                    />
                    <span v-else>{{ item.reason || '-' }}</span>
                  </div>
                  <div v-if="isEditMode" class="col-del">
                    <a-button
                      type="text"
                      size="small"
                      status="danger"
                      @click="removeItem(localItems.indexOf(item))"
                    >
                      <template #icon><IconDelete /></template>
                      删除
                    </a-button>
                  </div>
                </div>

                <div v-if="filteredItems(mod.moduleCode).length === 0" class="items-empty">
                  暂无数据
                </div>
              </div>

              <div v-if="isEditMode" class="add-item-bar">
                <a-button size="small" @click="addItem(mod.moduleCode)">
                  <template #icon><IconPlus /></template>
                  添加意见建议
                </a-button>
              </div>

              <div
                v-if="isPublished && adjustedItems(mod.moduleCode).length > 0"
                class="adjusted-block"
              >
                <h4 class="adjusted-title">调整后意见</h4>
                <div class="items-table">
                  <div class="items-header adjusted-header">
                    <div class="col-cat">指标类别</div>
                    <div class="col-name">指标名称</div>
                    <div class="col-factor">因素名称</div>
                    <div class="col-extra">扩展字段</div>
                    <div class="col-opcat">意见分类</div>
                    <div class="col-content">意见内容</div>
                    <div class="col-reason">理由</div>
                  </div>
                  <div
                    v-for="(item, ai) in adjustedItems(mod.moduleCode)"
                    :key="ai"
                    class="items-row"
                  >
                    <div class="col-cat"><span>{{ item.indicatorCategory || '-' }}</span></div>
                    <div class="col-name"><span>{{ item.indicatorName || '-' }}</span></div>
                    <div class="col-factor"><span>{{ item.factorName || '-' }}</span></div>
                    <div class="col-extra"><span>{{ item.extraField || '-' }}</span></div>
                    <div class="col-opcat"><span>{{ item.opinionCategory || '-' }}</span></div>
                    <div class="col-content"><span>{{ item.opinionContent || '-' }}</span></div>
                    <div class="col-reason"><span>{{ item.reason || '-' }}</span></div>
                  </div>
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
.opinion-unit-fill-detail {
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
          grid-template-columns: 120px 140px 120px 120px 120px 1fr 1fr 80px;
          gap: 8px;
          padding: 8px 12px;
          border-bottom: 1px solid #f0f0f0;
          align-items: flex-start;
          min-width: 1100px;
        }

        .items-header {
          background: #fafafa;
          font-weight: 500;
          color: #555;
          font-size: 13px;
          align-items: center;

          &.adjusted-header {
            grid-template-columns: 120px 140px 120px 120px 120px 1fr 1fr;
          }
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

      .add-item-bar {
        margin-top: 8px;
      }

      .adjusted-block {
        margin-top: 24px;

        .adjusted-title {
          color: #1890ff;
          margin: 0 0 8px;
          font-size: 14px;
          font-weight: 600;
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
