<template>
  <div class="center-wrap">
    <form class="form" @submit.prevent="updateInfo">
      <div class="input-group">
        <label>이름</label>
        <input v-model="editUser.name" class="underline-input" />
      </div>

      <div class="input-group">
        <label>나이</label>
        <input type="number" v-model.number="editUser.age" class="underline-input" />
      </div>

      <div class="input-group">
        <label>직업</label>
        <input v-model="editUser.job" class="underline-input" />
      </div>

      <div class="btn-area">
        <button type="submit" class="btn-primary">저장</button>

        <button
          type="button"
          class="btn-secondary"
          @click="router.push({ name: 'password' })"
        >
          비밀번호 변경
        </button>

        <button type="button" class="btn-ghost" @click="emit('cancel')">
          취소
        </button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, watch } from "vue";
import { useAuthStore } from "@/stores/auth";
import { useRouter } from "vue-router";

const router = useRouter();
const store = useAuthStore();
const emit = defineEmits(["cancel"]);

const editUser = ref({
  name: "",
  age: 0,
  job: "",
});

watch(
  () => store.myPageInfo,
  (data) => {
    if (data) editUser.value = { ...data };
  },
  { immediate: true }
);

const updateInfo = () => {
  store.updateMyPage(editUser.value).then(() => {
    alert("수정 완료!");
    emit("cancel"); // 수정 화면 종료
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
  margin-top: 10px;
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

.btn-secondary {
  width: 320px;
  max-width: 92vw;
  background: #f0f0f0;
  color: #666;
  border: 1px solid #d0d0d0;
  border-radius: 999px;
  padding: 13px 18px;
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
.btn-secondary:hover { filter: brightness(0.98); }
.btn-ghost:hover { text-decoration: underline; }
</style>
