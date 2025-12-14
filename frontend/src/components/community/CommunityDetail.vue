<template>
  <div v-if="communityDetail" class="detail-wrapper">
    <article class="post-card">
      <h1 class="post-title">{{ communityDetail.title }}</h1>

      <div class="post-meta">
        <span>작성자 · {{ communityDetail.writerName }}</span>
        <span>작성일 · {{ communityDetail.createdAt }}</span>
      </div>

      <div class="post-content">
        {{ communityDetail.content }}
      </div>
    </article>

    <div class="post-stats">
      <span>👀 조회수 {{ communityDetail.views }}</span>
      <button class="like-btn" :class="{ active: liked }" @click="$emit('toggle-like')" > ❤️ {{ communityDetail.likes }} </button>
    </div>

    <div v-if="isAuthor" class="post-actions">
      <button class="edit-btn" @click="goEdit">수정</button>
      <button class="delete-btn" @click="deletePost">삭제</button>
    </div>
  </div>
</template>


<script setup>
import { watch, computed } from "vue";
import { useRoute } from "vue-router";
import { storeToRefs } from "pinia";
import { useCommunityStore } from "@/stores/community";
import { useAuthStore } from "@/stores/auth";
import router from "@/router";
defineProps({
  liked: {
    type: Boolean,
    required: true,
  },
  likes: {
    type: Number,
    required: true,
  },
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
    params: {
      id: communityDetail.value.id,
    },
  });
};

// 삭제!
const deletePost = async () => {
  if (!confirm("정말 삭제하시겠습니까?")) return;

  await store.deleteCommunity(communityDetail.value.id);

  router.push({ name: "CommunityList" });
};

// 좋아요 토글


watch(
  () => route.params.id,
  (id) => {
    if (id) {
      store.fetchCommunityDetail(Number(id));
    }
  },
  { immediate: true }
);

</script>

<style scoped>
.detail-wrapper {
  max-width: 800px;
  margin: 40px auto;
  padding: 0 16px;
}

.post-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 24px;
  background-color: #fff;
}

.post-title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 12px;
}

.post-meta {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  color: #666;
  margin-bottom: 24px;
}

.post-content {
  font-size: 16px;
  line-height: 1.6;
  white-space: pre-line; /* 줄바꿈 유지 */
  color: #333;
}

.post-stats {
  display: flex;
  gap: 16px;
  margin-top: 16px;
  font-size: 14px;
  color: #555;
}

.post-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 24px;
}

.post-actions button {
  padding: 8px 14px;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  border: none;
}

.edit-btn {
  background-color: #4f7cff;
  color: white;
}

.edit-btn:hover {
  background-color: #3f68d8;
}

.delete-btn {
  background-color: #e74c3c;
  color: white;
}

.delete-btn:hover {
  background-color: #c0392b;
}


</style>