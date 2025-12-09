<template>
  <form @submit.prevent="handleSubmit" class="signup-form">

    <div class="form-group">
      <label for="email">이메일</label>
      <input id="email" type="email" v-model="user.email" />
    </div>

    <div class="form-group">
      <label for="password">비밀번호</label>
      <input id="password" type="password" v-model="user.password" />
    </div>

    <div class="form-group">
      <label for="passwordCheck">비밀번호 확인</label>
      <input id="passwordCheck" type="password" v-model="user.passwordCheck" />
    </div>

    <div class="form-group">
      <label for="name">이름</label>
      <input id="name" type="text" v-model="user.name" />
    </div>

    <div class="form-group">
      <label for="age">나이</label>
      <input id="age" type="number" v-model.number="user.age" />
    </div>

    <div class="form-group">
      <label for="job">직업</label>
      <input id="job" type="text" v-model="user.job" />
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
  age: 0,
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
/* 👉 원래 SignUpView에 있던 form 관련 스타일만 이곳에 배치해도 됩니다 */
.signup-form {
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
