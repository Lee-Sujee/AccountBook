<template>
  <div class="signup-container">

    <h2 class="title">회원가입</h2>

    <form @submit.prevent class="signup-form">
      <div class="form-group">
        <label for="email">이메일</label>
        <input id="email" type="email" v-model="user.email" />
      </div>

      <div class="form-group">
        <label for="password">비밀번호</label>
        <input id="password" type="password" v-model="user.password" />
      </div>

      <div class="form-group">
        <label for="name">이름</label>
        <input id="name" type="text" v-model="user.name"/>
      </div>

      <div class="form-group">
        <label for="age">나이</label>
        <input id="age" type="number" v-model.number="user.age"/>
      </div>

      <div class="form-group">
        <label for="job">직업</label>
        <input id="job" type="text" v-model="user.job"/>
      </div>

      <button type="submit" class="submit-btn" @click="insertUser" >회원가입</button>
    </form>

  </div>
</template>

<script setup>

import { ref, watch } from 'vue'
import { useAuthStore } from '@/stores/auth'

const store = useAuthStore()

//우리가 userRequestDto에 id랑 passwordCheck 객체가 있어서 일단 이거도 저장하고 사용자한테는 안 보이도록 설정할게요.^^>>
const user = ref({
  id: "",               
  email: "",
  password: "",
  passwordCheck: '', 
  name: "",
  age : 0,
  job: "",
})

// 이메일 변경 시 id 자동 생성
watch(() => user.value.email, (newEmail) => {
  if (newEmail.includes("@")) {
    user.value.id = newEmail.split("@")[0]
  }
})

// passwordCheck 자동 설정
watch(() => user.value.password, (newPw) => {
  user.value.passwordCheck = newPw
})

const insertUser = function(){
  const userData = {
    id: user.value.id,                   
    email: user.value.email,
    password: user.value.password,
    passwordCheck: user.value.passwordCheck,  
    name: user.value.name,
    age: user.value.age,
    job: user.value.job,
  };
  store.insertUser(userData)
}

</script>

<style scoped>
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

.subtitle {
  text-align: center;
  font-size: 14px;
  color: #7d8a97;
  margin-bottom: 30px;
}

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
  transition: border-color .2s ease, background-color .2s ease;
}

input:focus {
  border-color: #1F8A4C;
  background-color: #ffffff;
}

input.valid {
  border-color: #1F8A4C;
  background-image: url('data:image/svg+xml,%3Csvg fill="%231F8A4C" height="18" width="18" viewBox="0 0 20 20"%3E%3Cpath d="M7.629 14.571l-4.9-4.9L0 12.4l7.629 7.6L20 7.629 17.271 4.9z"/%3E%3C/svg%3E');
  background-repeat: no-repeat;
  background-position: right 10px center;
  background-size: 18px;
}

.helper-text {
  margin-top: 4px;
  font-size: 13px;
  color: #7d8a97;
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
