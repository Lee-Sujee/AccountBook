<template>
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
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useBookStore } from '@/stores/book'

const bookStore = useBookStore()

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
</script>

<style scoped>
.summary-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.card {
  background: #fff;
  padding: 20px;
  border-radius: 16px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.06);
}
</style>
