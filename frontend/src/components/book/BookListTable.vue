<template>
  <div class="book-list-container">
    <table class="book-table">
      <thead>
        <tr>
          <th>날짜</th>
          <th>구분</th>
          <th>카테고리</th>
          <th>내용</th>
          <th>메모</th>
          <th class="amount-col">금액 (원)</th>
        </tr>
      </thead>
      <tbody>
        <tr 
          v-for="entry in entries" 
          :key="entry.id" 
          @click="selectEntry(entry)" 
          class="entry-row"
        >
          <td>{{ formatDate(entry.createdAt) }}</td>
          
          <td :class="entry.type === 'income' ? 'text-income' : 'text-expense'">
            {{ entry.type === 'income' ? '수입' : '지출' }}
          </td>
          
          <td>{{ entry.category }}</td>
          <td>{{ entry.content }}</td>
          <td>{{ entry.memo }}</td>
          <td class="amount-col">{{ formatAmount(entry.amount) }}</td>
        </tr>
        
        <tr v-if="entries.length === 0">
          <td colspan="6" class="no-data">등록된 가계부 항목이 없습니다. 새 항목을 등록해주세요.</td>
        </tr>
      </tbody>
    </table>

    <!--합계-->
    <div class="total-box">
      <div><span>총 수입:</span> <span class="text-income">{{ formatAmount(totalIncome) }}원</span></div>
      <div><span>총 지출:</span> <span class="text-expense">{{ formatAmount(totalExpense) }}원</span></div>
      <div><span>잔액:</span>
        <span :class="balance >= 0 ? 'text-income' : 'text-expense'">
          {{ formatAmount(balance) }}원
        </span></div>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits, computed } from 'vue';

const props = defineProps({
  entries: {
    type: Array,
    required: true,
    default: () => [],
  }
});

const emit = defineEmits(['selectEntry']);
const selectEntry = (entry) => emit('selectEntry', entry);

// 금액 포맷 함수
const formatAmount = (amount) => {
  return amount ? amount.toLocaleString('ko-KR') : '0';
};

// 날짜 포맷 함수 (진짜 모르겠는데 gpt가 알려준대로 일단 적었음..)
const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleDateString('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit',
      hour12: false // 24시간 형식
  }).replace(/\. /g, '.').replace('.', '-').replace('.', '-').replace(' ', ' ');
};

//합계 계산
const totalIncome = computed(()=>{
  return props.entries
  .filter(e => e.type === 'income')
  .reduce((sum, e) => sum + Number(e.amount), 0);
});

const totalExpense = computed(()=>{
  return props.entries
  .filter(e => e.type === 'expense')
  .reduce((sum, e) => sum + Number(e.amount), 0);
});

const balance = computed(() => {
  return totalIncome.value - totalExpense.value;
});

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
  text-align: left;
  font-size: 14px;
}

.book-table th {
  background-color: #f0f4f7;
  color: #333;
  padding: 12px 15px;
  font-weight: 600;
  border-bottom: 2px solid #ddd;
  text-transform: uppercase;
}

.book-table td {
  padding: 10px 15px;
  border-bottom: 1px solid #eee;
  color: #555;
}

.entry-row {
  cursor: pointer;
  transition: background-color 0.2s;
}

.entry-row:hover {
  background-color: #f9f9f9;
}

.amount-col {
  text-align: right;
  font-weight: bold;
}
.content-col {
    max-width: 250px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.text-income {
  color: #1F8A4C;
  font-weight: 500;
}

.text-expense {
  color: #d9534f;
  font-weight: 500;
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
  margin-top: 10px;
  gap: 4px;
  font-weight: 600;
}
</style>