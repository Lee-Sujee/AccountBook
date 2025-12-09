<template>
  <form @submit.prevent="handleLogin" class="login-form">
    <div class="form-group">
      <label for="email">이메일</label>
      <input id="email" type="email" v-model="email" />
    </div>

    <div class="form-group">
      <label for="password">비밀번호</label>
      <input id="password" type="password" v-model="password" />
    </div>

    <button type="submit" class="submit-btn">로그인</button>
  </form>
</template>

<script setup>
import { ref } from "vue";
import { useAuthStore } from "@/stores/auth";

const store = useAuthStore();

const email = ref("");
const password = ref("");

const handleLogin = () => {
  store.login({
    email: email.value,
    password: password.value,
  });
};
</script>

<style scoped>
.login-form {
  margin-top: 20px;
}
.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 22px;
}
label {
  font-size: 15px;
  font-weight: 600;
  margin-bottom: 6px;
  color: #1C2A3A;
}
input {
  padding: 12px 14px;
  font-size: 15px;
  border: 1px solid #D6DEE6;
  border-radius: 6px;
  background-color: #F8FAFC;
}
</style>
