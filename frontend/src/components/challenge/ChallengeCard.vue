<template>
  <div class="challenge-card">

    <h3>{{ challenge.description }}</h3>
    <div class="progress-box">
    <!-- 상단 금액 요약 -->
    <div class="summary">
      <p>{{ challenge.startDate }} ~ {{ challenge.endDate }}</p>
      <p class="status">{{ statusKo }}</p>
    </div>
  <div class="amount-summary">
    <div class="amount-item">
      <span class="badge gray">목표 금액</span>
      <strong>{{ challenge.target.toLocaleString() }}</strong>
    </div>
    <div class="amount-item">
      <span class="badge">현재 금액</span>
      <strong>{{ depositSummary.totalAmount.toLocaleString() }}</strong>
    </div>
  </div>
  <!-- 원형 그래프 -->
  <div class="circle-progress">
  <svg width="140" height="140" viewBox="0 0 140 140">
    <circle cx="70" cy="70" :r="radius" class="circle-bg" />
    <circle
      cx="70"
      cy="70"
      :r="radius"
      class="circle-progress-bar"
      :stroke-dasharray="circumference"
      :stroke-dashoffset="strokeDashoffset"
    />
    <circle cx="70" cy="70" :r="innerRadius" class="circle-inner" />
  </svg>

  <div class="circle-text">
    {{ progressPercent }}%
  </div>
</div>

  <!-- 하단 설명 -->
  <p class="progress-desc">
    목표금액의 {{ progressPercent }}%를 저축하였습니다.
  </p>
</div>



<div class="deposit">
  <form class="deposit-row" @submit.prevent="submitDeposit">
    <input
      v-model.number="depositAmount"
      type="number"
      class="underline-input"
      placeholder="금액 입력"
    />

    <button type="submit" class="deposit-btn">
      입금
    </button>
  </form>

  <div class="actions">
    <button @click="$emit('edit')">수정</button>
    <button @click="$emit('delete')">삭제</button>
  </div>
</div>
  <DepositList
  :challenge-id="challenge.id"
  @edit-deposit="handleEditDeposit"
/>
</div>



</template>

<script setup>
import { storeToRefs } from 'pinia';
import { ref, defineEmits, defineProps, onMounted, computed, watch } from 'vue';
import { useChallengeStore } from '@/stores/challenge';
import { useChallengeDepositStore } from '@/stores/challengeDeposit';
import DepositList from './DepositList.vue';

const challengeStore = useChallengeStore();
const props = defineProps({
  challenge: {
    type: Object,
    required: true
  }
});

defineEmits(['edit', 'delete']);

const depositStore = useChallengeDepositStore()
const { depositSummary } = storeToRefs(depositStore)

const depositAmount = ref(0)


const onDeposit = () => {
  deposit();
}


const submitDeposit = async () => {
  const today = new Date().toISOString().split('T')[0]

  if (today < props.challenge.startDate) {
    alert("아직 챌린지 시작 전입니다! 시작일을 먼저 수정해주세요.")
    return
  }
  await depositStore.createDeposit(props.challenge.id, depositAmount.value)
  depositAmount.value = 0
  await depositStore.getDepositSummary(props.challenge.id)
  await challengeStore.getChallengeList()
  await depositStore.getDepositList(props.challenge.id);
}
onMounted(() => {
  depositStore.getDepositSummary(props.challenge.id)
})
watch(() => props.challenge.id, () => {
  depositStore.getDepositSummary(props.challenge.id)
  depositStore.getDepositList(props.challenge.id)
})

const statusKo = computed(() => {
  const map = {
    READY: '준비중',
    ONGOING: '진행중',
    SUCCESS: '성공',
  }
  return map[props.challenge.status] ?? props.challenge.status
})


const strokeWidth = 18
const radius = 52
const innerRadius = radius - strokeWidth / 2 - 2
const circumference = 2 * Math.PI * radius

const progressPercent = computed(() => {
  if (!props.challenge.target) return 0
  const raw = (depositSummary.value.totalAmount / props.challenge.target) * 100
  const safe = Number.isFinite(raw) ? raw : 0
  return Math.min(100, Math.max(0, Math.floor(safe))) // ✅ 0~100 정수
})

const strokeDashoffset = computed(() => {
  return circumference * (1 - progressPercent.value / 100)
})


const handleEditDeposit = async ({ depositId, amount }) => {
  await depositStore.updateDeposit(depositId, props.challenge.id, amount)

  await depositStore.getDepositSummary(props.challenge.id)
  await challengeStore.getChallengeList()
  await depositStore.getDepositList(props.challenge.id)
}
</script>


<style scoped>
.challenge-card {
  width: 100%;
  border-radius: 8px;
  background-color: #EDEDED;
  margin: 0 auto;
  box-sizing: border-box;
  overflow-x: hidden; 
}

.challenge-card h3 {
  width: 100%;
  margin-bottom: 12px;
  font-size: 22px;
  font-weight: 700;
  color: #0063f8;
  border-bottom: 2px solid #0063f8;
  padding: 10px;
}

.challenge-card p {
  margin: 4px 0;
  font-size: 14px;
  color: #333;
}

.summary {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  padding: 0 16px;
  box-sizing: border-box;
  font-size: 15px;
  font-weight: 700;
  margin-bottom: 20px;
}

.summary p {
  margin: 0;
}

.summary .status {
  white-space: nowrap;
  flex-shrink: 0;
  padding-left: 12px;
  color:#0063f8
}

.deposit {
  margin-top: 12px;
}

.deposit input {
  width: 100%;
  padding: 8px;
  margin-bottom: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 14px;
}

.deposit button {
  width: 100%;
  padding: 10px;
  background-color: #0063f8;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
}

.deposit button:disabled {
  background-color: #c8e6c9;
  cursor: not-allowed;
}

.deposit hr {
  margin: 16px 0;
}

.challenge-card,
.actions {
  box-sizing: border-box;
}

.actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 12px;
  padding: 0;
  margin-right: 10px;
  margin-bottom: 10px;
}

.actions button {
  width: 90px;
  padding: 8px 0;
  border-radius: 40px;
  font-size: 13px;
  font-weight: 600;
  cursor: pointer;
  transition: opacity 0.2s, transform 0.05s;
}

.actions button:hover {
  opacity: 0.8;
}
.actions button:active {
  transform: scale(0.99);
}

.actions button:first-child {
  background-color: #fff;
  color: #0063f8;
  border: 1px solid #0063f8;
}

.actions button:last-child {
  background-color: #0063f8;
  color: #fff;
  border: 1px solid #0063f8;
}
.progress-box {
  margin: 20px 0;
  text-align: center;
}

.amount-summary {
  display: flex;
  justify-content: center;
  gap: 40px;
  margin-bottom: 16px;
}

.amount-item {
  text-align: center;
}

.amount-item strong {
  display: block;
  margin-top: 6px;
  font-size: 20px;
  font-weight: 700;
}

.badge {
  display: inline-block;
  padding: 4px 12px;
  font-size: 12px;
  font-weight: 600;
  border-radius: 999px;
  background-color: #1e88e5;
  color: white;
}

.badge.gray {
  background-color: #b0bec5;
}

.circle-progress {
  position: relative;
  width: 140px;
  height: 140px;
  margin: 0 auto 12px;
}

.circle-bg {
  fill: none;
  stroke: #bdbdbd;  
  stroke-width: 18; 
}

.circle-progress-bar {
  fill: none;
  stroke: #0063f8;
  stroke-width: 18; 
  stroke-linecap: round;
  transform: rotate(-90deg);
  transform-origin: 50% 50%;
  transition: stroke-dashoffset 0.5s ease;
}

.circle-inner {
  fill: #ededed;   
}

.circle-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 28px;    
  font-weight: 800;
  color: #0063f8;
}
.progress-desc {
  font-size: 14px;
  font-weight: 500;
}

.deposit {
  margin-top: 12px;
}

.deposit-row {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  margin-bottom: 12px;
}

.deposit-row input {
  width: 180px;
  padding: 8px;
  background-color: #ededed;
  border: none;
  border-bottom: 2px solid #0063f8;
  border-radius: 0px;
  font-size: 14px;
  margin-bottom: 0; 
}

.deposit-row .deposit-btn {
  width: 120px;       
  padding: 10px;
  background-color: #0063f8;
  color: white;
  border: none;
  border-radius: 40px;
  font-size: 14px;
  cursor: pointer;
}

.deposit-row .deposit-btn:disabled {
  background-color: #81b1f8;
  cursor: not-allowed;
}

</style>