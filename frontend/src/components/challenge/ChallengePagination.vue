<template>
  <div class="pagination">
    <button @click="$emit('prev-group')" :disabled="isFirstGroup">
      ◀
    </button>

    <button v-for="page in pageNumbers" :key="page"
      @click="$emit('change-page', page)" :class="{ active: page === currentPage }">
      {{ page + 1 }}
    </button>

    <button @click="$emit('next-group')" :disabled="isLastGroup">
      ▶
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

defineEmits([
  'change-page',
  'prev-group',
  'next-group'
]);
</script>


<style scoped>
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 6px;
  margin: 16px 0;
}

.pagination button {
  min-width: 32px;
  height: 32px;
  border: 1px solid #ccc;
  background-color: #ffffff;
  color: #333;
  border-radius: 4px;
  font-size: 13px;
  cursor: pointer;
}

.pagination button:hover:not(:disabled) {
  background-color: #f0f0f0;
}

.pagination button.active {
  background-color: #2196f3;
  color: white;
  border-color: #2196f3;
}

.pagination button:disabled {
  opacity: 0.4;
  cursor: not-allowed;
}

</style>