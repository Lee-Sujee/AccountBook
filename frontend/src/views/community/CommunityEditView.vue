<template>
  <div v-if="communityDetail">
    <CommunityForm
      :initialData="communityDetail"
      @submit="updatePost"
    />
  </div>
</template>

<script setup>
import { watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { storeToRefs } from "pinia";

import CommunityForm from "@/components/community/CommunityForm.vue";
import { useCommunityStore } from "@/stores/community";
import { useAuthStore } from "@/stores/auth";

const route = useRoute();
const router = useRouter();

const postId = Number(route.params.id);

const communityStore = useCommunityStore();
const authStore = useAuthStore();

const { communityDetail } = storeToRefs(communityStore);
const { loginUserInfo } = storeToRefs(authStore);

communityStore.fetchCommunityDetail(postId);

watch(
  [communityDetail, loginUserInfo],
  ([detail, user]) => {
    if (!detail || !user) return;

    if (detail.writerId !== user.id) {
      alert("수정 권한이 없습니다.");
      router.replace(`/community/${postId}`);
    }
  },
  { immediate: true }
);

const updatePost = async (formData) => {
  await communityStore.updateCommunity(postId, formData);

  router.push({
    name: "CommunityDetail",
    params: { id: postId },
  });
};
</script>

<style scoped>
</style>
