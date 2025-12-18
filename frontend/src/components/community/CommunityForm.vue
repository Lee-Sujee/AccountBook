<template>
  <div class="form-wrapper">
    <h2 class="form-title">
      {{ initialData ? "게시글 수정" : "게시글 작성" }}
    </h2>

    <form class="post-form" @submit.prevent="submit">
      <div class="form-group">
        <label for="title">제목</label>
        <input
          id="title"
          type="text"
          v-model="title"
          placeholder="제목을 입력하세요"
        />
      </div>

      <div class="form-group">
        <label for="content">내용</label>
        <textarea
          id="content"
          v-model="content"
          placeholder="내용을 입력하세요"
        ></textarea>
      </div>

      <div class="form-actions">
        <button
          type="submit"
          class="submit-btn"
          :disabled="!isValid"
        >
          저장
        </button>
      </div>
    </form>
  </div>
</template>


<script setup>
import { ref, computed, watch } from "vue";

const emit = defineEmits(["submit"]);

const props = defineProps({
  initialData: {
    type: Object,
    default: null,
  },
});

const title = ref("");
const content = ref("");

watch(
  () => props.initialData,
  (val) => {
    if (val) {
      title.value = val.title ?? "";
      content.value = val.content ?? "";
    }
  },
  { immediate: true }
);

const isValid = computed(() => {
  return title.value.trim() !== "" && content.value.trim() !== "";
});

const submit = () => {
  if (!isValid.value) {
    alert("제목과 내용을 모두 입력해주세요.");
    return;
  }
  emit("submit", {
    title: title.value,
    content: content.value,
  });
};
</script>

<style scoped>
.form-wrapper {
  max-width: 800px;
  margin: 40px auto;
  padding: 24px;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.form-title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 24px;
  text-align: center;
}

.post-form {
  display: flex;
  flex-direction: column;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 6px;
  color: #333;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 10px 12px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #4f7cff;
}

.form-group textarea {
  min-height: 160px;
  resize: vertical;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

.submit-btn {
  padding: 10px 18px;
  background-color: #4f7cff;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
}

.submit-btn:hover {
  background-color: #3f68d8;
}

.submit-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

</style>
