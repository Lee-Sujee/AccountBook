<template>
    <div class="mypage-container">
        <h2>마이페이지</h2>

        <div class="section">
            <h3>회원 정보 수정</h3>

            <div class="form-group">
                <label>이메일 (수정불가)</label>
                <input type="text" :value="loginUser.email" disabled />
            </div>

            <div class="form-group">
                <label>이름</label>
                <input v-model="updateForm.name" type="text" />
            </div>

            <div class="form-group">
                <label>나이</label>
                <input v-model="updateForm.age" type="number" />
            </div>

            <div class="form-group">
                <label>직업</label>
                <input v-model="updateForm.job" type="text" />
            </div>

            <button @click="updateUserInfo">정보 수정</button>
        </div>
        <hr></hr>
        
        <div class="section">
            <h3>비밀번호 변경</h3>
            
            <div class="form-group">
                <label>현재 비밀번호</label>
                <input v-model="passwordForm.currentPassword" type="password" />
            </div>
            
            <div class="form-group">
                <label>새 비밀번호</label>
                <input v-model="passwordForm.newPassword" type="password" />
            </div>
            
            <div class="form-group">
                <label>비밀번호 확인</label>
                <input v-model="passwordForm.newPasswordCheck" type="password" />
            </div>
            
            <button @click="changePassword">비밀번호 변경</button>
        </div>
    </div>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth';
import axios from 'axios';
import { ref } from 'vue';

const store = useAuthStore();
const loginUser = store.loginUser

//회원 정보 수정
const updateForm = ref({
    name: loginUser.value.name,
    age: loginUser.value.age,
    job: loginUser.value.job
})

//비밀번호 수정
const passwordForm = ref({
    currentPassword: "",
    newPassword: "",
    newPasswordCheck: ""
})

//회원 정보 수정 
const updateUserInfo = () => {
    axios({
        url: "/api/user/myPage",
        method: "PUT",
        headers: {
            Authorization: `Bearer ${loginUser.value.token}`
        },
        data: updateForm.value
    })
    .then(res => {
        alert("회원 정보가 수정되었습니다.")

        //store 저장값 변경
        store.loginUser.value.name = res.data.name
        store.loginUser.value.age = res.data.age
        store.loginUser.value.job = res.data.job
    })
    .catch(err => {
        alert("정보 수정 실패")
        console.log(err)
    })
}

//비밀번호 변경
const changePassword = () => {
    axios({
        url: `api/user/password/${loginUser.value.email}`,
        method: "PUT",
        data: passwordForm.value
    })
    .then(res => {
        alert("비밀번호가 성공적으로 변경되었습니다.")
    })
    .catch(err => {
        alert("비밀번호 변경 실패: 현재 비밀번호가 틀렸거나 새 비밀번호가 일치하지 않습니다.")
        console.log(err)
    })
}
</script>

<style scoped>
.mypage-container {
  max-width: 500px;
  margin: 40px auto;
}

.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 15px;
}
button {
  width: 100%;
  padding: 10px;
  background-color: #1F8A4C;
  color: white;
  font-size: 16px;
  border: none;
  margin-top: 10px;
  cursor: pointer;
  border-radius: 4px;
}
button:hover {
  background-color: #176b3a;
}
.section {
  margin-bottom: 40px;
}
</style>