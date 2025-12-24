import { ref } from "vue";
import { defineStore } from "pinia";
import { createDepositApi, getDepositListApi, getDepositSummaryApi, updateDepositApi } from "@/api/challengeDepositApi";

export const useChallengeDepositStore = defineStore("challengeDeposit", () => { 

    const createDeposit = (challengeId, amount) =>{
        return createDepositApi(challengeId, amount)
        .then(() => {
            alert("해당 금액이 저축되었습니다.");
        })
        .catch((err) => {
            console.error("저축 실패: ", err);
            throw err;
        })
    }

    const depositList = ref([])
    const getDepositList = (challengeId) => {
        return getDepositListApi(challengeId)
        .then((res) => {
            depositList.value = res.data;
        })
        .catch((err) => {
            console.error("저축(입금) 내역 불러오기 실패: ", err);
            throw err;
        })
    }

    const depositSummary = ref({ totalAmount: 0, depositCount: 0 })
    const getDepositSummary = (challengeId) => {
        return getDepositSummaryApi(challengeId)
        .then((res) => {
            depositSummary.value = res.data;
        })
        .catch((err) => {
            console.error("저축(입금) 요약 정보 불러오기 실패: ", err);
            throw err;
        })
    }

    const updateDeposit = (depositId, challengeId, amount) => {
  return updateDepositApi(depositId, challengeId, amount)
    .then(() => alert("저축 금액이 수정되었습니다."))
    .catch((err) => {
      console.error("저축목록 수정 실패: ", err);
      throw err;
    })
}

    return {
        depositList,
        depositSummary,
        createDeposit,
        getDepositList,
        getDepositSummary,
        updateDeposit,
    }
});
