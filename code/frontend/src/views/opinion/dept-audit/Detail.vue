<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Message, Modal } from '@arco-design/web-vue'
import { IconArrowLeft } from '@arco-design/web-vue/es/icon'
import { getDeptAuditDetail, passDeptAudit, rejectDeptAudit } from '@/api/opinion/dept-audit'
import type { DeptAuditLog, DeptAuditModule, DeptAuditTask, DeptAuditDetail } from '@/types/opinion/dept-audit'
import {
  OPINION_DEPT_SUBMIT_STATUS_TEXT,
  OPINION_DEPT_AUDIT_STATUS_TEXT,
} from '@/constants/opinion'

defineOptions({ name: 'OpinionDeptAuditDetail' })

const deptSubmitText = OPINION_DEPT_SUBMIT_STATUS_TEXT
const deptAuditText = OPINION_DEPT_AUDIT_STATUS_TEXT

function submitText(s: string): string {
  return (deptSubmitText as Record<string, string>)[s] || s
}
function auditText(s: string): string {
  return (deptAuditText as Record<string, string>)[s] || s
}

const route = useRoute()
const router = useRouter()

const detail = ref<DeptAuditDetail | null>(null)
const loading = ref(false)

const activeTab = ref('')
const passLoading = ref(false)
const rejectLoading = ref(false)

const rejectReason = ref('')
const showRejectModal = ref(false)

const taskId = computed(() => Number(route.params.taskId))

async function load() {
  loading.value = true
  try {
    const data = await getDeptAuditDetail(taskId.value)
    detail.value = data
    if (data.modules.length > 0) {
      activeTab.value = data.modules[0]!.moduleCode
    }
  } finally {
    loading.value = false
  }
}

const task = computed<DeptAuditTask | null>(() => detail.value?.task ?? null)
const modules = computed<DeptAuditModule[]>(() => detail.value?.modules ?? [])
const auditLogs = computed<DeptAuditLog[]>(() => detail.value?.auditLogs ?? [])

const isPending = computed(() => task.value?.auditStatus === 'PENDING')

function itemsByModule(code: string) {
  return (detail.value?.items ?? []).filter((i) => i.moduleCode === code)
}

const auditLogColumns = computed(() => [
  { title: '操作', dataIndex: 'action', width: 100 },
  { title: '操作角色', dataIndex: 'actorRole', width: 120 },
  { title: '退回原因', slotName: 'rejectReason', width: 260 },
  { title: '操作时间', dataIndex: 'createdAt', width: 180 },
])

function fillStatusColor(status: string) {
  const map: Record<string, string> = { PENDING: 'orange', SUBMITTED: 'green' }
  return map[status] || 'gray'
}

function auditStatusColor(status: string) {
  const map: Record<string, string> = { PENDING: 'orange', PASS: 'green', REJECTED: 'red' }
  return map[status] || 'gray'
}

function handlePass() {
  Modal.confirm({
    title: '确认审核通过',
    content: '确认该专业部门的反馈审核通过？通过后反馈状态将变为已完成。',
    okText: '审核通过',
    cancelText: '取消',
    onOk: async () => {
      passLoading.value = true
      try {
        await passDeptAudit(taskId.value)
        Message.success('审核通过！')
        load()
      } catch {
        Message.error('操作失败，请重试')
      } finally {
        passLoading.value = false
      }
    },
  })
}

function openRejectModal() {
  rejectReason.value = ''
  showRejectModal.value = true
}

async function handleRejectConfirm() {
  if (!rejectReason.value.trim()) {
    Message.warning('请填写退回原因')
    return
  }
  rejectLoading.value = true
  try {
    await rejectDeptAudit(taskId.value, { rejectReason: rejectReason.value.trim() })
    Message.success('已退回！')
    showRejectModal.value = false
    load()
  } catch {
    Message.error('操作失败，请重试')
  } finally {
    rejectLoading.value = false
  }
}

function goBack() {
  router.push({ name: 'opinion-dept-audit-list' })
}

onMounted(load)
</script>

<template>
  <section class="opinion-dept-audit-detail">
    <header class="page-header">
      <a-button type="text" @click="goBack">
        <template #icon><IconArrowLeft /></template>
        返回列表
      </a-button>
      <h2>专业部门反馈审核</h2>
    </header>

    <a-spin :loading="loading" tip="加载中">
      <div v-if="detail" class="detail-content">
        <a-descriptions :column="2" :data="[]" layout="inline-vertical" class="info-block" bordered>
          <a-descriptions-item label="征集名称">{{ task?.surveyName }}</a-descriptions-item>
          <a-descriptions-item label="考核年份">{{ task?.assessYear }}年</a-descriptions-item>
          <a-descriptions-item label="提交时间">{{ task?.submittedAt || '-' }}</a-descriptions-item>
          <a-descriptions-item label="专业截止时间">{{ task?.deptDeadline || '-' }}</a-descriptions-item>
          <a-descriptions-item label="填报状态">
            <a-tag v-if="task" :color="fillStatusColor(task.submitStatus)" size="small">{{ submitText(task.submitStatus) }}</a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="审核状态">
            <a-tag v-if="task" :color="auditStatusColor(task.auditStatus)" size="small">{{ auditText(task.auditStatus) }}</a-tag>
          </a-descriptions-item>
        </a-descriptions>

        <div v-if="task?.lastRejectReason" class="reject-reason-bar">
          <span class="reject-label">上次退回原因：</span>
          <span class="reject-text">{{ task.lastRejectReason }}</span>
        </div>

        <div class="block">
          <h3>征集模块</h3>
          <a-tabs v-model:active-key="activeTab" type="card-gutter">
            <a-tab-pane
              v-for="mod in modules"
              :key="mod.moduleCode"
              :title="mod.moduleName"
            >
              <div v-if="mod.attachments.length" class="attachments-section">
                <span class="att-label">附件：</span>
                <span v-for="a in mod.attachments" :key="a.fileId" class="att-name">{{ a.fileName }}</span>
              </div>

              <a-table
                :data="itemsByModule(mod.moduleCode)"
                :pagination="false"
                stripe
                row-key="itemId"
                size="small"
                class="items-table"
              >
                <template #columns>
                  <a-table-column title="序号" :width="70" align="center">
                    <template #cell="{ rowIndex }">{{ rowIndex + 1 }}</template>
                  </a-table-column>
                  <a-table-column title="指标类别" data-index="indicatorCategory">
                    <template #cell="{ record }">{{ record.indicatorCategory || '-' }}</template>
                  </a-table-column>
                  <a-table-column title="指标名称" data-index="indicatorName">
                    <template #cell="{ record }">{{ record.indicatorName || '-' }}</template>
                  </a-table-column>
                  <a-table-column title="要素" data-index="factorName">
                    <template #cell="{ record }">{{ record.factorName || '-' }}</template>
                  </a-table-column>
                  <a-table-column title="单位名称" data-index="unitName">
                    <template #cell="{ record }">{{ record.unitName || '-' }}</template>
                  </a-table-column>
                  <a-table-column title="意见类别" data-index="opinionCategory">
                    <template #cell="{ record }">{{ record.opinionCategory || '-' }}</template>
                  </a-table-column>
                  <a-table-column title="意见内容" data-index="opinionContent">
                    <template #cell="{ record }">{{ record.opinionContent || '-' }}</template>
                  </a-table-column>
                  <a-table-column title="理由" data-index="reason">
                    <template #cell="{ record }">{{ record.reason || '-' }}</template>
                  </a-table-column>
                  <a-table-column title="是否采纳">
                    <template #cell="{ record }">
                      <a-tag :color="record.isAdopted ? 'green' : 'gray'" size="small">
                        {{ record.isAdopted ? '是' : '否' }}
                      </a-tag>
                    </template>
                  </a-table-column>
                  <a-table-column title="采纳备注" data-index="adoptionRemark">
                    <template #cell="{ record }">{{ record.adoptionRemark || '-' }}</template>
                  </a-table-column>
                </template>
              </a-table>
            </a-tab-pane>
          </a-tabs>
        </div>

        <div class="block">
          <h3>审核日志</h3>
          <a-table
            :columns="auditLogColumns"
            :data="auditLogs"
            :pagination="false"
            stripe
            row-key="id"
            size="small"
          >
            <template #rejectReason="{ record }">{{ record.rejectReason || '-' }}</template>
          </a-table>
        </div>

        <div v-if="isPending" class="action-footer">
          <a-space>
            <a-button type="primary" :loading="passLoading" @click="handlePass">审核通过</a-button>
            <a-button status="danger" :loading="rejectLoading" @click="openRejectModal">退回</a-button>
          </a-space>
        </div>
      </div>
    </a-spin>

    <a-modal
      v-model:visible="showRejectModal"
      title="退回审核"
      :ok-text="'确认退回'"
      :cancel-text="'取消'"
      :ok-loading="rejectLoading"
      @ok="handleRejectConfirm"
    >
      <a-form-item label="退回原因" required>
        <a-textarea
          v-model="rejectReason"
          placeholder="请输入退回原因（必填，最多260字）"
          :max-length="260"
          show-word-limit
          :auto-size="{ minRows: 3, maxRows: 6 }"
        />
      </a-form-item>
    </a-modal>
  </section>
</template>

<style scoped lang="less">
.opinion-dept-audit-detail {
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
    margin-bottom: 12px;
  }

  .reject-reason-bar {
    margin-bottom: 16px;
    padding: 8px 12px;
    background: #fff7e6;
    border: 1px solid #ffd591;
    border-radius: 4px;
    font-size: 13px;

    .reject-label {
      color: #d46b08;
      font-weight: 500;
    }

    .reject-text {
      color: #333;
    }
  }

  .block {
    margin-bottom: 24px;

    h3 {
      margin: 0 0 8px;
      font-size: 14px;
      font-weight: 600;
      color: #333;
    }

    .attachments-section {
      margin-bottom: 12px;
      font-size: 13px;

      .att-label {
        color: #555;
      }

      .att-name {
        color: #1890ff;
        margin-right: 12px;
      }
    }

    .items-table {
      margin-top: 8px;
    }
  }

  .action-footer {
    margin-top: 16px;
    padding-top: 16px;
    border-top: 1px solid #f0f0f0;
  }
}
</style>
