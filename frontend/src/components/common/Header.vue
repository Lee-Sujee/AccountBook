<template>
  <div class="home-container">
    <header class="navbar">
  <router-link :to="{ name: 'home' }" class="logo-text">
    <img class="logo-img" src="@/assets/img/logo.png">
  </router-link>

  <nav class="nav-center">
    <router-link :to="{ name: 'book' }" class="nav-link">가계부</router-link>
    <router-link :to="{ name: 'ChallengeList' }" class="nav-link">챌린지</router-link>
    <router-link :to="{name: 'CommunityList'}" class="nav-link">게시판</router-link>
    <router-link :to="{name: 'AverageCalculator'}" class="nav-link">평균계산기</router-link>
  </nav>

  <nav class="nav-right">
    <template v-if="store.loginUserInfo">
      <span class="welcome">{{ store.loginUserInfo.name }}님 안녕하세요!</span>
      <router-link :to="{name: 'myPage'}" class="nav-link">마이페이지</router-link>
      <button class="logout-btn" @click="store.logout">로그아웃</button>
    </template>
    <template v-else>
      <router-link :to="{ name: 'login' }" class="nav-link">로그인</router-link>
      <router-link :to="{ name: 'signUp' }" class="nav-link">회원가입</router-link>
    </template>
  </nav>
</header>

  </div>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth';

const store = useAuthStore();
</script>

<style scoped>
.navbar {
  width: 100%;
  padding: 14px 32px;
  display: grid;
  grid-template-columns: auto 1fr auto; 
  align-items: center;
  box-sizing: border-box;
  gap: 16px;
}

.logo-img {
  width: 180px;
  height: auto;
  vertical-align: middle;
}

.nav-center {
  display: flex;
  justify-content: center;  
  align-items: center;
  gap: 34px;               
  flex-wrap: wrap;
}

.nav-link {
  text-decoration: none;
  color: #0063f8;
  line-height: 1;
  font-size: 16px;           
  font-weight: 700;
}


.nav-right {
  display: flex;
  align-items: center;
  gap: 20px;                
  justify-content: flex-end;
  flex-wrap: wrap;
}

.welcome {
  font-size: 14px;         
  font-weight: 700;
  color: #0063f8;
  white-space: nowrap;
}

.logout-btn {
  padding: 6px 14px;
  font-size: 14px;         
  font-weight: 700;
  border: none;
  border-radius: 6px;
  color: #0063f8;
  cursor: pointer;
}

/* 모바일 대응 */
@media (max-width: 768px) {
  .navbar {
    grid-template-columns: 1fr; 
    gap: 12px;
    padding: 12px 16px;
  }

  .nav-center {
    justify-content: flex-start;
    gap: 16px;
  }

  .nav-right {
    justify-content: flex-start;
    gap: 12px;
  }

  .logo-img {
    width: 130px;
  }
}

</style>
