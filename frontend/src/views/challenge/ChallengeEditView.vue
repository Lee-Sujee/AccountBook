<template>
    <div>
        <h2>챌린지 수정하기</h2>
        <ChallengeForm v-if="challenge" :initial-data="challenge"
            @submit="editChallenge"/>
    </div>
</template>

<script setup>
import { onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useChallengeStore } from '@/stores/challenge'
import ChallengeForm from '@/components/challenge/ChallengeForm.vue'

const route = useRoute()
const router = useRouter()
const challengeStore = useChallengeStore()

const challengeId = Number(route.params.challengeId)

const challenge = computed(() =>
  challengeStore.challengeList.find(c => c.id === challengeId)
)

onMounted(async () => {
  if (!challenge.value) {
    await challengeStore.getChallengeList()
  }
})

const editChallenge = async (formData) => {
  await challengeStore.updateChallenge(challengeId, formData)
  router.push({ name: 'ChallengeList' })
}
</script>

<style scoped>

</style>