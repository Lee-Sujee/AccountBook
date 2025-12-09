<template>
  <div v-if="user">
    <p><strong>이메일:</strong> {{ user.email }}</p>
    <p><strong>이름:</strong> {{ user.name }}</p>
    <p><strong>나이:</strong> {{ user.age }}</p>
    <p><strong>직업:</strong> {{ user.job }}</p>

    <button @click="emit('edit')">회원정보 수정</button>
  </div>

  <div v-else>
    마이페이지 정보를 불러오는 중입니다...
  </div>
</template>

<script setup>
import { useAuthStore } from "@/stores/auth";
import { computed } from "vue";

const emit = defineEmits(["edit"]);   // ★★★ 여기가 핵심

const store = useAuthStore();
const user = computed(() => store.myPageInfo);
</script>
