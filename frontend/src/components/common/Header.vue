<template>
  <div class="home-container">
    <header class="navbar">
      <div class="logo">
        <router-link :to="{ name: 'home' }" class="logo-text"><img class="logo-img" src="@/assets/img/logo.png"></router-link>
        <router-link :to="{ name: 'home' }" class="nav-link">가계부</router-link>
        <router-link :to="{ name: 'ChallengeList' }" class="nav-link">챌린지</router-link>
        <router-link :to="{name: 'CommunityList'}" class="nav-link">게시판</router-link>
        <router-link :to="{name: 'AverageCalculator'}" class="nav-link">평균계산기</router-link>
      </div>

      <nav class="nav-right">
        <!--로그인 성공적으로 됐을 경우-->
        <template v-if="store.loginUserInfo">
          <span class="welcome">{{ store.loginUserInfo.name }}님 안녕하세요!</span>
          <router-link :to="{name: 'myPage'}" class="nav-link">마이페이지</router-link>
          <button class="logout-btn" @click="store.logout">로그아웃</button>
        </template>

        <!--로그인하지 않은 경우-->
        <template v-else>
          <router-link :to="{ name: 'login' }" class="nav-link">로그인</router-link>
          <router-link :to="{ name: 'signUp' }" class="nav-link">회원가입</router-link>
        </template>
      </nav>
    </header>
  </div>
</template>

<script setup>
import router from '@/router';
import { useAuthStore } from '@/stores/auth';

const store = useAuthStore();
</script>

<style scoped>
/* 전체 navbar */
.navbar {
  width: 100%;
  padding: 14px 32px;
  display: flex;
  justify-content: space-between;
  align-items: center;          
  box-sizing: border-box;
}

/* 왼쪽 로고 + 메뉴 */
.logo {
  font-size: 14px;
  font-weight: 700; 
  display: flex;
  align-items: center;          /* 🔑 로고/텍스트 같은 줄 */
  gap: 20px;                    /* 메뉴 사이 간격 */
  
  
}

/* 로고 이미지 */
.logo-img {
  width: 180px;
  height: auto;
  vertical-align: middle;       /* 🔑 이미지 baseline 보정 */
}

/* 공통 네비 링크 */
.nav-link {
  text-decoration: none;
  line-height: 1;  
  color: #0063f8;            /* 🔑 세로 흔들림 제거 */
}

/* 오른쪽 영역 */
.nav-right {
  font-size: 12px;
  display: flex;
  align-items: center;          /* 🔑 텍스트/버튼 중앙 */
  gap: 18px;
}

/* 환영 문구 */
.welcome {
  font-size: 12px;
  font-weight: 700;
  color: #0063f8;
  white-space: nowrap;
}

/* 로그아웃 버튼 */
.logout-btn {
  padding: 6px 14px;
  font-size: 12px;
  font-weight: 700;
  border: none;
  border-radius: 6px;
  background-color: #0063f8;
  color: #ffffff;
  cursor: pointer;
}

</style>
