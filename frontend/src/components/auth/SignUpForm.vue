<template>
  <form @submit.prevent="handleSubmit" class="signup-form">
    <div class="form-group">
      <label for="email">이메일</label>
      <input id="email" type="email" v-model="user.email" class="underline-input" />
    </div>

    <div class="form-group">
      <label for="password">비밀번호</label>
      <input id="password" type="password" v-model="user.password" class="underline-input" />
    </div>

    <div class="form-group">
      <label for="passwordCheck">비밀번호 확인</label>
      <input id="passwordCheck" type="password" v-model="user.passwordCheck" class="underline-input" />
    </div>

    <div class="form-group">
      <label for="name">이름</label>
      <input id="name" type="text" v-model="user.name" class="underline-input" />
    </div>

    <div class="form-group">
      <label for="age">나이</label>
      <input id="age" type="number" v-model.number="user.age" class="underline-input" />
    </div>

    <div class="form-group">
      <label for="job">직업</label>
      <input id="job" type="text" v-model="user.job" class="underline-input" />
    </div>

    <button type="submit" class="submit-btn">회원가입</button>
  </form>
</template>

<script setup>
import { ref } from "vue";
import { useAuthStore } from "@/stores/auth";

const store = useAuthStore();

const user = ref({
  email: "",
  password: "",
  passwordCheck: "",
  name: "",
  age: null,
  job: "",
});

const handleSubmit = () => {
  if (user.value.password !== user.value.passwordCheck) {
    return alert("비밀번호가 일치하지 않습니다.");
  }
  if (!user.value.email.trim()) return alert("이메일을 입력해주세요.");
  if (!user.value.password.trim()) return alert("비밀번호를 입력해주세요.");
  if (!user.value.passwordCheck.trim()) return alert("비밀번호 확인을 입력해주세요.");
  if (!user.value.name.trim()) return alert("이름을 입력해주세요.");
  if (!user.value.age) return alert("나이를 입력해주세요.");

  const payload = {
    email: user.value.email,
    password: user.value.password,
    passwordCheck: user.value.passwordCheck,
    name: user.value.name,
    age: isNaN(user.value.age) ? null : user.value.age,
    job: user.value.job.trim() || null,
  };

  store.insertUser(payload);
};
</script>

<style scoped>
.signup-form {
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
  border-bottom: 2px solid rgba(0, 0, 0, 0.2);
  padding: 10px 4px;
  font-size: 15px;
  font-weight: 700;
  color: #333;
  transition: border-color 0.2s;
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
  margin-top: 20px;
  box-shadow: 0 4px 10px rgba(0, 99, 248, 0.2);
}

/* 자동 완성 시의 스타일 조정 */
input:-webkit-autofill,
input:-webkit-autofill:hover,
input:-webkit-autofill:focus,
input:-webkit-autofill:active {
  -webkit-box-shadow: 0 0 0 30px #ededed inset !important;
  -webkit-text-fill-color: #333 !important;
  transition: background-color 5000s ease-in-out 0s;
}
</style>