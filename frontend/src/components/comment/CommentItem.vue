<template>
  <article class="comment-card">
    <div class="comment-content">
      <span class="writer">{{ comment.writerName }}</span>
      {{ comment.content }}
    </div>

    <div v-if="isAuthor" class="comment-actions">
      <button class="update-button" @click="onEdit">수정</button>
      <button class="delete-button" @click="onDelete">삭제</button>
    </div>
  </article>
</template>

<script setup>
import { defineProps, defineEmits, computed } from 'vue';
import { useAuthStore } from '@/stores/auth';
const props = defineProps({
  comment: Object
});

const emit = defineEmits(['edit-comment', 'delete-comment']);

const authStore = useAuthStore();

const loginUserId = computed(() => authStore.loginUserInfo?.id ?? null);

const isAuthor = computed(() => {
  return (
    loginUserId.value !== null &&
    props.comment.writerId === loginUserId.value
  );
});

const onEdit = () => {
  emit('edit-comment', props.comment);
};

const onDelete = () => {
    emit('delete-comment', props.comment);
}
</script>
<style scoped>
.comment-card {
  padding: 14px 16px;
  border-bottom: 1px solid #e1e1e1;
  display: flex;
  flex-direction: column;
  gap: 6px;
  background-color: #EDEDED;
}

.comment-content {
  font-size: 14px;
  color: #111827;
  line-height: 1.6;
  word-break: break-word;
}

.comment-content .writer {
  display: block;
  margin-top: 4px;
  margin-bottom: 8px;
  font-size: 12px;
  color: #6b7280;
}

.comment-actions {
  display: flex;
  justify-content: flex-end;
  gap: 6px;
  margin-top: 4px;
}

.comment-actions button {
  padding: 4px 10px;
  font-size: 12px;
  border-radius: 4px;
  border: none;
  cursor: pointer;
  transition: background-color 0.15s ease;
}

.update-button {
  background-color: #EDEDED;
  color: #374151;
}

.update-button:hover {
  background-color: #d1d5db;
}

.delete-button {
  background-color: #EDEDED;
  color: #374151;
}

.delete-button:hover {
  background-color: #fecaca;
}

</style>