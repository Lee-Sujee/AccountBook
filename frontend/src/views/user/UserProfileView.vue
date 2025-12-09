<template>
  <div class="mypage-container">
    <h1>마이페이지</h1>

    <!-- 보기 화면 -->
    <UserInfo 
      v-if="mode === 'view'" 
      @edit="mode = 'edit'"
    />

    <!-- 수정 화면 -->
    <UserEditForm
      v-else
      @cancel="mode = 'view'"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useAuthStore } from "@/stores/auth";
import UserInfo from "@/components/user/UserInfo.vue";
import UserEditForm from "@/components/user/UserEditForm.vue";

const store = useAuthStore();
const mode = ref("view");

onMounted(() => {
  store.fetchMyPage();
});
</script>

<style>
    
</style>