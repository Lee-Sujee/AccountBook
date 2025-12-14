<template>
  <div class="home-container">
    <div class="user-greeting">
      <h2>{{ authStore.loginUserInfo?.name || '사용자' }} 님의 가계부</h2>
    </div>

    <div class="account-book-section">
      <h3 class="section-title">가계부 항목</h3>

      <button @click="bookStore.openCreateModal()" class="btn btn-primary mb-3">
        + 새 항목 등록
      </button>

      <!--목록 가져오기-->
      <BookListTable :entries="bookStore.bookEntries"
      @select-entry="bookStore.openDetailModal"></BookListTable>

      <!--등록/수정-->
      <BookFormModal
        :isVisible="bookStore.isFormModalVisible"
        :entry-to-edit="bookStore.selectEntry"
        @close-modal="bookStore.closeModal"
      />

      <!--상세 조회-->
      <BookDetailModal :isVisible="bookStore.isDetailModalVisible"
      :entry="bookStore.selectEntry"
      @close-modal="bookStore.closeModal"
      @edit-request="bookStore.openEditModal"
      @delete-request="bookStore.deleteEntry">
      </BookDetailModal>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from "vue";
import { useAuthStore } from "@/stores/auth";
import { useBookStore } from "@/stores/book";
import BookFormModal from '@/components/book/BookFormModal.vue';
import BookListTable from "@/components/book/BookListTable.vue";
import BookDetailModal from "@/components/book/BookDetailModal.vue";

const authStore = useAuthStore();
const bookStore = useBookStore();

onMounted(async () => {
  if(authStore.accessToken) await bookStore.loadBookList();
});
</script>
