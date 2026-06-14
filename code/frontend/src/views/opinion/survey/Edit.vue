<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Message, Modal } from '@arco-design/web-vue'
import { IconDelete, IconPlus, IconSave, IconUpload } from '@arco-design/web-vue/es/icon'
import {
  OPINION_MODULE_OPTIONS,
  OPINION_UNIT_TYPE_TEXT,
  type OpinionModuleCode,
} from '@/constants/opinion'
import {
  createSurvey,
  dictListUnits,
  getSurveyDetail,
  startSurvey,
  updateSurvey,
  uploadAttachment,
} from '@/api/opinion/survey'
import type { DictUnit, SurveySaveRequest } from '@/types/opinion/survey'

defineOptions({ name: 'OpinionSurveyEdit' })

const route = useRoute()
const router = useRouter()

const isCreate = computed(() => String(route.params.id) === 'new')
const surveyId = computed(() =>
  isCreate.value ? null : Number(route.params.id),
)

const formRef = ref()
const submitting = ref(false)
const loading = ref(false)

interface ModuleAttachmentForm {
  fileId: string
  fileName: string
  fileSize: number
}

interface ModuleRowForm {
  moduleCode: OpinionModuleCode | ''
  moduleAlias: string
  displayOrder: number
  attachments: ModuleAttachmentForm[]
}

const form = reactive<{
  name: string
  assessYear: number | undefined
  noticeContent: string
  remark: string
  unitDeadline: string
  deptDeadline: string
  targetUnitIds: number[]
  modules: ModuleRowForm[]
}>({
  name: '',
  assessYear: new Date().getFullYear(),
  noticeContent: '',
  remark: '',
  unitDeadline: '',
  deptDeadline: '',
  targetUnitIds: [],
  modules: [{ moduleCode: '', moduleAlias: '', displayOrder: 100, attachments: [] }],
})

const yearOptions = (() => {
  const now = new Date().getFullYear()
  const list: { value: number; label: string }[] = []
  for (let y = now + 1; y >= now - 5; y--) {
    list.push({ value: y, label: `${y}年` })
  }
  return list
})()

const units = ref<DictUnit[]>([])

const groupedUnits = computed(() => {
  const groups = new Map<string, DictUnit[]>()
  for (const u of units.value) {
    const key = u.unitTypeText
    if (!groups.has(key)) groups.set(key, [])
    groups.get(key)!.push(u)
  }
  return Array.from(groups.entries()).map(([typeText, list]) => ({ typeText, list }))
})

const rules = {
  name: [
    { required: true, message: '请输入征集名称' },
    { maxLength: 100, message: '征集名称最长 100 字符' },
  ],
  assessYear: [{ required: true, message: '请选择考核年份' }],
  unitDeadline: [{ required: true, message: '请选择基层截止时间' }],
  deptDeadline: [{ required: true, message: '请选择专业截止时间' }],
  targetUnitIds: [
    {
      required: true,
      message: '至少选择 1 个征集对象',
      validator: (val: number[], cb: (msg?: string) => void) => {
        if (!val || val.length === 0) {
          cb('至少选择 1 个征集对象')
          return
        }
        cb()
      },
    },
  ],
}

async function loadUnits() {
  units.value = await dictListUnits()
}

async function loadDetail() {
  if (!surveyId.value) return
  loading.value = true
  try {
    const detail = await getSurveyDetail(surveyId.value)
    form.name = detail.name
    form.assessYear = detail.assessYear
    form.noticeContent = detail.noticeContent ?? ''
    form.remark = detail.remark ?? ''
    form.unitDeadline = detail.unitDeadline
    form.deptDeadline = detail.deptDeadline
    form.targetUnitIds = detail.targets.map((t) => t.unitId)
    form.modules = detail.modules.map((m) => ({
      moduleCode: m.moduleCode,
      moduleAlias: m.moduleAlias ?? '',
      displayOrder: m.displayOrder ?? 0,
      attachments: m.attachments.map((a) => ({
        fileId: a.fileId,
        fileName: a.fileName,
        fileSize: a.fileSize,
      })),
    }))
    if (form.modules.length === 0) {
      form.modules.push({ moduleCode: '', moduleAlias: '', displayOrder: 100, attachments: [] })
    }
  } finally {
    loading.value = false
  }
}

function addModuleRow() {
  form.modules.push({
    moduleCode: '',
    moduleAlias: '',
    displayOrder: (form.modules.length + 1) * 100,
    attachments: [],
  })
}

function removeModuleRow(idx: number) {
  if (form.modules.length === 1) {
    Message.warning('至少需要保留 1 个征集模块')
    return
  }
  form.modules.splice(idx, 1)
}

async function handleUpload(idx: number, fileItem: { file?: File }) {
  const file = fileItem?.file
  if (!file) return false
  const fd = new FormData()
  fd.append('file', file)
  const res = await uploadAttachment(fd)
  const target = form.modules[idx]
  if (!target) return false
  target.attachments.push({
    fileId: res.fileId,
    fileName: res.fileName,
    fileSize: res.fileSize,
  })
  return false // 阻止 arco 默认上传
}

function removeAttachment(modIdx: number, attIdx: number) {
  const target = form.modules[modIdx]
  if (!target) return
  target.attachments.splice(attIdx, 1)
}

function buildPayload(): SurveySaveRequest {
  return {
    name: form.name.trim(),
    assessYear: form.assessYear as number,
    noticeContent: form.noticeContent || null,
    remark: form.remark || null,
    unitDeadline: form.unitDeadline,
    deptDeadline: form.deptDeadline,
    targets: form.targetUnitIds.map((unitId) => ({ unitId })),
    modules: form.modules
      .filter((m) => m.moduleCode)
      .map((m) => ({
        moduleCode: m.moduleCode as OpinionModuleCode,
        moduleAlias: m.moduleAlias || null,
        displayOrder: m.displayOrder ?? 0,
        attachments: m.attachments,
      })),
  }
}

function validateBeforeSubmit(): string | null {
  if (form.modules.filter((m) => m.moduleCode).length === 0) {
    return '至少配置 1 个征集模块'
  }
  if (!form.unitDeadline || !form.deptDeadline) return null
  if (new Date(form.deptDeadline) <= new Date(form.unitDeadline)) {
    return '专业截止时间必须晚于基层截止时间'
  }
  return null
}

async function handleSave() {
  const validation = await formRef.value?.validate()
  if (validation) return
  const customErr = validateBeforeSubmit()
  if (customErr) {
    Message.warning(customErr)
    return
  }
  submitting.value = true
  try {
    const payload = buildPayload()
    if (isCreate.value) {
      const res = await createSurvey(payload)
      Message.success('保存成功')
      router.replace({ name: 'opinion-survey-edit', params: { id: String(res.id) } })
    } else {
      await updateSurvey(surveyId.value as number, payload)
      Message.success('保存成功')
    }
  } finally {
    submitting.value = false
  }
}

function handleStart() {
  Modal.confirm({
    title: '确认开启征集',
    content: '确认开启征集吗？开启后不可再编辑意见征集信息。',
    okText: '开启',
    cancelText: '取消',
    onOk: async () => {
      if (isCreate.value) {
        Message.warning('请先保存草稿后再开启')
        return
      }
      // 先保存再开启
      const validation = await formRef.value?.validate()
      if (validation) return
      const customErr = validateBeforeSubmit()
      if (customErr) {
        Message.warning(customErr)
        return
      }
      try {
        await updateSurvey(surveyId.value as number, buildPayload())
        const res = await startSurvey(surveyId.value as number)
        Message.success(`征集已开启，已初始化 ${res.unitTaskCount} 个基层任务`)
        router.replace({ name: 'opinion-survey-list' })
      } catch {
        // 错误已由 request 拦截器统一提示
      }
    },
  })
}

function handleCancel() {
  router.push({ name: 'opinion-survey-list' })
}

onMounted(async () => {
  await loadUnits()
  await loadDetail()
})

const breadcrumbTitle = computed(() => (isCreate.value ? '新增意见征集' : '编辑意见征集'))
</script>

<template>
  <section class="opinion-survey-edit">
    <header class="page-header">
      <h2>{{ breadcrumbTitle }}</h2>
    </header>

    <a-spin :loading="loading" tip="加载中">
      <a-form ref="formRef" :model="form" :rules="rules" auto-label-width>
        <a-form-item label="意见征集名称" field="name" required>
          <a-input v-model="form.name" placeholder="请输入" :max-length="100" show-word-limit />
        </a-form-item>

        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="考核周期" field="assessYear" required>
              <a-select v-model="form.assessYear" placeholder="请选择">
                <a-option
                  v-for="y in yearOptions"
                  :key="y.value"
                  :value="y.value"
                  :label="y.label"
                />
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="基层截止时间" field="unitDeadline" required>
              <a-date-picker
                v-model="form.unitDeadline"
                show-time
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
                placeholder="请选择"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="专业截止时间" field="deptDeadline" required>
              <a-date-picker
                v-model="form.deptDeadline"
                show-time
                value-format="YYYY-MM-DD HH:mm:ss"
                style="width: 100%"
                placeholder="请选择"
              />
            </a-form-item>
          </a-col>
        </a-row>

        <a-form-item label="征集对象" field="targetUnitIds" required>
          <a-checkbox-group v-model="form.targetUnitIds">
            <div v-for="grp in groupedUnits" :key="grp.typeText" class="unit-group">
              <div class="unit-group-title">
                {{ grp.typeText }}（{{ grp.list.length }} 家）
              </div>
              <div class="unit-group-items">
                <a-checkbox v-for="u in grp.list" :key="u.unitId" :value="u.unitId">
                  {{ u.unitName }}
                </a-checkbox>
              </div>
            </div>
          </a-checkbox-group>
        </a-form-item>

        <a-form-item label="征集模块" required>
          <div class="module-table">
            <div class="module-row module-header">
              <div class="col-module">指标模块</div>
              <div class="col-alias">模块别称</div>
              <div class="col-order">排序</div>
              <div class="col-att">附件</div>
              <div class="col-action">操作</div>
            </div>
            <div v-for="(m, idx) in form.modules" :key="idx" class="module-row">
              <div class="col-module">
                <a-select v-model="m.moduleCode" placeholder="请选择" style="width: 100%">
                  <a-option
                    v-for="opt in OPINION_MODULE_OPTIONS"
                    :key="opt.code"
                    :value="opt.code"
                    :label="opt.text"
                  />
                </a-select>
              </div>
              <div class="col-alias">
                <a-input v-model="m.moduleAlias" placeholder="可选" :max-length="50" />
              </div>
              <div class="col-order">
                <a-input-number v-model="m.displayOrder" placeholder="排序" :min="0" />
              </div>
              <div class="col-att">
                <a-upload
                  :auto-upload="false"
                  :show-file-list="false"
                  @before-upload="(file: File) => handleUpload(idx, { file })"
                >
                  <template #upload-button>
                    <a-button size="small">
                      <template #icon><IconUpload /></template>
                      上传附件
                    </a-button>
                  </template>
                </a-upload>
                <ul class="attachment-list">
                  <li v-for="(a, ai) in m.attachments" :key="a.fileId">
                    <span class="att-name" :title="a.fileName">{{ a.fileName }}</span>
                    <a-button type="text" size="mini" status="danger" @click="removeAttachment(idx, ai)">
                      <template #icon><IconDelete /></template>
                    </a-button>
                  </li>
                </ul>
              </div>
              <div class="col-action">
                <a-button type="text" size="small" status="danger" @click="removeModuleRow(idx)">
                  <template #icon><IconDelete /></template>
                  删除
                </a-button>
              </div>
            </div>
          </div>
          <div class="add-module">
            <a-button size="small" @click="addModuleRow">
              <template #icon><IconPlus /></template>
              添加模块
            </a-button>
          </div>
        </a-form-item>

        <a-form-item label="通知内容">
          <a-textarea v-model="form.noticeContent" placeholder="可选" :max-length="1000" show-word-limit :auto-size="{ minRows: 3, maxRows: 6 }" />
        </a-form-item>

        <a-form-item label="备注">
          <a-textarea v-model="form.remark" placeholder="可选" :max-length="1000" show-word-limit :auto-size="{ minRows: 2, maxRows: 4 }" />
        </a-form-item>

        <a-form-item>
          <a-space>
            <a-button type="primary" :loading="submitting" @click="handleSave">
              <template #icon><IconSave /></template>
              保存
            </a-button>
            <a-button v-if="!isCreate" type="primary" status="success" @click="handleStart">
              开启征集
            </a-button>
            <a-button @click="handleCancel">取消</a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </a-spin>
  </section>
</template>

<style scoped lang="less">
.opinion-survey-edit {
  background: #fff;
  padding: 16px 24px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);

  .page-header {
    margin-bottom: 16px;
    border-bottom: 1px solid #f0f0f0;
    padding-bottom: 8px;

    h2 {
      margin: 0;
      font-size: 16px;
      font-weight: 600;
    }
  }

  .unit-group {
    margin-bottom: 12px;

    .unit-group-title {
      font-weight: 500;
      color: #333;
      margin-bottom: 4px;
      font-size: 13px;
    }

    .unit-group-items {
      display: flex;
      flex-wrap: wrap;
      gap: 6px 18px;
    }
  }

  .module-table {
    border: 1px solid #e8e8e8;
    border-radius: 4px;

    .module-row {
      display: grid;
      grid-template-columns: 200px 200px 100px 1fr 120px;
      gap: 12px;
      padding: 8px 12px;
      border-bottom: 1px solid #f0f0f0;
      align-items: flex-start;

      &.module-header {
        background: #fafafa;
        font-weight: 500;
        color: #555;
        font-size: 13px;
        align-items: center;
      }

      &:last-child {
        border-bottom: 0;
      }

      .col-att {
        .attachment-list {
          margin: 6px 0 0;
          padding: 0;
          list-style: none;
          font-size: 12px;
          color: #555;

          li {
            display: flex;
            align-items: center;
            gap: 6px;
          }

          .att-name {
            max-width: 220px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }
        }
      }
    }
  }

  .add-module {
    margin-top: 8px;
  }
}
</style>
