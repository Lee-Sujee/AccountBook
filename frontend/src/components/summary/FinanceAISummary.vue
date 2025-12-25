<template>
  <div class="ai-analysis-wrapper">
    <div class="ai-header">
      <h3 class="title">AI 재정 분석 리포트</h3>
    </div>

    <div v-if="financeStore.analysisResult || isLoading" class="ai-result-box">
      <div v-if="isLoading" class="loading-state">
        AI가 이번 달 소비 패턴을 분석하고 있습니다...
      </div>
      <div v-else>
        <h4 class="result-label">분석 결과</h4>
        <p class="result-content">{{ financeStore.analysisResult }}</p>
      </div>
    </div>

    <div v-else class="no-data-msg">
      분석할 내역이 없습니다.
    </div>
  </div>
</template>

<script setup>
import { ref, watch, computed } from "vue";
import { useFinanceStore } from "@/stores/finance";

const financeStore = useFinanceStore();
const isLoading = ref(false);

const props = defineProps({
  year: Number,
  month: Number,
  entries: { type: Array, default: () => [] },
  currentType: { type: String, default: "expense" },
});

const key = computed(() =>
  `${props.currentType}-${props.year}-${props.month}-${props.entries.length}`
);

watch(
  key,
  async () => {
    if (!props.entries.length) {
      financeStore.reset?.();
      return;
    }

    isLoading.value = true;
    try {
       await financeStore.analyzeFinances(
         props.currentType,
         props.year,
         props.month,
         props.entries
       );
    } finally {
      isLoading.value = false;
    }
  },
  { immediate: true }
);
</script>


<style scoped>
.ai-analysis-wrapper {
  width: 100%;
  max-width: none;
  margin: 0;
  background: #EDEDED;
  border-radius: 12px;
}

.ai-header {
  margin-bottom: 20px;
}

.title {
  font-size: 22px;
  font-weight: 700;
  color: #0063f8;
}

.ai-result-box {
  background-color: #EDEDED;
  padding: 24px 0;
  border-top: 2px solid #0063f8;
  border-bottom: 2px solid #0063f8;
}

.result-label {
  font-size: 13px;
  font-weight: 600;
  color: #0063f8;
  margin-bottom: 12px;
  text-transform: uppercase;
}

.result-content {
  font-size: 16px;
  line-height: 1.8;
  color: #374151;
  white-space: pre-wrap;
}

.loading-state,
.no-data-msg {
  text-align: center;
  padding: 40px;
  color: #6b7280;
  font-size: 14px;
}
</style>
