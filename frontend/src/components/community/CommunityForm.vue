<template>
  <div class="form-wrapper">
    <h2 class="form-title">
      {{ initialData ? "게시글 수정" : "게시글 작성" }}
    </h2>

    <form class="post-form" @submit.prevent="submit">
      <div class="form-group">
        <label for="title">제목</label>
        <input id="title" type="text" v-model="title" placeholder="제목을 입력하세요" />
      </div>

      <div class="form-group">
        <label for="content">내용</label>
        <textarea id="content" v-model="content" placeholder="내용을 입력하세요"></textarea>
      </div>

      <div class="form-actions">
        <button type="submit" class="submit-btn" :disabled="!isValid">
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
  width: 100%;
  margin: 40px auto;
  padding: 24px;
  background-color: #EDEDED;
  box-sizing: border-box;
}

.form-title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 40px;
  text-align: left;
  color: #0063f8;
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
  font-size: 15px;
  font-weight: 700;
  margin-bottom: 6px;
  color: #0063f8;
}

.form-group input,
.form-group textarea {
  width: 100%;
  padding: 12px;
  font-size: 14px;
  border: none;
  border-top: 1.5px solid #0063f8;
  border-bottom: 1.5px solid #0063f8;
  background-color: #EDEDED;
  box-sizing: border-box;
}

.form-group input:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #0056d6;
}

.form-group textarea {
  min-height: 300px;
  resize: vertical;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

.submit-btn {
  padding: 10px 40px;
  background-color: #0063f8;
  color: #fff;
  border: none;
  border-radius: 20px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s;
}

.submit-btn:hover {
  background-color: #0052cc;
}

.submit-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* 모바일 대응 */
@media (max-width: 768px) {
  .form-wrapper {
    margin: 20px auto;
    padding: 16px;
  }

  .form-title {
    font-size: 20px;
    margin-bottom: 24px;
    text-align: center;
  }

  .form-group textarea {
    min-height: 200px;
  }

  .submit-btn {
    width: 100%;
  }
}
</style>