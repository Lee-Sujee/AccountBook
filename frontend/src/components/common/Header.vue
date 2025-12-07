<template>
  <div class="home-container">
    <header class="navbar">
      <div class="logo">
        <router-link :to="{ name: 'home' }" class="logo-text">지출연구소</router-link>
      </div>

      <nav class="nav-right">
        <!--로그인 성공적으로 됐을 경우-->
        <template v-if="store.loginUser">
          <span class="welcome">{{ store.loginUser.name }}님 안녕하세요!</span>
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
import { useAuthStore } from '@/stores/auth';

const store = useAuthStore();
</script>

<style scoped>
.navbar {
  width: 100%;
  padding: 20px 40px;
  background-color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid #eef1f7;
  box-sizing: border-box;
}

.logo-text {
  font-size: 24px;
  font-weight: 700;
  color: #1C2A3A;
  text-decoration: none;
}

.nav-right {
  display: flex;
  gap: 28px;
}

.nav-link {
  text-decoration: none;
  color: #5a6a85;
  font-size: 16px;
  font-weight: 500;
  transition: 0.2s ease;
  padding: 6px 8px;
  border-radius: 6px;
}

.nav-link:hover {
  color: #1F8A4C;
  background-color: rgba(31, 138, 76, 0.1);
}

.welcome {
  font-size: 16px;
  font-weight: 600;
  color: #1C2A3A;
}

.logout-btn {
  padding: 6px 12px;
  border: none;
  background-color: #1F8A4C;
  color: #ffffff;
  border-radius: 6px;
  cursor: pointer;
  transition: .2s ease;
  font-size: 15px;
}

.logout-btn:hover {
  background-color: #187342;
}
</style>
