<template>
  <div class="center-wrap">
    <form class="form" @submit.prevent="submitChange">
      <div class="input-group">
        <label>현재 비밀번호</label>
        <input
          type="password"
          v-model="pw.currentPassword"
          class="underline-input"
          required
        />
      </div>

      <div class="input-group">
        <label>새 비밀번호</label>
        <input
          type="password"
          v-model="pw.newPassword"
          class="underline-input"
          required
        />
      </div>

      <div class="input-group">
        <label>새 비밀번호 확인</label>
        <input
          type="password"
          v-model="pw.newPasswordCheck"
          class="underline-input"
          required
        />
      </div>

      <div class="btn-area">
        <button type="submit" class="btn-primary">비밀번호 변경</button>
        <button type="button" class="btn-ghost" @click="emit('cancel')">취소</button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useAuthStore } from "@/stores/auth";

const emit = defineEmits(["cancel"]);
const store = useAuthStore();

const pw = ref({
  currentPassword: "",
  newPassword: "",
  newPasswordCheck: "",
});

const submitChange = () => {
  if (pw.value.newPassword !== pw.value.newPasswordCheck) {
    alert("새 비밀번호가 일치하지 않습니다!");
    return;
  }

  store.changePw(pw.value).then(() => {
    emit("cancel");
  });
};
</script>

<style scoped>
.center-wrap {
  width: 100%;
  display: flex;
  justify-content: center;
}

.form {
  width: 360px;
  max-width: 92vw;
  font-family: 'Pretendard', sans-serif;
}

.input-group {
  margin-bottom: 22px;
}

.input-group label {
  display: block;
  color: #0063f8;
  font-weight: 800;
  font-size: 13px;
  margin-bottom: 10px;
}

.underline-input {
  width: 100%;
  border: none;
  outline: none;
  background: transparent;
  border-bottom: 2px solid rgba(0, 99, 248, 0.6);
  padding: 10px 4px;
  font-size: 14px;
  font-weight: 700;
  color: #333;
}

.btn-area {
  margin-top: 18px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
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

.btn-ghost {
  background: transparent;
  border: none;
  color: #666;
  font-weight: 800;
  cursor: pointer;
}

.btn-primary:hover { filter: brightness(0.97); }
.btn-ghost:hover { text-decoration: underline; }
</style>
