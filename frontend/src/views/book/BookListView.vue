<template>
    <div class="home-wrapper">
        <div class="home-container">
            <div class="user-header">
                <h2 class="user-greeting">
                    <span class="user-name">{{ authStore.loginUserInfo?.name || '사용자' }}님</span>의 가계부
                </h2>
                <div class="header-buttons">
                    <button @click="bookStore.openCreateModal()" class="btn-add-entry">
                        새 항목 등록
                    </button>
                    <RouterLink :to="{ name: 'BookSummary' }" class="btn-summary">
                        통계 보기
                    </RouterLink>
                </div>
            </div>

            <section class="account-book-section">
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
import { ref, onMounted } from 'vue'
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

const showStats = ref(localStorage.getItem("showStats") === "true")
</script>

<style scoped>
/* 전체 배경 설정 */
.home-wrapper {
    background-color: #ededed;
    min-height: 100vh;
    padding: 60px 20px;
    display: flex;
    justify-content: center;
}

.home-container {
    max-width: 1000px;
    width: 100%;
    margin: 0 auto;
}

/* 상단 헤더 */
.user-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 30px;
    flex-wrap: wrap;
    gap: 16px;
}

.user-greeting {
    font-size: 22px;
    font-weight: 700;
    color: #0063f8;
    margin: 0;
}

.header-buttons {
    display: flex;
    gap: 12px;
}

.user-name {
    color: #0063f8;
}

/* 새 항목 등록 버튼 */
.btn-add-entry {
    background-color: #0063f8;
    color: #ffffff;
    border: none;
    padding: 10px 20px;
    border-radius: 25px;
    font-weight: 600;
    cursor: pointer;
    transition: opacity 0.2s;
    white-space: nowrap;
}

.btn-add-entry:hover {
    opacity: 0.9;
}


/* 통계 보기 버튼 */
.btn-summary {
    background-color: #0063f8;
    color: #ffffff;
    border: none;
    padding: 10px 20px;
    border-radius: 25px;
    font-weight: 500;
    font-size: 14px;
    cursor: pointer;
    text-decoration: none;
    display: inline-flex;
    align-items: center;
    transition: opacity 0.2s;
    white-space: nowrap;
}

.btn-summary:hover {
    opacity: 0.9;
}

/* 반응형 미디어 쿼리 */
@media (max-width: 768px) {
    .home-wrapper {
        padding: 30px 16px;
    }

    .user-greeting {
        font-size: 18px;
    }

    .btn-add-entry,
    .btn-summary {
        padding: 8px 16px;
        font-size: 13px;
    }
}
</style>