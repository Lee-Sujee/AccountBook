import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import UserLoginView from '@/views/user/UserLoginView.vue'
import UserPasswordView from '@/views/user/UserPasswordView.vue'
import UserProfileView from '@/views/user/UserProfileView.vue'
import UserSignUpView from '@/views/user/UserSignUpView.vue'
import { useAuthStore } from '@/stores/auth'


const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/login',
      name: 'login',
      component: UserLoginView,
    },
    {
      path: '/signUp',
      name: 'signUp',
      component: UserSignUpView,
    },
    {
      path: '/myPage',
      name: 'myPage',
      component: UserProfileView,
      meta: {requiresAuth: true} //로그인 해야만 접근 가능
    },
    {
      path: '/password',
      name: 'password',
      component: UserPasswordView,
      meta: {requiresAuth: true} //로그인 해야만 접근 가능
    },
  ],
})

//토큰 사용해서 로그인 안 한 사용자 -> 마이페이지 접근 못하게 막기
router.beforeEach((to, from, next) => {
  const store = useAuthStore()

  //마이페이지 접근 시 로그인 체크 
  if (to.meta.requiresAuth && !store.loginUserInfo) {
    alert("로그인이 필요한 페이지입니다.")
    return next({name: 'login'})
  }

  next()
})

export default router
