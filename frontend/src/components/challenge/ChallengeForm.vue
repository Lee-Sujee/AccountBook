<template>
  <div class="form-wrapper">
    <h2 class="form-title">챌린지 등록</h2>

    <form class="post-form" @submit.prevent="submit">
      <div class="form-grid">
        <div class="form-group">
          <label for="startDate">시작일</label>
          <input id="startDate" type="date" v-model="startDate" />
        </div>

        <div class="form-group">
          <label for="endDate">종료일</label>
          <input id="endDate" type="date" v-model="endDate" />
        </div>

        <div class="form-group full">
          <label for="target">목표금액</label>
          <input id="target" type="number" v-model="target" placeholder="예) 100000" />
        </div>

        <div class="form-group full">
          <label for="description">목적</label>
          <input id="description" type="text" v-model="description" placeholder="예) 여행 자금 모으기" />
        </div>
      </div>

      <div class="form-actions">
        <button type="submit" class="submit-btn" :disabled="!isValid">등록</button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import { defineEmits } from "vue";

const emit = defineEmits(["submit"]);

const props = defineProps({
  initialData: { type: Object, default: null },
});

const startDate = ref(props.initialData?.startDate ?? "");
const endDate = ref(props.initialData?.endDate ?? "");
const target = ref(props.initialData?.target ?? "");
const description = ref(props.initialData?.description ?? "");

const isValid = computed(() => {
  return (
    description.value.trim() !== "" &&
    startDate.value !== "" &&
    endDate.value !== "" &&
    Number(target.value) > 0
  );
});

const submit = async () => {
  if (!isValid.value) {
    alert("입력값을 확인해주세요.");
    return;
  }
  emit("submit", {
    startDate: startDate.value,
    endDate: endDate.value,
    target: Number(target.value),
    description: description.value,
  });
};
</script>

<style scoped>
/* 전체 컨테이너(다른 페이지들과 톤 맞춤) */
.form-wrapper {
  max-width: 550px;
  margin: 40px auto;
  padding: 0 16px;
  box-sizing: border-box;
}

.form-title {
  font-size: 22px;
  font-weight: 800;
  color: #0063f8;
  margin-bottom: 14px;
}

/* 카드 */
.post-form {
  background: #ededed;
  border-radius: 12px;
  padding: 22px;
  box-sizing: border-box;
}

/* 입력 영역을 그리드로 */
.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 14px 16px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.form-group.full {
  grid-column: 1 / -1;
}

.form-group label {
  font-size: 13px;
  font-weight: 700;
  color: #0063f8;
}

/* 인풋: 밑줄 포인트 스타일 */
.form-group input {
  width: 100%;
  padding: 10px 10px;
  background: #ededed;
  border: none;
  border-bottom: 2px solid #0063f8;
  border-radius: 0;
  font-size: 14px;
  color: #111827;
  box-sizing: border-box;
}

.form-group input::placeholder {
  color: #9aa3b2;
}

.form-group input:focus {
  outline: none;
  border-bottom-color: #004fc7;
}

/* 버튼 영역 */
.form-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 18px;
}

.submit-btn {
  min-width: 120px;
  padding: 10px 18px;
  background-color: #0063f8;
  color: #fff;
  border: 1.5px solid #0063f8;
  border-radius: 999px; /* pill */
  font-size: 14px;
  font-weight: 800;
  cursor: pointer;
}

.submit-btn:disabled {
  background-color: #81b1f8;
  border-color: #81b1f8;
  cursor: not-allowed;
}

/* 반응형 */
@media (max-width: 768px) {
  .form-grid {
    grid-template-columns: 1fr;
  }

  .form-actions {
    justify-content: flex-end;
  }

  .submit-btn {
    width: 100%;
  }
}
</style>
