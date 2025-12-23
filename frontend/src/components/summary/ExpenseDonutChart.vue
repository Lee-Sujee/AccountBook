<template>
  <div class="stats-container">
    <h3 class="stats-title">카테고리별 비율</h3>

    <div class="month-selector">
      <button @click="changeMonth(-1)">
      </button>
      <span>{{ currentMonthLabel }}</span>
      <button @click="changeMonth(1)">></button>
    </div>

    <div class="btn-group">
      <button :class="{ active: currentType === 'expense' }" @click="loadChart('expense')">
        지출
      </button>
      <button :class="{ active: currentType === 'income' }" @click="loadChart('income')">
        수입
      </button>
    </div>

    <div class="total-amount">
      총 {{ currentType === 'expense' ? '지출' : '수입' }}
      <strong>{{ totalAmount.toLocaleString() }}원</strong>
    </div>

    <div class="content-grid">
      <div class="chart-card">
        <div class="chart-area" ref="chartArea">
          <canvas ref="chartCanvas"></canvas>
        </div>
      </div>

      <div class="list-card">
        <ul class="category-list" v-if="summary.length > 0">
          <li v-for="(item, index) in summary" :key="item.category">
            <span class="color-dot" :style="{ backgroundColor: colors[index] }"></span>
            <span class="category-name">{{ item.category }}</span>
            <span class="category-percent">{{ getPercent(item.total) }}%</span>
            <span class="category-amount">{{ item.total.toLocaleString() }}원</span>
          </li>
        </ul>
        <!--데이터 없을 때-->
        <div v-else class="no-data-message">
          아직 등록된 항목이 없습니다.
        </div>
      </div>
    </div>
  </div>
</template>


<script setup lang="ts">
import { ref, computed, onUnmounted, watch, nextTick } from 'vue'
import { Chart, DoughnutController, ArcElement, Tooltip } from 'chart.js'
import instance from '@/api/axiosInstance'

Chart.register(DoughnutController, ArcElement, Tooltip)

const props = defineProps<{ visible: boolean }>()

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
const currentMonth = ref(new Date())

const currentMonthLabel = computed(() => {
  const y = currentMonth.value.getFullYear()
  const m = currentMonth.value.getMonth() + 1
  return `${y}년 ${m}월`
})

const palette = ['#FF6B6B', '#FFA94D', '#FFD43B', '#74C0FC', '#69DB7C', '#B197FC']
const generateColor = (i: number) => palette[i % palette.length]

const getPercent = (v: number) =>
  totalAmount.value ? ((v / totalAmount.value) * 100).toFixed(1) : '0.0'

const changeMonth = (diff: number) => {
  const d = new Date(currentMonth.value)
  d.setMonth(d.getMonth() + diff)
  currentMonth.value = d
  loadChart(currentType.value)
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

const loadChart = async (type: 'expense' | 'income') => {
  currentType.value = type

  // 1) 통계 화면이 아닐 때는 불필요 호출 방지
  if (!props.visible) return

  // 2) DOM/레이아웃이 "보이는 상태"로 잡힐 때까지 대기
  const ok = await waitUntilVisibleSized()
  if (!ok) return

  if (!chartCanvas.value) return

  const y = currentMonth.value.getFullYear()
  const m = currentMonth.value.getMonth() + 1

  try {
    const { data } = await instance.get<CategorySummary[]>(
      `/book/summary?type=${type}&year=${y}&month=${m}`
    )

    //데이터 읎으면 회색 도넛
    if (data.length === 0) {
      summary.value = []
      totalAmount.value = 0
      colors.value = []
      chartInstance?.destroy()
      chartInstance = new Chart(chartCanvas.value, {
        type: 'doughnut',
        data: {
          labels: ['No Data'],
          datasets: [{
            data: [1],
            backgroundColor: ['#d3d3d3']
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          cutout: '65%',
          plugins: { legend: { display: false } }
        }
      })
      chartInstance.resize()
      return
    }

    const sorted = [...data].sort((a, b) => b.total - a.total)
    summary.value = sorted
    totalAmount.value = sorted.reduce((s, i) => s + i.total, 0)
    colors.value = sorted.map((_, i) => generateColor(i))

    chartInstance?.destroy()
    chartInstance = new Chart(chartCanvas.value, {
      type: 'doughnut',
      data: {
        labels: sorted.map(i => i.category),
        datasets: [{ data: sorted.map(i => i.total), backgroundColor: colors.value }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        cutout: '65%',
        plugins: { legend: { display: false } }
      }
    })

    chartInstance.resize()

  } catch (e) {
    console.error('summary api failed:', e)
    summary.value = []
    totalAmount.value = 0
    colors.value = []
    chartInstance?.destroy()
    chartInstance = null
  }
}

watch(
  () => props.visible,
  async (v) => {
    if (v) {
      await loadChart(currentType.value)
    }
  },
  { immediate: true }
)

onUnmounted(() => {
  chartInstance?.destroy()
  chartInstance = null
})
</script>



<style scoped>
.stats-container {
  max-width: 1100px;
  margin: 0 auto;
  padding: 32px 24px;
}

.stats-title {
  text-align: center;
  font-size: 20px;
  font-weight: 700;
  margin-bottom: 12px;
}

.month-selector {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}

.btn-group {
  display: flex;
  justify-content: center;
  gap: 8px;
  margin-bottom: 8px;
}

.btn-group button {
  padding: 6px 14px;
  border-radius: 20px;
  border: 1px solid #ddd;
  background: #f1f3f5;
  cursor: pointer;
}

.btn-group button.active {
  background: #339af0;
  color: #fff;
  border-color: #339af0;
}

.total-amount {
  text-align: center;
  font-weight: 600;
  margin-bottom: 24px;
}

.content-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
  align-items: stretch;
}

.chart-card,
.list-card {
  background: #fff;
  border-radius: 20px;
  padding: 24px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.06);
}

.chart-card {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
}

.chart-area {
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  width: 280px;
  height: 280px;
}

.category-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.category-list li {
  display: grid;
  grid-template-columns: 12px 1fr auto auto;
  align-items: center;
  gap: 10px;
  padding: 10px 0;
  border-bottom: 1px solid #f1f3f5;
}

.category-list li:last-child {
  border-bottom: none;
}

.color-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
}

.category-name {
  font-weight: 500;
}

.category-percent {
  color: #868e96;
  font-size: 14px;
}

.category-amount {
  font-weight: 600;
}

@media (max-width: 768px) {
  .content-grid {
    grid-template-columns: 1fr;
  }

  .chart-area {
    width: 240px;
    height: 240px;
  }
}

.no-data-message {
  text-align: center;
  color: #868e96;
  font-size: 16px;
  font-weight: 500;
  padding: 20px 0;
}
</style>