<template>
  <div v-if="communityDetail" class="detail-wrapper">
    <article class="post-card">
      <h1 class="post-title">{{ communityDetail.title }}</h1>
      
      <div class="post-meta">
        <div class="write-info">
          <span>{{ communityDetail.writerName }}</span>
          <span>{{ communityDetail.createdAt }}</span>
        </div>
        <div class="post-stats">
          <span>조회 {{ communityDetail.views }}</span>
          <button class="like-btn" :class="{ active: liked }" @click="$emit('toggle-like')" > 💕 {{ communityDetail.likes }} </button>
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

    <CommentList :boardId="communityDetail.id"/>
  </div>
</template>


<script setup>
import { watch, computed } from "vue";
import { useRoute } from "vue-router";
import { storeToRefs } from "pinia";
import { useCommunityStore } from "@/stores/community";
import { useAuthStore } from "@/stores/auth";
import router from "@/router";
import CommentList from "../comment/CommentList.vue";
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
  padding: 24px;
  background-color: #EDEDED;
}

.post-title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 12px;
}

.post-meta{
  display: flex;
  justify-content: space-between;
  width: 100%;
  font-size: 14px;
  padding: 10px;
  margin-bottom: 24px;
  border: none;
  border-top: 1.5px solid #0063f8;
  border-bottom: 1.5px solid #0063f8;
}

.write-info {
  display: inline-flex;
  flex-direction: column; /* 세로로 배치 */
}

.post-stats {
  margin-left: auto;
  display: flex;
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
}

.post-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 24px;
}

.post-actions button {
  padding: 8px 14px;
  font-size: 14px;
  cursor: pointer;
  background-color: #ededed;
  border: 1.5px solid #0063f8 !important;
  border-radius: 20px;
}

.like-btn {
  width: 50px;
}



</style>