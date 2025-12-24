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
        <tr v-for="(post, index) in communityList" :key="post.id" class="table-row">
          <td data-label="No">{{ index + 1 }}</td>

          <td data-label="제목" class="title-cell">
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
          <td colspan="5" class="empty-row">
            작성된 게시글이 없습니다.
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { useCommunityStore } from "@/stores/community";
import { computed, onMounted } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();
const store = useCommunityStore();
const communityList = computed(() => store.communityList);

onMounted(() => {
  store.fetchCommunityList();
});
</script>

<style scoped>
.list-wrapper {
  max-width: 1000px;
  width: 100%;
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

.write-btn:hover {
  background-color: #0063f8;
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
  border-bottom: 1px solid #0063f8;
  background-color: #EDEDED;
  text-align: center;
}

.community-table tbody td {
  padding: 14px 12px;
  font-size: 14px;
  color: #374151;
  border-bottom: 2px solid #e5e7eb;
  text-align: center;
}

.community-table tbody tr:last-child td {
  border-bottom: none;
}

.table-row:hover {
  background-color: #e5e7eb;
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
  color: #777;
}

@media (max-width: 768px) {
  .list-wrapper {
    margin: 20px auto;
    padding: 16px;
  }

  .community-table thead {
    display: none;
    /* 헤더 숨김 */
  }

  .community-table,
  .community-table tbody,
  .community-table tr,
  .community-table td {
    display: block;
    width: 100%;
    box-sizing: border-box;
  }

  .community-table tr {
    margin-bottom: 16px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    padding: 16px;
    border: 1px solid #e5e7eb;
  }

  .community-table td {
    padding: 6px 0;
    border-bottom: none;
    text-align: left !important;
    font-size: 13px;
    display: flex;
    justify-content: space-between;
  }

  .community-table td.title-cell {
    font-size: 16px;
    font-weight: 700;
    margin-bottom: 8px;
    padding-top: 0;
  }

  .community-table td[data-label="No"] {
    display: none;
  }

  .community-table td[data-label="작성자"]::before,
  .community-table td[data-label="작성일"]::before {
    content: attr(data-label);
    font-weight: 600;
    color: #6b7280;
    margin-right: 8px;
  }

  .mobile-stat {
    font-size: 12px;
    color: #888;
    background-color: #f3f4f6;
    padding: 2px 8px;
    border-radius: 10px;
    display: inline-block;
    margin-top: 4px;
  }
}
</style>