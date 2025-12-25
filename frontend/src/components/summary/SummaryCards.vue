<template>
  <div class="stats-container">
    <div class="header-row">
      <div class="month-selector">
        <button @click="changeMonth(-1)"><font-awesome-icon :icon="['far', 'circle-left']" /></button>
        <span>{{ todayDate }}</span>
        <button @click="changeMonth(1)"><font-awesome-icon :icon="['far', 'circle-right']" /></button>
      </div>
    </div>

    <div class="summary-bar">
      <div class="item">
        <p>총 지출</p>
        <h3 class="expense">{{ (totalExpense ?? 0).toLocaleString() }}원</h3>
      </div>
      <div class="item">
        <p>총 수입</p>
        <h3 class="income">{{ (totalIncome ?? 0).toLocaleString() }}원</h3>
      </div>
      <div class="item">
        <p>잔액</p>
        <h3>{{ ((totalIncome ?? 0) - (totalExpense ?? 0)).toLocaleString() }}원</h3>
      </div>
    </div>

    <div class="content-area">
      <ExpenseDonutChart :visible="showStats" :year="year" :month="month" />
      <FinanceAISummary class="ai-summary" :totalIncome="totalIncome" :totalExpense="totalExpense" :entries="bookStore.bookEntries" :currentType="'expense'" />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from "vue";
import { useBookStore } from "@/stores/book";
import ExpenseDonutChart from "./ExpenseDonutChart.vue";
import FinanceAISummary from "./FinanceAISummary.vue";

const bookStore = useBookStore();

const today = ref(new Date());
const year = computed(() => today.value.getFullYear());
const month = computed(() => today.value.getMonth() + 1);
const todayDate = computed(() => `${year.value}년 ${month.value}월`);

const changeMonth = (diff) => {
  const d = new Date(today.value);
  d.setMonth(d.getMonth() + diff);
  today.value = d;
};


const totalExpense = computed(() =>
  (bookStore.bookEntries || [])
    .filter(e => e.type === "expense")
    .reduce((sum, e) => sum + Number(e.amount || 0), 0)
)

const totalIncome = computed(() =>
  (bookStore.bookEntries || [])
    .filter(e => e.type === "income")
    .reduce((sum, e) => sum + Number(e.amount || 0), 0)
)

onMounted(() => bookStore.loadBookList(year.value, month.value));

watch([year, month], async ([y, m]) => {
  await bookStore.loadBookList(y, m)
})


const showStats = computed(() => (bookStore.bookEntries || []).length > 0)
</script>

<style scoped>
.stats-container {
  max-width: 1100px;
  margin: 40px auto;
  padding: 24px;
  background: #EDEDED;
   flex-direction: column;
}

.header-row {
  display: flex;
  justify-content: center; 
  align-items: center;
  margin-bottom: 30px;
}

.month-selector {
  display: flex;
  align-items: center;
  gap: 20px; 
  font-size: 22px;
  font-weight: 700;
  color: #374151;
}

.month-selector button {
  background: none;
  border: none;
  color: #0063f8;
  font-size: 24px; 
  cursor: pointer;
  display: flex;
  align-items: center;
  transition: transform 0.2s ease;
}

.month-selector button:hover {
  transform: scale(1.1); 
}


.summary-bar {
  display: flex;
  justify-content: space-around;
  padding: 24px 0;
  border-top: 2px solid #0063f8;
  border-bottom: 2px solid #0063f8;
  margin-bottom: 30px;
}

.item {
  font-weight: 700;
  text-align: center;
  min-width: 0;
}

.content-area {
  display: flex;
  flex-direction: column;
  gap: 20px;
  padding: 0 24px;
}

.chart-wrap {
  display: flex;
  justify-content: center;
  width: 100%;
}

.section {
  width: 100%;
}

.ai-summary {
  width: 100%;
}

/* 태블릿 */
@media (max-width: 768px) {
  .stats-container {
    margin: 20px auto;
    padding: 16px;
  }

  .month-selector {
    font-size: 18px;
    gap: 12px;
  }

  .month-selector button {
    font-size: 22px;
  }

  .summary-bar {
    display: grid;
    grid-template-columns: 1fr 1fr; 
    gap: 12px;
    padding: 16px 0;
  }

  .content-area {
    grid-template-columns: 1fr;
  }

  .ai-summary {
    width: 100%;
    margin-top: 0;
  }
}
/* 모바일 */
@media (max-width: 420px) {
  .month-selector {
    font-size: 16px;
  }

  .summary-bar {
    grid-template-columns: 1fr; 
    text-align: left;
    padding: 12px 0;
  }

  .item {
    display: flex;
    justify-content: space-between;
    align-items: baseline;
    padding: 6px 0;
  }

  .item p {
    margin: 0;
    font-size: 13px;
  }

  .item h3 {
    margin: 0;
    font-size: 16px;
  }
}
</style>
