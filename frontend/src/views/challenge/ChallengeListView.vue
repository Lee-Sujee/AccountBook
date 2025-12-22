<template>
    <div v-if="!currentChallenge" class="empty-message">
      아직 등록된 챌린지가 없습니다!
    </div>

    <div v-else class="challenge-container">
      <ChallengeCard :challenge="currentChallenge" @edit="goEdit" @delete="deleteChallenge"/>
    </div>
    <button class="create-btn" @click="router.push({ name: 'ChallengeCreate' })">챌린지 만들기</button>
    <ChallengePagination :page-numbers="pageNumbers" :current-page="currentPage" :is-first-group="currentGroup === 0"
        :is-last-group="(currentGroup + 1) * pageSize >= totalPage" @change-page="currentPage = $event"
        @prev-group="prevGroup" @next-group="nextGroup"/>

</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import ChallengeCard from '@/components/challenge/ChallengeCard.vue';
import ChallengePagination from '@/components/challenge/ChallengePagination.vue';
import { useChallengeStore } from '@/stores/challenge';
import { storeToRefs } from 'pinia';
import router from '@/router';

const challengeStore = useChallengeStore();
const { challengeList } = storeToRefs(challengeStore);
const currentPage = ref(0);
const currentChallenge = computed(() => {
  if (!challengeList.value || challengeList.value.length === 0) {
    return null;
  }
  return challengeList.value[currentPage.value];
});
const pageSize = 5;
const totalPage = computed(() => challengeList.value.length);
const currentGroup = computed(() => {
    return Math.floor(currentPage.value / pageSize);
})
const pageNumbers = computed(() => {
  const start = currentGroup.value * pageSize;
  const end = Math.min(start + pageSize, totalPage.value);

  return Array.from(
    { length: end - start },
    (_, i) => start + i
  );
})
const prevGroup = () => {
  const prevStart = (currentGroup.value - 1) * pageSize;
  if (prevStart >= 0) {
    currentPage.value = prevStart;
  }
};
const nextGroup = () => {
  const nextStart = (currentGroup.value + 1) * pageSize;
  if (nextStart < totalPage.value) {
    currentPage.value = nextStart;
  }
};
onMounted(() => {
    challengeStore.getChallengeList();
})

// 챌린지 수정
const goEdit = () => {
  router.push({
    name: 'ChallengeEdit',
    params: { challengeId: currentChallenge.value.id }
  })
}

// 챌린지 삭제
const deleteChallenge = async () => {
  if (!confirm('정말 삭제하시겠습니까?')) return

  await challengeStore.deleteChallenge(currentChallenge.value.id)
  await challengeStore.getChallengeList()
}

</script>

<style scoped>
/* 비어 있을 때 메시지 */
.empty-message {
  text-align: center;
  margin-top: 40px;
  font-size: 15px;
  color: #777;
}

/* 챌린지 카드 영역 정렬 */
.challenge-container {
  
  justify-content: center;
  margin-top: 16px;
}

/* 챌린지 생성 버튼 */
.create-btn {
  display: block;
  width: 200px;
  margin: 20px auto 12px;
  padding: 10px;
  background-color: #0063f8;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
}

.create-btn:hover {
  background-color: #0063f8;
}

</style>