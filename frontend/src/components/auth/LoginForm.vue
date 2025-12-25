<template>
  <form @submit.prevent="handleLogin" class="login-form">
    <div class="form-group">
      <label for="email">이메일</label>
      <input id="email" type="email" v-model="email" class="underline-input" />
    </div>

    <div class="form-group">
      <label for="password">비밀번호</label>
      <input id="password" type="password" v-model="password" class="underline-input" />
    </div>

    <div class="btn-area">
      <button type="submit" class="submit-btn">로그인</button>
      <button type="button" class="signup-btn" @click="router.push({ name: 'signUp' })">
        회원가입
      </button>
    </div>
  </form>
</template>

<script setup>
import { ref } from "vue";
import { useAuthStore } from "@/stores/auth";
import { useRouter } from "vue-router";

const store = useAuthStore();
const router = useRouter();

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
  margin-top: 30px;
}

.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 25px;
}

label {
  font-size: 14px;
  font-weight: 800;
  margin-bottom: 8px;
  color: #0063f8;
  text-align: left;
}

.underline-input {
  width: 100%;
  border: none;
  outline: none;
  background: transparent;
  border-bottom: 2px solid rgba(0, 99, 248, 0.6);
  padding: 10px 4px;
  font-size: 15px;
  font-weight: 700;
  color: #333;
  transition: border-color 0.2s;
}

.underline-input:focus {
  border-bottom-color: #0063f8;
}

input:-webkit-autofill,
input:-webkit-autofill:hover,
input:-webkit-autofill:focus,
input:-webkit-autofill:active {
  -webkit-box-shadow: 0 0 0 30px #ededed inset !important;
  -webkit-text-fill-color: #333 !important;
  transition: background-color 5000s ease-in-out 0s;
}

.btn-area {
  margin-top: 30px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.submit-btn {
  width: 100%;
  background-color: #0063f8;
  color: #ffffff;
  border: none;
  padding: 16px;
  border-radius: 30px;
  font-size: 16px;
  font-weight: 800;
  cursor: pointer;
  box-shadow: 0 4px 10px rgba(0, 99, 248, 0.2);
  transition: background-color 0.2s;
}

.submit-btn:hover {
  background-color: #0056d6;
}

.signup-btn {
  width: 100%;
  background-color: #f0f0f0;
  color: #666;
  border: 1px solid #ccc;
  padding: 16px;
  border-radius: 30px;
  font-size: 16px;
  font-weight: 800;
  cursor: pointer;
  transition: background-color 0.2s;
}

.signup-btn:hover {
  background-color: #e5e5e5;
}
</style>