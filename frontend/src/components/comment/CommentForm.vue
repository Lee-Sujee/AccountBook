<template>
    <div class="form-wrapper">
        <form class="comment-form" @submit.prevent="submit">
            <div class="form-group">
                <label for="content">댓글</label>
                <textarea type="text" id="content" v-model="content" placeholder="댓글을 입력하세요"></textarea>
            </div>
            <div class="form-actions">
                <button v-if="isEdit" type="submit" class="submit-btn" :disabled="!isValid">수정</button>
                <button v-else type="submit" class="submit-btn" :disabled="!isValid">등록</button>
            </div>
        </form>

    </div>
</template>

<script setup>
import { defineProps, computed, ref, watch } from 'vue';
import { useCommunityStore } from '@/stores/community';
const communityStore = useCommunityStore();
const props = defineProps({
    initialData:{
        type: Object,
        default: null,
    },
    boardId : Number,
});

const content = ref("");

// 댓글 내용 있는지 확인
const isValid = computed(() => {
    return content.value.trim() !== "";
})

const isEdit = computed(() => {
    return props.initialData !== null;
})

// 댓글 등록
const submit = (() => {
    if(!isValid.value) return null;
    if(!isEdit.value) communityStore.createComment(props.boardId, content.value);
    else {
        communityStore.updateComment(props.boardId, props.initialData.id, content.value);
    }
    content.value = "";
})

watch(
  () => props.initialData, // 감시대상
  (newVal) => {
    if (newVal) {
      content.value = newVal.content;
    } else {
      content.value = "";
    }
  }
);
</script>

<style scoped>
.form-wrapper {
  margin-top: 16px;
  padding: 12px;
  border-radius: 8px;
  background-color: #EDEDED;
}

.comment-form {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.form-group label {
  font-size: 14px;
  font-weight: 600;
  color: #374151;
}

.form-group textarea {
  resize: vertical;
  min-height: 80px;
  padding: 8px 10px;
  font-size: 14px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  outline: none;
}

.form-group textarea:focus {
  border-color: #2563eb;
  box-shadow: 0 0 0 1px rgba(37, 99, 235, 0.3);
}

.form-actions {
  display: flex;
  justify-content: flex-end;
}

.submit-btn {
  padding: 6px 16px;
  font-size: 14px;
  font-weight: 600;
  color: white;
  background-color: #2563eb;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.submit-btn:hover:not(:disabled) {
  background-color: #1e40af;
}

.submit-btn:disabled {
  background-color: #9ca3af;
  cursor: not-allowed;
}

</style>