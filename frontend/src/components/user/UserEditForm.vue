<template>
  <form @submit.prevent="updateInfo">
    <label>이름</label>
    <input v-model="editUser.name" />

    <label>나이</label>
    <input type="number" v-model.number="editUser.age" />

    <label>직업</label>
    <input v-model="editUser.job" />

    <button type="submit">저장하기</button>
    <button type="button" @click="emit('cancel');">취소</button>
    <button @click="router.push({name : 'password'})">비밀번호 변경</button>

  </form>
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
    if (data) {
      editUser.value = { ...data };
    }
  },
  { immediate: true }
);

const updateInfo = () => {
  store.updateMyPage(editUser.value)
    .then(() => {
      alert("수정 완료!");
      emit("cancel"); // 수정 화면 종료
    });
};
</script>

<style scoped>

</style>