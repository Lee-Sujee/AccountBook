<template>
    <div>
        <CommunityDetail :liked="liked" :likes="likes" @toggle-like="onToggleLike" />
    </div>
</template>

<script setup>
import CommunityDetail from '@/components/community/CommunityDetail.vue';
import { increaseCommunityViewApi } from '@/api/communityApi';
import { useCommunityStore } from '@/stores/community';
import { useAuthStore } from '@/stores/auth';
import { useRoute } from 'vue-router';
import { onMounted, computed } from 'vue';
import { storeToRefs } from 'pinia';

const route = useRoute();
const postId = Number(route.params.id);
const communityStore = useCommunityStore();
const authStore = useAuthStore();

const {loginUserInfo} = storeToRefs(authStore);
const { communityDetail, liked, likes } = storeToRefs(communityStore);
const isLogin = computed(() => !!loginUserInfo.value);
const onToggleLike = async () => {
  if (!isLogin.value) {
    alert("로그인 후 이용 가능합니다.");
    return;
  }
  await communityStore.toggleLike(communityDetail.value.id);
};

onMounted(() => {
  increaseCommunityViewApi(postId);      // 조회수 증가
  communityStore.fetchCommunityDetail(postId); // 상세 조회
});
</script>

<style scoped>

</style>