<template>
  <div class="book-list-container">
    <table class="book-table">
      <thead>
        <tr>
          <th style="width: 15%">날짜</th>
          <th style="width: 10%">구분</th>
          <th style="width: 15%">카테고리</th>
          <th style="width: 20%">내용</th>
          <th style="width: 25%">메모</th>
          <th style="width: 15%" class="amount-col-header">금액(원)</th>
        </tr>
      </thead>

      <tbody>
        <tr v-for="entry in paginatedEntries" :key="entry.id" class="entry-row" @click="selectEntry(entry)">
          <td data-label="날짜" class="date-cell">{{ formatDateSimple(entry.createdAt) }}</td>
          <td data-label="구분" :class="entry.type === 'income' ? 'text-income' : 'text-expense'" class="type-cell">
            {{ entry.type === 'income' ? '수입' : '지출' }}
          </td>
          <td data-label="카테고리">{{ entry.category }}</td>
          <td data-label="내용" class="content-cell">{{ entry.content }}</td>
          <td data-label="메모" class="memo-cell">{{ entry.memo }}</td>
          <td data-label="금액" class="amount-col">{{ formatAmount(entry.amount) }}</td>
        </tr>

        <tr v-if="entries.length === 0">
          <td colspan="6" class="no-data">등록된 항목이 없습니다.</td>
        </tr>
      </tbody>
    </table>

    <div class="pagination-container" v-if="totalPages > 1">
      <button class="arrow-btn" @click="currentPage--" :disabled="currentPage === 1">
        <span class="arrow-left"></span>
      </button>

      <div class="page-number">
        {{ currentPage }} / {{ totalPages }}
      </div>

      <button class="arrow-btn" @click="currentPage++" :disabled="currentPage === totalPages">
        <span class="arrow-right"></span>
      </button>
    </div>

    <div class="summary-container">
      <div class="summary-item">
        <span class="summary-label">총 수입 :</span>
        <span class="text-income">{{ formatAmount(totalIncome) }}원</span>
      </div>
      <div class="summary-item">
        <span class="summary-label">총 지출 :</span>
        <span class="text-expense">{{ formatAmount(totalExpense) }}원</span>
      </div>
      <div class="summary-item total-balance">
        <span class="summary-label">잔액 :</span>
        <span :class="balance >= 0 ? 'text-balance-positive' : 'text-balance-negative'">
          {{ formatAmount(balance) }}원
        </span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, computed, ref, watch } from 'vue'

const props = defineProps({
  entries: {
    type: Array,
    required: true,
    default: () => [],
  },
})

const emit = defineEmits(['selectEntry'])
const selectEntry = (entry) => emit('selectEntry', entry)

/* 페이지네이션 */
const currentPage = ref(1)
const itemsPerPage = 16 // 한 페이지에 16개 까지 표시

// 전체 페이지 수 계산
const totalPages = computed(() => {
  return props.entries.length > 0 ? Math.ceil(props.entries.length / itemsPerPage) : 1
})

// 현재 페이지에 해당하는 데이터만 추출
const paginatedEntries = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage
  const end = start + itemsPerPage
  return props.entries.slice(start, end)
})

// 데이터가 변경되어 현재 페이지가 전체 페이지보다 커질 경우 보정
watch(totalPages, (newTotal) => {
  if (currentPage.value > newTotal) {
    currentPage.value = Math.max(1, newTotal)
  }
})

// 포맷 함수
const formatAmount = (amount) =>
  amount ? amount.toLocaleString('ko-KR') : '0'

const formatDateSimple = (dateString) => {
  if (!dateString) return ''
  const d = new Date(dateString)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`
}

// 합계 계산 (전체 데이터 기준 유지)
const totalIncome = computed(() =>
  props.entries
    .filter((e) => e.type === 'income')
    .reduce((sum, e) => sum + Number(e.amount), 0)
)

const totalExpense = computed(() =>
  props.entries
    .filter((e) => e.type === 'expense')
    .reduce((sum, e) => sum + Number(e.amount), 0)
)

const balance = computed(() => totalIncome.value - totalExpense.value)
</script>

<style scoped>
.book-list-container {
  background-color: #ededed;
}

.book-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.book-table th {
  padding: 15px 5px;
  border-bottom: 2px solid #0063f8;
  color: #0063f8;
  font-weight: 600;
  font-size: 15px;
}

.book-table td {
  padding: 15px 5px;
  border-bottom: 1px solid #d1d1d1;
  color: #040000;
  font-size: 14px;
  text-align: center;
}

.entry-row:hover {
  background-color: #f0f0f0;
  cursor: pointer;
}

.text-income {
  color: #029b07 !important;
  font-weight: bold;
}

.text-expense {
  color: #f04400 !important;
  font-weight: bold;
}

.amount-col {
  text-align: center;
  padding-right: 20px !important;
  font-weight: 600;
}

.amount-col-header {
  text-align: center;
  padding-right: 20px !important;
}

.pagination-container {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
  margin: 20px 0;
}

.arrow-btn {
  width: 40px;
  height: 40px;
  background-color: #f5f5f5;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: background-color 0.2s;
}

.arrow-btn:hover:not(:disabled) {
  background-color: #e8e8e8;
}

.arrow-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.arrow-left {
  width: 0;
  height: 0;
  border-top: 6px solid transparent;
  border-bottom: 6px solid transparent;
  border-right: 8px solid #888;
}

.arrow-right {
  width: 0;
  height: 0;
  border-top: 6px solid transparent;
  border-bottom: 6px solid transparent;
  border-left: 8px solid #888;
}

.page-number {
  font-size: 15px;
  font-weight: 700;
  color: #333;
  min-width: 50px;
  text-align: center;
}

.summary-container {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  margin-top: 20px;
  gap: 5px;
}

.summary-item {
  font-size: 16px;
  font-weight: 600;
}

.summary-label {
  color: #040000;
  margin-right: 10px;
}

.total-balance {
  margin-top: 5px;
  border-top: 1px solid #868686;
  padding-top: 5px;
}

.text-balance-positive {
  color: #029b07;
  font-weight: bold;
}

.text-balance-negative {
  color: #f04400;
  font-weight: bold;
}

@media (max-width: 768px) {
  .book-table thead {
    display: none;
  }

  .book-table,
  .book-table tbody,
  .book-table tr,
  .book-table td {
    display: block;
    width: 100%;
  }

  .book-table tr.entry-row {
    margin-bottom: 16px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
    padding: 12px;
    border: 1px solid #d1d1d1;
  }

  .book-table td {
    display: flex;
    justify-content: space-between;
    align-items: center;
    text-align: right;
    padding: 8px 0;
    border-bottom: 1px solid #f0f0f0;
    font-size: 14px;
  }

  .book-table td:last-child {
    border-bottom: none;
  }

  .book-table td::before {
    content: attr(data-label);
    float: left;
    font-weight: 700;
    color: #0063f8;
    margin-right: 10px;
  }

  .amount-col {
    padding-right: 0 !important;
  }

  .no-data {
    text-align: center;
    padding: 20px;
    background: transparent;
    border: none;
  }
}
</style>