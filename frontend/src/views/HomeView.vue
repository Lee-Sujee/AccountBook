<template>
  <div class="home-container">
    <!-- 상단 인사 + 토글 버튼 -->
    <div class="user-greeting">
      <h2>{{ authStore.loginUserInfo?.name || '사용자' }} 님의 가계부</h2>

      <button class="btn btn-secondary" @click="toggleView">
        {{ showStats ? '가계부 보기' : '통계 보기' }}
      </button>
    </div>

    <!-- 통계 화면 -->
    <section v-show="showStats" class="stats-section">
      <SummaryCards />
      <ExpenseDonutChart :visible="showStats" />
    </section>

    <!-- 가계부 화면 -->
    <section v-show="!showStats" class="account-book-section">
      <h3 class="section-title">가계부 항목</h3>

      <button @click="bookStore.openCreateModal()" class="btn btn-primary mb-3">
        + 새 항목 등록
      </button>

      <BookListTable
        :entries="bookStore.bookEntries"
        @select-entry="bookStore.openDetailModal"
      />

      <BookFormModal
        :isVisible="bookStore.isFormModalVisible"
        :entry-to-edit="bookStore.selectEntry"
        @close-modal="bookStore.closeModal"
      />

      <BookDetailModal
        :isVisible="bookStore.isDetailModalVisible"
        :entry="bookStore.selectEntry"
        @close-modal="bookStore.closeModal"
        @edit-request="bookStore.openEditModal"
        @delete-request="bookStore.deleteEntry"
      />
    </section>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useBookStore } from '@/stores/book'

import SummaryCards from '@/components/summary/SummaryCards.vue'
import ExpenseDonutChart from '@/components/summary/ExpenseDonutChart.vue'

import BookFormModal from '@/components/book/BookFormModal.vue'
import BookListTable from '@/components/book/BookListTable.vue'
import BookDetailModal from '@/components/book/BookDetailModal.vue'

const authStore = useAuthStore()
const bookStore = useBookStore()

// 새로고침해도 유지
const showStats = ref(localStorage.getItem('showStats') === 'true')

watch(showStats, v => {
  localStorage.setItem('showStats', String(v))
})

const toggleView = () => {
  showStats.value = !showStats.value
}

onMounted(async () => {
  if (authStore.accessToken) {
    await bookStore.loadBookList()
  }
})
</script>

<style scoped>
.home-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
}

.user-greeting {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.stats-section {
  display: flex;
  flex-direction: column;
  gap: 32px;
}

.account-book-section {
  margin-top: 24px;
}

.section-title {
  margin-bottom: 16px;
}
</style>
