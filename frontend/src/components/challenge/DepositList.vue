<template>
  <div class="deposit-list">
    <h4>저축 내역</h4>

    <div v-if="depositList.length === 0">
      아직 저축 내역이 없습니다.
    </div>

    <table v-else class="deposit-table">
      <thead>
        <tr>
          <th>날짜</th>
          <th>금액</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="d in depositList" :key="d.id">
          <td>{{ formatDate(d.createdAt) }}</td>
          <td>{{ formatAmount(d.amount) }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useChallengeDepositStore } from '@/stores/challengeDeposit'
import { storeToRefs } from 'pinia'

const props = defineProps({
  challengeId: {
    type: Number,
    required: true
  }
})

const depositStore = useChallengeDepositStore()
const { depositList } = storeToRefs(depositStore)

onMounted(async() => {
  await depositStore.getDepositList(props.challengeId)
})

const formatDate = (dateTime) => {
  return dateTime.split('T')[0] // yyyy-MM-dd
}

const formatAmount = (amount) => {
  return amount.toLocaleString() + '원'
}
</script>

<style scoped>
.deposit-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 8px;
}

.deposit-table th,
.deposit-table td {
  border: 1px solid #ddd;
  padding: 6px 8px;
  text-align: center;
}

.deposit-table th {
  background-color: #f5f5f5;
  font-weight: 600;
}
</style>
