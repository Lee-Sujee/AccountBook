<template>
  <div class="home-container">
    <header class="navbar">
      <div class="logo">
        <router-link :to="{ name: 'home' }" class="logo-text">흥청망청</router-link>
      </div>

      <nav class="nav-right">
        <router-link :to="{ name: 'login' }" class="nav-link">로그인</router-link>
        <router-link :to="{ name: 'signUp' }" class="nav-link">회원가입</router-link>
      </nav>
    </header>
  </div>
</template>

<script setup>
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

</style>
