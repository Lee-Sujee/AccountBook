import { ref } from "vue";
import { defineStore } from "pinia";
import { analyzeFinancesApi } from "@/api/bookApi";

export const useFinanceStore = defineStore("finance", () => {
  const analysisResult = ref("");
  const summaryList = ref([]);
  const totalAmount = ref(0);

  const reset = () => {
    analysisResult.value = "";
    summaryList.value = [];
    totalAmount.value = 0;
  };

  const analyzeFinances = async (type, year, month, entries = []) => {
    if (!entries.length) {
      reset();
      return;
    }

    const history = entries.map((e) => ({
      category: e.category,
      amount: Number(e.amount || 0),
      type: e.type,
      memo: e.memo || "",
      date: e.date || null,
    }));

    const res = await analyzeFinancesApi({ type, year, month, history });

    if (typeof res.data === "string") {
      analysisResult.value = res.data;
      summaryList.value = [];
      totalAmount.value = 0;
      return;
    }

    analysisResult.value = res.data.aiReport ?? "";
    const list = res.data.summaryList ?? [];
    summaryList.value = list;
    totalAmount.value = list.reduce((s, i) => s + Number(i.total || 0), 0);
  };

  return { analysisResult, summaryList, totalAmount, analyzeFinances, reset };
});
