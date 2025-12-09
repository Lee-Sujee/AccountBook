<template>
  <div class="login-container">
    <h2 class="title">로그인</h2>
    <LoginForm />
  </div>
</template>

<script setup>
import LoginForm from "@/components/auth/LoginForm.vue";
</script>

<style scoped>
.login-container {
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
