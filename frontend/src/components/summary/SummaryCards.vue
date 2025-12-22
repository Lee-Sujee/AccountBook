<template>
  <div>
    <div class="month-selector">
      <button @click="changeMonth(-1)">&lt;</button>
      <span>{{ todayDate }}</span>
      <button @click="changeMonth(1)">&gt;</button>
    </div>

    <div class="summary-grid">
    <div class="card">
      <p>총 지출</p>
      <h2>{{ totalExpense.toLocaleString() }}원</h2>
    </div>

    <div class="card">
      <p>총 수입</p>
      <h2>{{ totalIncome.toLocaleString() }}원</h2>
    </div>

    <div class="card">
      <p>잔액</p>
      <h2>{{ (totalIncome - totalExpense).toLocaleString() }}원</h2>
    </div>
  </div>
    <ExpenseDonutChart :visible="showStats" />
    <Finance class="ai-summary" :totalIncome = "totalIncome" :totalExpense="totalExpense" />
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { useBookStore } from '@/stores/book'
import Finance from './Finance.vue'
import ExpenseDonutChart from './ExpenseDonutChart.vue'

const today = ref(new Date())
const todayDate = computed(() => {
    const y = today.value.getFullYear()
    const m = today.value.getMonth() + 1
    return `${y}년 ${m}월`
})
const bookStore = useBookStore()

const changeMonth = (diff:number) => {
  const d = new Date(today.value)
  d.setMonth(d.getMonth() + diff)
  today.value = d
}

const totalExpense = computed(() =>
  bookStore.bookEntries
    .filter(e => e.type === 'expense')
    .reduce((sum, e) => sum + e.amount, 0)
)

const totalIncome = computed(() =>
  bookStore.bookEntries
    .filter(e => e.type === 'income')
    .reduce((sum, e) => sum + e.amount, 0)
)



// 새로고침해도 유지
const showStats = ref(localStorage.getItem('showStats') === 'true')

watch(showStats, v => {
  localStorage.setItem('showStats', String(v))
})


</script>

<style scoped>
.dashboard-container {
  padding: 20px;
  background-color: #f9f9f9;
  min-height: 100vh;
}

.month-selector {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
  font-size: 20px;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  margin-bottom: 30px;
}

.card {
  background: #fff;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
}

.card:hover {
  transform: translateY(-5px);
}

.card-label {
  font-size: 16px;
  color: #777;
  margin-bottom: 8px;
}

.card-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.ai-summary {
  width: 100%;
  margin-top: 20px;
}

</style>