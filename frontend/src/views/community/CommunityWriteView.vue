<template>
  <div>
    <CommunityForm @submit="createPost" />
  </div>
</template>

<script setup>
import CommunityForm from "@/components/community/CommunityForm.vue";
import { useCommunityStore } from "@/stores/community";
import { useRouter } from "vue-router";

const communityStore = useCommunityStore();
const router = useRouter();

const createPost = async (formData) => {
  await communityStore.createCommunity(formData);

  router.push({ name: "CommunityList" });
};
</script>

<style scoped>
</style>
