<template>
  <div class="book-list-container">
    <table class="book-table">
      <thead>
        <tr>
          <th class="date-col-header">날짜</th>
          <th class="type-col-header">구분</th>
          <th class="category-col-header">카테고리</th>
          <th class="content-col-header">내용</th>
          <th class="memo-col-header">메모</th>
          <th class="amount-col">금액 (원)</th>
        </tr>
      </thead>

      <tbody>
        <template v-for="entry in entries" :key="entry.id">
          <!--기본 행-->
          <tr class="entry-row" @click="selectEntry(entry)">
            <td>{{ formatDate(entry.createdAt) }}</td>
            <td
            :class="entry.type === 'income' ? 'text-income' : 'text-expense'"
          >
            {{ entry.type === 'income' ? '수입' : '지출' }}
          </td>

          <td>{{ entry.category }}</td>
          <td>{{ entry.content }}</td>
          <td>{{ entry.memo }}</td>
          <td class="amount-col">{{ formatAmount(entry.amount) }}</td>

        </tr>
  </template>

  <tr v-if="entries.length === 0">
    <td colspan="6" class="no-data">
      등록된 가계부 항목이 없습니다.
    </td>
  </tr>
</tbody>
</table>

    <!--합계-->
    <div class="total-box">
      <div>
        <span>총 수입:</span>
        <span class="text-income">{{ formatAmount(totalIncome) }}원</span>
      </div>
      <div>
        <span>총 지출:</span>
        <span class="text-expense">{{ formatAmount(totalExpense) }}원</span>
      </div>
      <div>
        <span>잔액:</span>
        <span :class="balance >= 0 ? 'text-income' : 'text-expense'">
          {{ formatAmount(balance) }}원
        </span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, computed, ref } from 'vue'
import instance from '@/api/axiosInstance'

const props = defineProps({
  entries: {
    type: Array,
    required: true,
    default: () => [],
  },
})

const emit = defineEmits(['selectEntry'])
const selectEntry = (entry) => emit('selectEntry', entry)

// 포맷 함수
const formatAmount = (amount) =>
  amount ? amount.toLocaleString('ko-KR') : '0'

const formatDate = (dateString) => {
  if (!dateString) return ''
  const d = new Date(dateString)
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(
    d.getDate()
  ).padStart(2, '0')} ${String(d.getHours()).padStart(2, '0')}:${String(
    d.getMinutes()
  ).padStart(2, '0')}`
}

// 합계 계산
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
  overflow-x: auto;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
  background-color: white;
}

.book-table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
  font-size: 14px;
}

.book-table th,
.book-table td {
  padding: 10px 8px;
  border-bottom: 1px solid #eee;
  text-align: center;
  vertical-align: middle;
}

.entry-row:hover {
  background-color: #f9f9f9;
  cursor: pointer;
}

.amount-col {
  text-align: right;
  font-weight: bold;
}

.text-income {
  color: #1f8a4c;
}

.text-expense {
  color: #d9534f;
}

.btn-compare {
  background-color: #f0f7ff;
  color: #007bff;
  border: 1px solid #b3d4ff;
  padding: 5px 10px;
  border-radius: 5px;
  font-size: 12px;
  cursor: pointer;
}

.btn-compare:hover {
  background-color: #007bff;
  color: white;
}

.no-data {
  text-align: center;
  padding: 20px;
  color: #999;
}

.total-box {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  text-align: right;
  margin-top: 10px;
  padding: 0 15px 15px 0;
  gap: 4px;
  font-weight: 600;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  gap: 20px;
}

.close-btn {
  border: none;
  background: none;
  font-size: 18px;
  cursor: pointer;
}

.result-text {
  margin-top: 8px;
  font-weight: bold;
}

.card-body p {
  margin: 5px 0;
}

</style>
