<template>
  <div v-if="communityDetail" class="detail-wrapper">
    <article class="post-card">
      <h1 class="post-title">{{ communityDetail.title }}</h1>

      <div class="post-meta">
        <div class="write-info">
          <span class="writer-name">{{ communityDetail.writerName }}</span>
          <span class="write-date">{{ communityDetail.createdAt }}</span>
        </div>
        <div class="post-stats">
          <span>조회 {{ communityDetail.views }}</span>
          <button class="like-btn" :class="{ active: liked }" @click="$emit('toggle-like')">
            💕 {{ communityDetail.likes }}
          </button>
        </div>
      </div>

      <div class="post-content">
        {{ communityDetail.content }}
      </div>
    </article>

    <div v-if="isAuthor" class="post-actions">
      <button class="edit-btn" @click="goEdit">수정</button>
      <button class="delete-btn" @click="deletePost">삭제</button>
    </div>

    <CommentList :boardId="communityDetail.id" />
  </div>
</template>

<script setup>
// 기존 스크립트 로직 유지
import { watch, computed } from "vue";
import { useRoute } from "vue-router";
import { storeToRefs } from "pinia";
import { useCommunityStore } from "@/stores/community";
import { useAuthStore } from "@/stores/auth";
import router from "@/router";
import CommentList from "../comment/CommentList.vue";

defineProps({
  liked: { type: Boolean, required: true },
  likes: { type: Number, required: true },
});
defineEmits(["toggle-like"]);

const route = useRoute();
const store = useCommunityStore();
const authStore = useAuthStore();

const { loginUserInfo } = storeToRefs(authStore);
const { communityDetail } = storeToRefs(store);

// 로그인 사용자 id
const loginUserId = computed(() => loginUserInfo.value?.id);

// 작성자 여부 확인
const isAuthor = computed(() => {
  return (
    loginUserId.value !== undefined &&
    communityDetail.value?.writerId === loginUserId.value
  );
});

// 수정 페이지 이동
const goEdit = () => {
  router.push({
    name: "CommunityEditView",
    params: { id: communityDetail.value.id },
  });
};

// 삭제!
const deletePost = async () => {
  if (!confirm("정말 삭제하시겠습니까?")) return;
  await store.deleteCommunity(communityDetail.value.id);
  router.push({ name: "CommunityList" });
};

watch(
  () => route.params.id,
  (id) => {
    if (id) store.fetchCommunityDetail(Number(id));
  },
  { immediate: true }
);
</script>

<style scoped>
.detail-wrapper,
.form-wrapper {
  max-width: 1000px;
  width: 100%;
  margin: 40px auto;
  padding: 0 16px;
  box-sizing: border-box;
}

.post-card {
  padding: 24px;
  background-color: #EDEDED;
  border-radius: 8px;
}

.post-title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 12px;
  word-break: break-all;
}

.post-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  font-size: 14px;
  padding: 10px 0;
  margin-bottom: 24px;
  border-top: 1.5px solid #0063f8;
  border-bottom: 1.5px solid #0063f8;
  flex-wrap: wrap;
  gap: 10px;
}

.write-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.writer-name {
  font-weight: 700;
  color: #333;
}

.write-date {
  font-size: 12px;
  color: #666;
}

.post-stats {
  margin-left: auto;
  display: flex;
  align-items: center;
  gap: 12px;
}

.post-content {
  font-size: 16px;
  line-height: 1.6;
  white-space: pre-line;
  color: #333;
  min-height: 150px;
}

.post-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 24px;
}

.post-actions button {
  padding: 8px 18px;
  font-size: 14px;
  cursor: pointer;
  background-color: #ededed;
  border: 1.5px solid #0063f8 !important;
  border-radius: 20px;
  color: #0063f8;
  font-weight: 600;
  transition: all 0.2s;
}

.post-actions button:hover {
  background-color: #0063f8;
  color: #fff;
}

.like-btn {
  padding: 4px 12px;
  border: 1px solid #ddd;
  border-radius: 20px;
  background: white;
  cursor: pointer;
  font-size: 13px;
}

.like-btn:hover {
  background-color: #f9f9f9;
}

@media (max-width: 480px) {
  .post-card {
    padding: 16px;
  }

  .post-title {
    font-size: 20px;
  }

  .post-meta {
    flex-direction: column;
    align-items: flex-start;
    /* 왼쪽 정렬 */
    gap: 12px;
  }

  .post-stats {
    margin-left: 0;
    /* 왼쪽으로 붙임 */
    width: 100%;
    justify-content: space-between;
    /* 양끝 정렬 */
  }
}
</style>