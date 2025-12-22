<template>
    <div>
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