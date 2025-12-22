import { ref } from "vue";
import { defineStore } from "pinia";
import { analyzeFinancesApi } from "@/api/bookApi";

export const useFinanceStore = defineStore('finance', () => {
    
    const income = ref(0);
    const expense = ref(0);
    const analysisResult = ref(null);

    const setIncome = (value) => {
        income.value = value;
    }

    const setExpense = (value) => {
        expense.value = value;
    }

    // 가계부 항목 기반으로 수입/지출 값 가져오기
    const calculateIncomeAndExpense = (entries) => {
        let totalIncome = 0;
        let totalExpense = 0;

        entries.forEach(entry => {
            if(entry.type === 'income')
                totalIncome += entry.amount;
            else if(entry.type === 'expense') {
                totalExpense += entry.amount;
            }
        })
    }

    const analyzeFinances = (inputIncome, inputExpense) => {
        setIncome(inputIncome);
        setExpense(inputExpense);
        return analyzeFinancesApi(income.value, expense.value)
        .then((res) => {
            console.log(income.value)
            analysisResult.value = res.data;
        })
        .catch((err) => {
            console.error("수입/지출 분석 실패: ", err);
            throw err;
        })
    }


    return{
        income,
        expense,
        analysisResult,
        setIncome,
        setExpense,
        analyzeFinances,
        calculateIncomeAndExpense,
    }
})