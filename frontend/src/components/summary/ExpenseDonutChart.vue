<template>
  <div class="stats-detail-container">
    <div class="type-selector">
      <button :class="{ active: currentType === 'expense' }" @click="loadChart('expense')">지출</button>
      <button :class="{ active: currentType === 'income' }" @click="loadChart('income')">수입</button>
    </div>

    <div class="chart-section" ref="chartArea">
      <canvas ref="chartCanvas"></canvas>
    </div>

    <div class="list-section">
      <ul class="category-list" v-if="summary.length > 0">
        <li v-for="(item, index) in summary" :key="item.category" class="list-item">
          <div class="cat-left">
            <span class="dot" :style="{ backgroundColor: colors[index] }"></span>
            <span class="name">{{ item.category }}</span>
            <span class="percent">{{ getPercent(item.total) }}%</span>
          </div>
          <div class="cat-right">
            <span class="amount">{{ item.total.toLocaleString() }}원</span>
          </div>
        </li>
      </ul>
      <div v-else class="empty-msg">내역이 없습니다.</div>
    </div>
  </div>
</template>


<script setup lang="ts">
import { ref, onUnmounted, watch, nextTick } from 'vue'
import { Chart, DoughnutController, ArcElement, Tooltip } from 'chart.js'
import instance from '@/api/axiosInstance'

// Props 정의
const props = defineProps<{
  visible: boolean
  year: number
  month: number
}>()

// Emit 정의 (월 변경 시 부모의 상태를 바꾸기 위함)
const emit = defineEmits(['change-month'])

Chart.register(DoughnutController, ArcElement, Tooltip)

interface CategorySummary {
  category: string
  total: number
}

const chartCanvas = ref<HTMLCanvasElement | null>(null)
const chartArea = ref<HTMLDivElement | null>(null)
let chartInstance: Chart | null = null

const summary = ref<CategorySummary[]>([])
const colors = ref<string[]>([])
const totalAmount = ref(0)
const currentType = ref<'expense' | 'income'>('expense')

const palette = ['#4DABF7', '#63E6BE', '#FF8787', '#B197FC', '#FFD93D', '#495057']
const generateColor = (i: number) => palette[i % palette.length]

const getPercent = (v: number) =>
  totalAmount.value ? ((v / totalAmount.value) * 100).toFixed(1) : '0.0'

// 부모에게 월 변경 알림
const emitMonthChange = (diff: number) => {
  emit('change-month', diff)
}

const waitUntilVisibleSized = async () => {
  for (let i = 0; i < 60; i++) {
    await nextTick()
    await new Promise<void>((r) => requestAnimationFrame(() => r()))
    const area = chartArea.value
    if (area && area.clientWidth > 0 && area.clientHeight > 0) return true
  }
  return false
}

const loadChart = async (type: 'expense' | 'income' = currentType.value) => {
  currentType.value = type

  if (!props.visible) return

  const ok = await waitUntilVisibleSized()
  if (!ok || !chartCanvas.value) return

  try {
    // 부모로부터 받은 props.year와 props.month를 API 파라미터로 사용
    const { data } = await instance.get<CategorySummary[]>(
      `/book/summary?type=${type}&year=${props.year}&month=${props.month}`
    )

    if (data.length === 0) {
      clearData()
      renderNoDataChart()
      return
    }

    const sorted = [...data].sort((a, b) => b.total - a.total)
    summary.value = sorted
    totalAmount.value = sorted.reduce((s, i) => s + i.total, 0)
    colors.value = sorted.map((_, i) => generateColor(i))

    renderChart(sorted)
  } catch (e) {
    console.error('summary api failed:', e)
    clearData()
  }
}

// 헬퍼 함수: 데이터 초기화
const clearData = () => {
  summary.value = []
  totalAmount.value = 0
  colors.value = []
  chartInstance?.destroy()
  chartInstance = null
}

// 헬퍼 함수: 차트 렌더링
const renderChart = (data: CategorySummary[]) => {
  chartInstance?.destroy()
  if (!chartCanvas.value) return

  chartInstance = new Chart(chartCanvas.value, {
    type: 'doughnut',
    data: {
      labels: data.map(i => i.category),
      datasets: [{ data: data.map(i => i.total), backgroundColor: colors.value }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      cutout: '65%',
      plugins: { legend: { display: false } }
    }
  })
}

// 헬퍼 함수: 데이터 없을 때 회색 차트
const renderNoDataChart = () => {
  if (!chartCanvas.value) return
  chartInstance = new Chart(chartCanvas.value, {
    type: 'doughnut',
    data: {
      labels: ['No Data'],
      datasets: [{ data: [1], backgroundColor: ['#d3d3d3'] }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      cutout: '65%',
      plugins: { legend: { display: false } }
    }
  })
}

// 감시자 설정: visible, year, month 중 하나라도 변경되면 loadChart 실행
watch(
  [() => props.visible, () => props.year, () => props.month],
  ([newVisible]) => {
    if (newVisible) {
      loadChart()
    }
  },
  { immediate: true }
)

onUnmounted(() => {
  chartInstance?.destroy()
})
</script>




<style scoped>
.stats-detail-container {
  background: #EDEDED;
  padding: 0;
}

.type-selector {
  display: flex;
  gap: 10px;
  margin-bottom: 40px;
}

.type-selector button {
  padding: 8px 24px;
  border-radius: 20px;
  border: 1.5px solid #0063f8;
  background: transparent;
  color: #0063f8;
  font-size: 13px;
  font-weight: 700;
  cursor: pointer;
}

.type-selector button.active {
  background: #0063f8;
  color: #fff;
}

/* 차트 영역: 중앙에 크게 배치 */
.chart-section {
  position: relative;
  height: 350px; /* 차트 크기를 더 키움 */
  width: 100%;
  margin-bottom: 50px;
  display: flex;
  justify-content: center;
}

/* 리스트 영역: 게시판 테이블처럼 넓게 사용 */
.category-list {
  list-style: none;
  padding: 0;
  margin: 0;
  border-top: 1.5px solid #0063f8; /* 게시판 헤더 선 스타일 */
  border-bottom: 1.5px solid #0063f8; /* 게시판 헤더 선 스타일 */
}

.list-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px; /* 게시판 td 패딩과 유사하게 */
  border-bottom: 1px solid #e5e7eb;
}

.list-item:hover {
  background-color: #e5e7eb; /* 게시판 hover 효과 */
}

.cat-left {
  display: flex;
  align-items: center;
  gap: 15px;
}

.dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
}

.name {
  font-size: 15px;
  font-weight: 600;
  color: #374151;
  min-width: 80px;
}

.percent {
  color: #0063f8;
  font-weight: 700;
  font-size: 14px;
}

.amount {
  font-size: 16px;
  font-weight: 700;
  color: #374151;
}

.empty-msg {
  padding: 60px;
  text-align: center;
  color: #9ca3af;
}
</style>