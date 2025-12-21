<template>
    <div>
        <h2>챌린지 작성 뷰</h2>
        <ChallengeForm @submit="createChallenge" />
    </div>
</template>

<script setup>
import ChallengeForm from '@/components/challenge/ChallengeForm.vue';
import { useChallengeStore } from '@/stores/challenge';
import { useRouter } from 'vue-router';

const challengeStore = useChallengeStore();
const router = useRouter();
const createChallenge = async(formData) => {
    await challengeStore.createChallenge(formData);
    router.push({name: "ChallengeList"});
}
</script>

<style scoped>

</style>