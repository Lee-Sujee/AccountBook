<template>
  <div class="list-wrapper">
    <div class="list-header">
      <h2>COMMUNITY</h2>
      <button class="write-btn" @click="router.push({ name: 'CommunityWrite' })">
        새 글 작성
      </button>
    </div>

    <table class="community-table">
      <thead>
        <tr>
          <th>No</th>
          <th>제목</th>
          <th>작성자</th>
          <th>작성일</th>
          <th>조회수</th>
          <th>좋아요</th>
        </tr>
      </thead>

      <tbody>
        <tr
          v-for="post in communityList"
          :key="post.id"
          class="table-row"
        >
          <td>{{ post.id }}</td>

          <td class="title-cell">
            <router-link
              :to="`/community/${post.id}`"
              class="title-link"
            >
              {{ post.title }}
            </router-link>
          </td>

          <td>{{ post.writerName }}</td>
          <td>{{ post.createdAt }}</td>
          <td>{{ post.views }}</td>
          <td>{{ post.likes }}</td>
        </tr>

        <tr v-if="communityList.length === 0">
          <td colspan="6" class="empty-row">
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
  max-width: 1100px;
  margin: 40px auto;
  padding: 24px;
  background: #EDEDED;
  border-radius: 12px;
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
}

.write-btn {
  padding: 8px 30px;
  background-color: #0063f8;
  color: #fff;
  border: none;
  border-radius: 20px;
  font-size: 13px;
  cursor: pointer;
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
  text-align: left;
}

.title-link {
  color: #111827;
  font-weight: 500;
  text-decoration: none;
}

.title-link:hover {
  color: #0063f8;
  text-decoration: underline;
}

.empty-row {
  padding: 32px;
  text-align: center;
  color: #EDEDED;
}

   
</style>