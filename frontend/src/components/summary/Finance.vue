<template>
    <div class="finance-container">
        <h3 class="title">수입/지출 분석</h3>
        <div v-if="result" class="result">
            <h4 class="result-title">분석결과 : </h4>
            <p class="result-content">{{ result }}</p>
        </div>
    </div>
</template>

<script setup>
import { ref, watch, defineProps } from 'vue';
import { useFinanceStore } from '@/stores/finance';

const financeStore = useFinanceStore();

const props = defineProps({
    totalIncome: {
        type: Number,
        required: true,
    },
    totalExpense: {
        type: Number,
        required: true,
    },
})

const result = ref("");
watch(
    () => [props.totalIncome, props.totalExpense],
    async () => {
        // await financeStore.analyzeFinances(props.totalIncome, props.totalExpense);
        result.value = financeStore.analysisResult;
    },
    { immediate: true }
);
</script>

<style scoped>
.finance-container {
    padding: 20px;
    background-color: #f9f9f9;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    max-width: 600px;
    margin: 0 auto;
}

.title {
    font-size: 24px;
    font-weight: bold;
    color: #333;
    margin-bottom: 20px;
}

.result {
    background-color: #fff;
    padding: 15px;
    border-radius: 5px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.result-title {
    font-size: 20px;
    font-weight: bold;
    color: #444;
    margin-bottom: 10px;
}

.result-content {
    font-size: 16px;
    color: #555;
}
</style>
