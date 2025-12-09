import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import LoginView from '@/views/user/UserLoginView.vue'
import SignUpView from '@/views/user/UserSignUpView.vue'
import MyPageView from '@/views/user/UserProfileView.vue'
import { useAuthStore } from '@/stores/auth'
import PasswordChangeView from '@/views/user/UserPasswordView.vue'


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
      component: LoginView,
    },
    {
      path: '/signUp',
      name: 'signUp',
      component: SignUpView,
    },
    {
      path: '/myPage',
      name: 'myPage',
      component: MyPageView,
      meta: {requiresAuth: true} //로그인 해야만 접근 가능
    },
    {
      path: '/password',
      name: 'password',
      component: PasswordChangeView,
      meta: {requiresAuth: true} //로그인 해야만 접근 가능
    },
  ],
})

//토큰 사용해서 로그인 안 한 사용자 -> 마이페이지 접근 못하게 막기
router.beforeEach((to, from, next) => {
  const store = useAuthStore()

  //마이페이지 접근 시 로그인 체크 
  if(to.meta.requiresAuth && !store.loginUserInfo) {
    console.log(store.loginUserInfo)
    alert("로그인이 필요한 페이지입니다.")
    return next({name: 'login'})
  }

  next()
})

export default router
