<template>
  <div class="pagination">
    <button
      class="page-btn"
      @click="$emit('prev-group')"
      :disabled="isFirstGroup"
      aria-label="이전 페이지 그룹"
    >
      ‹
    </button>

    <button
      v-for="page in pageNumbers"
      :key="page"
      class="page-btn"
      :class="{ active: page === currentPage }"
      @click="$emit('change-page', page)"
      :aria-current="page === currentPage ? 'page' : null"
    >
      {{ page + 1 }}
    </button>

    <button
      class="page-btn"
      @click="$emit('next-group')"
      :disabled="isLastGroup"
      aria-label="다음 페이지 그룹"
    >
      ›
    </button>
  </div>
</template>

<script setup>
defineProps({
  pageNumbers: Array,
  currentPage: Number,
  isFirstGroup: Boolean,
  isLastGroup: Boolean
});

defineEmits(["change-page", "prev-group", "next-group"]);
</script>

<style scoped>
/* ✅ deposit 페이지네이션과 동일 */
.pagination {
  margin-top: 12px;
  display: flex;
  justify-content: center;
  gap: 6px;
  flex-wrap: wrap;
}

.page-btn {
  min-width: 34px;
  height: 34px;
  padding: 0 10px;
  border-radius: 999px;      /* pill */
  border: 1px solid #d7def0; /* 동일 테두리 */
  background: #ededed;
  color: #0063f8;
  font-weight: 700;
  cursor: pointer;
}

.page-btn.active {
  background: #0063f8;
  border-color: #0063f8;
  color: #fff;
}

.page-btn:disabled {
  opacity: 0.45;
  cursor: not-allowed;
}

/* 모바일 */
@media (max-width: 480px) {
  .page-btn {
    min-width: 30px;
    height: 30px;
  }
}
</style>
