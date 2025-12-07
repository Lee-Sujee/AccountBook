import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'
import router from '@/router'

const REST_API_URL = "/api/user"

export const useAuthStore = defineStore('auth', () => {
  

  const user = ref({})
  const userList = ref([])
  const loginUser = ref(null) //loginUser 선언 

  //회원가입
  const insertUser = function(userData){
    axios({
      url: `${REST_API_URL}/signUp`,
      method: "POST",
      data : userData
    })
    .then((res) => {
      // console.log(user.value);
      // router.push({name: "userList"})
      console.log("회원가입 성공 응답: ", res.data);
      console.log("전송한 데이터: ", userData);
      router.push({name: 'login'})
    })
    .catch((err)=>{
      console.log(err)
    })
  }

  //로그인
  const login = function(loginData){
    axios({
      url: `${REST_API_URL}`,
      method: "POST",
      data: loginData
    })
    .then((res)=>{
      console.log("로그인 성공: ", res.data)

      loginUser.value = res.data //로그인 정보 여기다 저장

      router.push({name: 'home'})
    })
    .catch((err)=>{
      console.log("로그인 실패: ", err)
      alert("이메일 또는 비밀번호가 올바르지 않습니다.")
    })
  }

  //로그아웃
  const logout = function(){
    loginUser.value = null
    router.push({name: 'login'})
  }
  return { insertUser, user, userList, loginUser, login, logout}
})
