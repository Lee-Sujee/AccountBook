<template>
  <div class="signup-container">
    <h2 class="title">회원가입</h2>
    <SignUpForm />
  </div>
</template>

<script setup>
import SignUpForm from "@/components/auth/SignUpForm.vue";
</script>

<style scoped>
/* layout 스타일은 여기 유지 */
.signup-container {
  max-width: 480px;
  margin: 50px auto;
  padding: 30px;
  background-color: #ffffff;
}
.title {
  text-align: center;
  font-size: 26px;
  font-weight: 700;
  margin-bottom: 10px;
  color: #1C2A3A;
}
</style>
