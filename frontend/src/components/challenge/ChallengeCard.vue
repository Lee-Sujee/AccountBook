<template>
  <div class="challenge-card">

    <h3>{{ challenge.description }}</h3>
    <div class="progress-box">
  <!-- 상단 금액 요약 -->
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
    <svg width="140" height="140">
      <circle cx="70" cy="70" :r="radius" class="circle-bg" />
      <circle
        cx="70"
        cy="70"
        :r="radius"
        class="circle-progress-bar"
        :stroke-dasharray="circumference"
        :stroke-dashoffset="strokeDashoffset"
      />
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
  

    <p>목표기간: {{ challenge.startDate }} ~ {{ challenge.endDate }}</p>
    <p>상태: {{ challenge.status }}</p>
    <div class="deposit">
    <input type="number" v-model.number="depositAmount" placeholder="저축 금액" min="1" />
    <button @click="submitDeposit" :disabled="depositAmount <= 0" > 저축하기 </button>
    <DepositList :challenge-id="challenge.id"/>
</div>

<hr>

    <div class="actions">
      <button @click="$emit('edit')">수정</button>
      <button @click="$emit('delete')">삭제</button>
    </div>
  </div>
</template>

<script setup>
import { storeToRefs } from 'pinia';
import { ref, defineEmits, defineProps, onMounted, computed } from 'vue';
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

const progressPercent = computed(() => {
  if (!props.challenge.target) return 0
  const percent = (depositSummary.value.totalAmount / props.challenge.target) * 100
  return percent
})
const radius = 36
const circumference = 2 * Math.PI * radius

const strokeDashoffset = computed(() => {
  return circumference * (1 - progressPercent.value / 100)
})
</script>


<style scoped>
.challenge-card {
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 16px;
  background-color: #ffffff;
  max-width: 700px;
  margin: 0 auto;
}

.challenge-card h3 {
  margin-bottom: 12px;
  font-size: 18px;
  font-weight: 600;
}

.challenge-card p {
  margin: 4px 0;
  font-size: 14px;
  color: #333;
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
  background-color: #4caf50;
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

.actions {
  display: flex;
  justify-content: space-between;
  margin-top: 12px;
}

.actions button {
  width: 48%;
  padding: 8px;
  border-radius: 4px;
  font-size: 13px;
  cursor: pointer;
}

.actions button:first-child {
  background-color: #2196f3;
  color: white;
  border: none;
}

.actions button:last-child {
  background-color: #f44336;
  color: white;
  border: none;
}

.progress-box {
  margin: 20px 0;
  text-align: center;
}

/* 상단 금액 */
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

/* pill badge */
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

/* 원형 그래프 */
.circle-progress {
  position: relative;
  width: 140px;
  height: 140px;
  margin: 0 auto 12px;
}

.circle-bg {
  fill: none;
  stroke: #e6eaf0;
  stroke-width: 14;
}

.circle-progress-bar {
  fill: none;
  stroke: #1e88e5;
  stroke-width: 14;
  stroke-linecap: round;
  transform: rotate(-90deg);
  transform-origin: 50% 50%;
  transition: stroke-dashoffset 0.5s ease;
}

.circle-text {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 22px;
  font-weight: 700;
  color: #1e88e5;
}

/* 하단 설명 */
.progress-desc {
  font-size: 14px;
  color: #555;
}

</style>