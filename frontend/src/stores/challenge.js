import { ref } from "vue";
import { defineStore } from "pinia";
import { createChallengeApi, getChallengeListApi, getOneChallengeApi, updateChallengeApi, deleteChallengeApi } from "@/api/challengeApi";
import router from "@/router";

export const useChallengeStore = defineStore("challenge", () => {


    const createChallenge = (data) => {
        return createChallengeApi(data)
        .then((res) => {
            alert("챌린지가 등록되었습니다");
            router.push({ name: "ChallengeList"});
        })
        .catch((err) => {
            console.error("챌린지 등록 실패: ", err);
            throw err;
        })
    }

    const challengeList = ref([])
    const getChallengeList = () => {
        return getChallengeListApi()
        .then((res) => {
            challengeList.value = res.data;
        })
        .catch((err) => {
            console.error("챌린지 불러오기 실패: ", err);
            throw err;
        })
    }

    const challenge = ref()
    const getOneChallenge = (challengeId) => {
        return getOneChallengeApi(challengeId)
        .then((res) => {
            challenge.value = res.data;
        })
        .catch((err) => {
            console.error("단일 챌린지 불러오기 실패: ", err);
            throw err;
        })
    }

    const updateChallenge = (challengeId, data ) => {
        return updateChallengeApi(challengeId, data)
        .then(() => {
            alert("챌린지가 수정되었습니다");
        })
        .catch((err) => {
            console.error("챌린지 수정 실패: ", err);
            throw err;
        })
    }

    const deleteChallenge = (challengeId) => {
        return deleteChallengeApi(challengeId)
        .then(() => {
            alert("해당 챌린지가 삭제되었습니다");
        })
        .catch((err) => {
            console.error("챌린지 삭제 실패: ", err);
            throw err;
        })
    }

    return{
        challengeList,
        challenge,
        createChallenge,
        getChallengeList,
        getOneChallenge,
        updateChallenge,
        deleteChallenge,
    };
});
