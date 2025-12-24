<template>
  <div class="list-wrapper">
    <div class="list-header">
      <h2>게시판</h2>
      <button class="write-btn" @click="router.push({ name: 'CommunityWrite' })">
        새 글 작성
      </button>
    </div>

    <table class="community-table">
      <thead>
        <tr>
          <th width="10%">No</th>
          <th width="45%">제목</th>
          <th width="15%">작성자</th>
          <th width="20%">작성일</th>
          <th width="10%">조회/좋아요</th>
        </tr>
      </thead>

      <tbody>
        <tr
          v-for="(post, index) in pagedList"
          :key="post.id"
          class="table-row"
        >
          <!-- 전체 리스트 기준 번호 -->
          <td>{{ (currentPage - 1) * pageSize + index + 1 }}</td>

          <td class="title-cell">
            <router-link :to="`/community/${post.id}`" class="title-link">
              {{ post.title }}
            </router-link>
          </td>

          <td data-label="작성자">{{ post.writerName }}</td>
          <td data-label="작성일">{{ post.createdAt }}</td>
          <td data-label="반응">
            <span class="mobile-stat">조회 {{ post.views }} · 좋아요 {{ post.likes }}</span>
          </td>
        </tr>

        <tr v-if="communityList.length === 0">
          <td colspan="6" class="empty-row">작성된 게시글이 없습니다.</td>
        </tr>
      </tbody>
    </table>

    <!-- ✅ 페이지네이션 -->
    <div v-if="totalPages > 1" class="pagination">
      <button
        class="page-btn"
        :disabled="currentPage === 1"
        @click="goPage(1)"
      >
        «
      </button>

      <button
        class="page-btn"
        :disabled="currentPage === 1"
        @click="goPage(currentPage - 1)"
      >
        ‹
      </button>

      <button
        v-for="p in visiblePages"
        :key="p"
        class="page-btn"
        :class="{ active: p === currentPage }"
        @click="goPage(p)"
      >
        {{ p }}
      </button>

      <button
        class="page-btn"
        :disabled="currentPage === totalPages"
        @click="goPage(currentPage + 1)"
      >
        ›
      </button>

      <button
        class="page-btn"
        :disabled="currentPage === totalPages"
        @click="goPage(totalPages)"
      >
        »
      </button>
    </div>
  </div>
</template>

<script setup>
import { useCommunityStore } from "@/stores/community";
import { computed, onMounted, ref, watch } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();
const store = useCommunityStore();

const communityList = computed(() => store.communityList ?? []);

const pageSize = 10;      // ✅ 한 페이지에 보여줄 게시글 개수 (원하면 바꿔)
const pageGroupSize = 5;  // ✅ 페이지 버튼은 항상 5개씩만

const currentPage = ref(1);

const totalPages = computed(() => {
  return Math.max(1, Math.ceil(communityList.value.length / pageSize));
});

const pagedList = computed(() => {
  const start = (currentPage.value - 1) * pageSize;
  return communityList.value.slice(start, start + pageSize);
});

// 1~5, 6~10 처럼 5개 묶어서 보여주기
const visiblePages = computed(() => {
  const groupIndex = Math.floor((currentPage.value - 1) / pageGroupSize);
  const start = groupIndex * pageGroupSize + 1;
  const end = Math.min(start + pageGroupSize - 1, totalPages.value);

  const pages = [];
  for (let p = start; p <= end; p++) pages.push(p);
  return pages;
});

const goPage = (p) => {
  if (p < 1 || p > totalPages.value) return;
  currentPage.value = p;
  window.scrollTo({ top: 0, behavior: "smooth" }); // 페이지 바꿀 때 상단으로
};

onMounted(async () => {
  await store.fetchCommunityList();
});

// 리스트 길이가 바뀌면 현재 페이지가 범위 밖으로 튀지 않게 보정
watch(
  () => communityList.value.length,
  () => {
    if (currentPage.value > totalPages.value) currentPage.value = totalPages.value;
    if (communityList.value.length === 0) currentPage.value = 1;
  }
);
</script>

<style scoped>
.list-wrapper {
  max-width: 1100px;
  margin: 40px auto;
  padding: 24px;
  background: #EDEDED;
  border-radius: 12px;
  box-sizing: border-box;
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  background-color: #EDEDED;
}

.list-header h2 {
  font-size: 22px;
  font-weight: 700;
  color: #0063f8;
  margin: 0;
}

.write-btn {
  padding: 8px 30px;
  background-color: #0063f8;
  color: #fff;
  border: none;
  border-radius: 20px;
  font-size: 13px;
  cursor: pointer;
  white-space: nowrap;
}

.community-table {
  width: 100%;
  border-collapse: separate;
  border-spacing: 0;
  background-color: #EDEDED;
  border-radius: 12px;
  overflow: hidden;
}

.community-table thead th {
  padding: 14px 12px;
  font-size: 13px;
  font-weight: 600;
  color: #0063f8;
  border-bottom: 2px solid #0063f8;
  background-color: #EDEDED;
  text-align: center;
}

.community-table tbody td {
  padding: 14px 12px;
  font-size: 14px;
  color: #374151;
  border-bottom: 1px solid #e1e1e1;
  text-align: center;
}

.table-row:hover {
  background-color: #e1e1e1;
}

.title-cell {
  text-align: left !important;
}

.title-link {
  color: #111827;
  font-weight: 500;
  text-decoration: none;
  display: block;
  /* 링크 영역 확장 */
}

.title-link:hover {
  color: #0063f8;
  text-decoration: underline;
}

.empty-row {
  padding: 32px;
  text-align: center;
  color: #666;
}

/* ✅ 페이지네이션 */
.pagination {
  margin-top: 18px;
  display: flex;
  justify-content: center;
  gap: 6px;
  flex-wrap: wrap;
}

.page-btn {
  min-width: 34px;
  height: 34px;
  padding: 0 10px;
  border-radius: 10px;
  border: 1px solid #cbd7ff;
  background: #fff;
  color: #0063f8;
  font-weight: 700;
  cursor: pointer;
}

.page-btn.active {
  background: #0063f8;
  color: #fff;
  border-color: #0063f8;
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>
