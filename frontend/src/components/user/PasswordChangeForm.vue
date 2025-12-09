<template>
  <form @submit.prevent="submitChange">

    <label>현재 비밀번호</label>
    <input type="password" v-model="pw.currentPassword" required />

    <label>새 비밀번호</label>
    <input type="password" v-model="pw.newPassword" required />

    <label>새 비밀번호 확인</label>
    <input type="password" v-model="pw.newPasswordCheck" required />

    <button type="submit">비밀번호 변경</button>
    <button type="button" @click="emit('cancel')">취소</button>
  </form>
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

  store.changePw(pw.value)
    .then(() => {
      emit("cancel");
    });
};
</script>
