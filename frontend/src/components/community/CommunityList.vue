<template>
  <div class="list-wrapper">
    <div class="list-header">
      <h2>커뮤니티</h2>
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
  max-width: 1000px;
  margin: 40px auto;
  padding: 0 16px;
}

.list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.list-header h2 {
  font-size: 24px;
  font-weight: 600;
}

.write-btn {
  padding: 8px 14px;
  background-color: #4f7cff;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
}

.write-btn:hover {
  background-color: #3f68d8;
}

.community-table {
  width: 100%;
  border-collapse: collapse;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
}

.community-table thead {
  background-color: #f5f6f8;
}

.community-table th,
.community-table td {
  padding: 12px;
  text-align: center;
  font-size: 14px;
}

.community-table th {
  font-weight: 600;
  color: #444;
}

.table-row:hover {
  background-color: #f9fafc;
}

.title-cell {
  text-align: left;
}

.title-link {
  color: #333;
  text-decoration: none;
  font-weight: 500;
}

.title-link:hover {
  text-decoration: underline;
  color: #4f7cff;
}

.empty-row {
  padding: 24px;
  color: #777;
  text-align: center;
}
   
</style>