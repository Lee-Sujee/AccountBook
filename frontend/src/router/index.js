import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import UserLoginView from '@/views/user/UserLoginView.vue'
import UserPasswordView from '@/views/user/UserPasswordView.vue'
import UserProfileView from '@/views/user/UserProfileView.vue'
import UserSignUpView from '@/views/user/UserSignUpView.vue'
import { useAuthStore } from '@/stores/auth'
import CommunityListView from '@/views/community/CommunityListView.vue'
import CommunityWriteView from '@/views/community/CommunityWriteView.vue'
import CommunityDetailView from '@/views/community/CommunityDetailView.vue'
import ChallengeListView from '@/views/challenge/ChallengeListView.vue'
import ChallengeCreateView from '@/views/challenge/ChallengeCreateView.vue'
import ChallengeEditView from '@/views/challenge/ChallengeEditView.vue'
import AverageCalculatorView from '@/views/AverageCalculatorView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
      meta: { requiresAuth: true }
    },
    {
      path: '/user/login',
      name: 'login',
      component: UserLoginView,
    },
    {
      path: '/user/signUp',
      name: 'signUp',
      component: UserSignUpView,
    },
    {
      path: '/user/myPage',
      name: 'myPage',
      component: UserProfileView,
      meta: { requiresAuth: true } // 로그인 해야만 접근 가능
    },
    {
      path: '/user/password',
      name: 'password',
      component: UserPasswordView,
      meta: { requiresAuth: true } // 로그인 해야만 접근 가능
    },
    {
      path: "/community",
      name: "CommunityList",
      component: CommunityListView,
    },
    {
      path: "/community/write",
      name: "CommunityWrite",
      component: CommunityWriteView,
      meta: { requiresAuth: true } // 로그인 해야만 접근 가능
    },
    {
      path: "/community/:id",
      name: "CommunityDetail",
      component: CommunityDetailView,
    },
    {
      path: "/community/:id/edit",
      name: "CommunityEditView",
      component: () => import("@/views/community/CommunityEditView.vue"),
      meta: { requiresAuth: true } // 로그인 해야만 접근 가능
    },
    {
      path: '/average',
      name: 'AverageCalculator',
      component: AverageCalculatorView
    },
    {
      path: "/challenge/create",
      name: "ChallengeCreate",
      component: ChallengeCreateView,
      meta: { requiresAuth: true } //로그인 해야만 접근 가능
    },
    {
      path: "/challenge",
      name: "ChallengeList",
      component: ChallengeListView,
      meta: { requiresAuth: true } //로그인 해야만 접근 가능
    },
    {
      path: "/challenge/:challengeId/edit",
      name: "ChallengeEdit",
      component: ChallengeEditView,
      meta: { requiresAuth: true }
    }

  ],
})

// 토큰 사용해서 로그인 안 한 사용자 -> 마이페이지 접근 제한
router.beforeEach((to, from, next) => {
  const store = useAuthStore();

  if (to.meta.requiresAuth && !store.loginUserInfo) {
    alert("로그인이 필요한 페이지입니다.")
    return next({ name: 'login' })
  }

  next();
})

export default router;
