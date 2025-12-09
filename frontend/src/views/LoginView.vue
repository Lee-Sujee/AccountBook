<template>
  <div class="login-container">
    <h2 class="title">로그인</h2>
    <form @submit.prevent="login" class="login-form">
      <div class="form-group">
        <label for="email">이메일</label>
        <input id="email" type="email" v-model="email">
      </div>

      <div class="form-group">
        <label for="password">비밀번호</label>
        <input id="password" type="password" v-model="password">
      </div>

      <button type="submit" class="submit-btn">로그인</button>
    </form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useAuthStore } from '@/stores/auth';

const store = useAuthStore()

const email = ref("")
const password = ref("")

const login = () => {
  store.login({email: email.value, password: password.value})
}
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
  transition: border-color .2s ease, background-color .2s ease;
}

input:focus {
  border-color: #1F8A4C;
  background-color: #ffffff;
}

.submit-btn {
  width: 100%;
  padding: 14px;
  margin-top: 10px;
  font-size: 17px;
  text-align: center;
  background-color: #1F8A4C;
  color: #ffffff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: .2s ease;
}

.submit-btn:hover {
  background-color: #187342;
}
</style>
