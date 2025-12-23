<template>
  <div class="pw-page">
    <h2 class="page-title">비밀번호 변경</h2>
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

<style scoped>
.pw-page {
  width: 100%;
  min-height: calc(100vh - 70px);
  background: #ededed;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 90px 16px 60px;
  box-sizing: border-box;
  font-family: 'Pretendard', sans-serif;
}

.page-title {
  color: #0063f8;
  font-weight: 800;
  font-size: 20px;
  margin: 0 0 44px;
  text-align: center;
}
</style>
