<template>
    <div>
        <div class="home-container">
            <div class="user-greeting">
                <h3>{{ authStore.loginUserInfo?.name || '사용자' }} 님의 가계부</h3>
                <router-link :to="{name: 'BookSummary'}" class="link">분석결과</router-link>
            </div>

            <!-- 가계부 화면 -->
            <section v-show="!showStats" class="account-book-section">
                <h3 class="section-title">가계부 항목</h3>

                <button @click="bookStore.openCreateModal()" class="btn btn-primary mb-3">
                    + 새 항목 등록
                </button>

                <BookList :entries="bookStore.bookEntries" @select-entry="bookStore.openDetailModal" />

                <BookFormModal :isVisible="bookStore.isFormModalVisible" :entry-to-edit="bookStore.selectEntry"
                    @close-modal="bookStore.closeModal" />

                <BookDetailModal :isVisible="bookStore.isDetailModalVisible" :entry="bookStore.selectEntry"
                    @close-modal="bookStore.closeModal" @edit-request="bookStore.openEditModal"
                    @delete-request="bookStore.deleteEntry" />
            </section>
        </div>
    </div>
</template>

<script setup>
import BookList from '@/components/book/BookListTable.vue';
import { ref, watch, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useBookStore } from '@/stores/book'

import BookFormModal from '@/components/book/BookFormModal.vue'
import BookDetailModal from '@/components/book/BookDetailModal.vue'

const authStore = useAuthStore()
const bookStore = useBookStore()

onMounted(async () => {
  if (authStore.accessToken) {
    await bookStore.loadBookList()
  }
})
</script>

<style scoped>
.link{
    text-decoration: none;
    margin-right: 10px;
}
</style>