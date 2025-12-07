import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'
import router from '@/router'

const REST_API_URL = "http://localhost:8080/user"

export const useAuthStore = defineStore('auth', () => {
  

  const user = ref({})
  const userList = ref([])
  const insertUser = function(user){
    axios({
      url: `${REST_API_URL}/signUp`,
      method: "POST",
      data : user
    })
    .then((res) => {
      console.log(user.value);
      // router.push({name: "userList"})
    })
    .catch((err)=>{
      console.log(err)
    })
  }

  return { insertUser, user, userList, }
})
