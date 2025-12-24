<template>
  <div>
    <BookListView v-if="auth.isLoggedIn" />


    <div v-else class="guest-page">
      <div>
        <p class="service-description">AI와 데이터로 더 똑똑한 소비 선택을 돕는</p>
        <p class="service">금융 라이프 서비스</p>
      </div>
      <img class="main-img" :src="mainImg" alt="메인 이미지" />

      <div class="feature-grid">
        <div class="feature">
          <p class="feature-title">돈 모으기 챌린지로</p>
          <p class="feature-desc">목표있는 저축을 실천해보세요</p>
        </div>

        <div class="feature">
          <p class="feature-title">AI 평가체계로</p>
          <p class="feature-desc">합리적인 소비 기준을 판단합니다</p>
        </div>

        <div class="feature">
          <p class="feature-title">참여가 어려운 절약이 아닌</p>
          <p class="feature-desc">지속 가능한 소비 습관을 만듭니다</p>
        </div>
      </div>

    </div>

  </div>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth'
import BookListView from './book/BookListView.vue'

import mainImg from '@/assets/img/main.png'

const auth = useAuthStore()
</script>

<style scoped>
.guest-page {
  min-height: calc(100vh - 60px);
  display: flex;
  flex-direction: column;
  gap: 20px;
  align-items: center;
  justify-content: center;
}

.guest-page > div {
  display: flex;
  align-items: center;
  flex-direction: column;
  color: #0063f8;
}

.guest-page .service-description{
  font-weight: 500;
  font-size: 25px;
  margin: 0px;
}
.guest-page .service{
  font-weight: 800;
  font-size: 30px;
  padding: 0px;
  margin: 5px;
}


.main-img {
  width: 50%;
  max-width: 900px;
  height: auto;
  display: block;
}

.feature-grid {
  width: 100%;
  max-width: 1100px;
  display: grid !important;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  padding: 0 16px;
}

.feature {
  text-align: center;
  padding: 14px 10px;
  border-top: 2px solid #0063f8;
  border-bottom: 2px solid #0063f8;
}

.feature-title,
.feature-desc {
  margin: 0;
  color: #111;
}

.feature-title {
  font-weight: 700;
  font-size: 14px;
}

.feature-desc {
  font-weight: 700;
  font-size: 14px;
  color: #0063f8;
  margin-top: 6px;
}

@media (max-width: 300px) {
  .feature-grid {
    grid-template-columns: 1fr;
  }
  .main-img {
    width: 90%;
  }
}
</style>
