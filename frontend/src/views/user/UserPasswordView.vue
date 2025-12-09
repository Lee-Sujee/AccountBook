<template>
  <div class="pw-container">
    <h2>비밀번호 변경</h2>

    <passwordChangeForm @cancel="goBack" />
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import passwordChangeForm from "@/components/user/PasswordChangeForm.vue";

const router = useRouter();

const goBack = () => {
  router.push({ name: "myPage" });
};
</script>
