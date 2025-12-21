import instance from "./axiosInstance";

// 챌린지 등록
export const createChallengeApi = (data) => {
    return instance.post(`/challenge`, data)
}

// 챌린지 전체조회
export const getChallengeListApi = () => {
    return instance.get(`/challenge`)
}

// 챌린지 단일조회
export const getOneChallengeApi = (challengeId) => {
    return instance.get(`/challenge/${challengeId}`)
}

// 챌린지 수정
export const updateChallengeApi = (challengeId, data) => {
    return instance.put(`/challenge/${challengeId}`, data)
}

// 챌린지 삭제
export const deleteChallengeApi = (challengeId) => {
    return instance.delete(`/challenge/${challengeId}`)
}