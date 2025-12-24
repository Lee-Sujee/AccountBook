<template>
  <div class="page">
    <h2 class="page-title">마이페이지</h2>

    <div v-if="user" class="form">
      <div class="field">
        <div class="label">이메일</div>
        <div class="value">{{ user.email }}</div>
      </div>

      <div class="field">
        <div class="label">이름</div>
        <div class="value">{{ user.name }}</div>
      </div>

      <div class="field">
        <div class="label">나이</div>
        <div class="value">{{ user.age }}</div>
      </div>

      <div class="field">
        <div class="label">직업</div>
        <div class="value">{{ user.job }}</div>
      </div>

      <div class="btn-area">
        <button class="btn-primary" @click="emit('edit')">회원정보수정</button>
      </div>
    </div>

    <div v-else class="loading">마이페이지 정보를 불러오는 중입니다...</div>
  </div>
</template>

<script setup>
import { useAuthStore } from "@/stores/auth";
import { computed } from "vue";

const emit = defineEmits(["edit"]);
const store = useAuthStore();
const user = computed(() => store.myPageInfo);
</script>

<style scoped>
.page {
  width: 100%;
  min-height: calc(100vh - 70px);
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 90px 16px 60px;
  box-sizing: border-box;
  font-family: 'Pretendard', sans-serif;
}

.page-title {
  color: #0063f8;
  font-weight: 800;
  font-size: 20px;
  margin: 0 0 44px;
  text-align: center;
}

.form {
  width: 360px;
  max-width: 92vw;
}

.field {
  display: grid;
  grid-template-columns: 90px 1fr;
  align-items: center;
  gap: 18px;
  padding: 14px 0;
  border-bottom: 2px solid rgba(0, 99, 248, 0.6);
  margin-bottom: 15px;
}

.label {
  color: #0063f8;
  font-weight: 800;
  font-size: 13px;
  text-align: left;
}

.value {
  color: #333;
  font-weight: 700;
  font-size: 14px;
  text-align: left;
  word-break: break-word;
}

.btn-area {
  margin-top: 26px;
  display: flex;
  justify-content: center;
}

.btn-primary {
  width: 320px;
  max-width: 92vw;
  background: #0063f8;
  color: #fff;
  border: none;
  border-radius: 999px;
  padding: 14px 18px;
  font-weight: 900;
  cursor: pointer;
}

.btn-primary:hover {
  filter: brightness(0.97);
}

.loading {
  margin-top: 30px;
  color: #666;
  font-weight: 700;
}
</style>
