<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { IconArrowLeft } from '@arco-design/web-vue/es/icon'
import { statusColor } from '@/constants/opinion'
import { getSurveyDetail } from '@/api/opinion/survey'
import type { SurveyDetail } from '@/types/opinion/survey'

defineOptions({ name: 'OpinionSurveyView' })

const route = useRoute()
const router = useRouter()

const detail = ref<SurveyDetail | null>(null)
const loading = ref(false)

const surveyId = computed(() => Number(route.params.id))

async function load() {
  loading.value = true
  try {
    detail.value = await getSurveyDetail(surveyId.value)
  } finally {
    loading.value = false
  }
}

const groupedTargets = computed(() => {
  if (!detail.value) return []
  const groups = new Map<string, typeof detail.value.targets>()
  for (const t of detail.value.targets) {
    const key = t.unitTypeText
    if (!groups.has(key)) groups.set(key, [])
    groups.get(key)!.push(t)
  }
  return Array.from(groups.entries()).map(([typeText, list]) => ({ typeText, list }))
})

onMounted(load)
</script>

<template>
  <section class="opinion-survey-view">
    <header class="page-header">
      <a-button type="text" @click="router.push({ name: 'opinion-survey-list' })">
        <template #icon><IconArrowLeft /></template>
        返回列表
      </a-button>
      <h2>查看意见征集</h2>
    </header>

    <a-spin :loading="loading" tip="加载中">
      <div v-if="detail" class="detail-content">
        <a-descriptions :column="2" :data="[]" layout="inline-vertical" class="info-block" bordered>
          <a-descriptions-item label="征集名称">{{ detail.name }}</a-descriptions-item>
          <a-descriptions-item label="考核周期">{{ detail.assessYear }}年</a-descriptions-item>
          <a-descriptions-item label="状态">
            <a-tag :color="statusColor(detail.status)" size="small">{{ detail.statusText }}</a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="基层截止时间">{{ detail.unitDeadline }}</a-descriptions-item>
          <a-descriptions-item label="专业截止时间">{{ detail.deptDeadline }}</a-descriptions-item>
          <a-descriptions-item label="开启时间">{{ detail.startAt || '-' }}</a-descriptions-item>
          <a-descriptions-item label="发布时间">{{ detail.publishAt || '-' }}</a-descriptions-item>
          <a-descriptions-item label="创建时间">{{ detail.createdAt }}</a-descriptions-item>
        </a-descriptions>

        <div class="block">
          <h3>征集对象（{{ detail.targets.length }} 家）</h3>
          <div v-for="grp in groupedTargets" :key="grp.typeText" class="unit-group">
            <div class="unit-group-title">
              {{ grp.typeText }}（{{ grp.list.length }} 家）
            </div>
            <div class="unit-group-items">
              <a-tag v-for="u in grp.list" :key="u.unitId">{{ u.unitName }}</a-tag>
            </div>
          </div>
        </div>

        <div class="block">
          <h3>征集模块（{{ detail.modules.length }} 个）</h3>
          <a-table :data="detail.modules" :pagination="false" stripe row-key="id">
            <template #columns>
              <a-table-column title="模块名" data-index="moduleName" />
              <a-table-column title="别称" data-index="moduleAlias">
                <template #cell="{ record }">{{ record.moduleAlias || '-' }}</template>
              </a-table-column>
              <a-table-column title="排序" data-index="displayOrder" :width="80" align="center" />
              <a-table-column title="附件">
                <template #cell="{ record }">
                  <ul v-if="record.attachments && record.attachments.length" class="attachment-list">
                    <li v-for="a in record.attachments" :key="a.id">
                      <span class="att-name" :title="a.fileName">{{ a.fileName }}</span>
                    </li>
                  </ul>
                  <span v-else>-</span>
                </template>
              </a-table-column>
            </template>
          </a-table>
        </div>

        <div v-if="detail.noticeContent" class="block">
          <h3>通知内容</h3>
          <pre class="text-block">{{ detail.noticeContent }}</pre>
        </div>

        <div v-if="detail.remark" class="block">
          <h3>备注</h3>
          <pre class="text-block">{{ detail.remark }}</pre>
        </div>
      </div>
    </a-spin>
  </section>
</template>

<style scoped lang="less">
.opinion-survey-view {
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

  .block {
    margin-bottom: 24px;

    h3 {
      margin: 0 0 8px;
      font-size: 14px;
      font-weight: 600;
      color: #333;
    }

    .unit-group {
      margin-bottom: 8px;

      .unit-group-title {
        font-size: 13px;
        color: #555;
        margin-bottom: 4px;
      }

      .unit-group-items {
        display: flex;
        flex-wrap: wrap;
        gap: 6px;
      }
    }

    .text-block {
      background: #fafafa;
      padding: 8px 12px;
      border-radius: 4px;
      white-space: pre-wrap;
      font-size: 13px;
      color: #333;
      line-height: 1.6;
      border: 1px solid #f0f0f0;
      margin: 0;
    }

    .attachment-list {
      margin: 0;
      padding: 0;
      list-style: none;

      li {
        display: flex;
        align-items: center;
        gap: 6px;
        font-size: 13px;
      }

      .att-name {
        color: #1890ff;
      }
    }
  }
}
</style>
