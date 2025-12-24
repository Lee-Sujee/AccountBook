import instance from "./axiosInstance";

// 입금(저축)하기
export const createDepositApi = (challengeId, amount) => {
    return instance.post(`/challenge/${challengeId}/deposit`, {amount})
}

// 입금(저축) 내역 불러오기
export const getDepositListApi = (challengeId) => {
    return instance.get(`/challenge/${challengeId}/deposit`)
}

// 입금(저축) 요약 정보 불러오기
export const getDepositSummaryApi = (challengeId) => {
    return instance.get(`/challenge/${challengeId}/deposit/summary`)
}

export const updateDepositApi = (depositId, challengeId, amount) => {
    return instance.put(`/challenge/${challengeId}/deposit/${depositId}`, {amount})
}
