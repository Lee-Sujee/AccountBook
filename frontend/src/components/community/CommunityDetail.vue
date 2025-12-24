<template>
  <div v-if="communityDetail" class="detail-wrapper">
    <article class="post-card">
      <h1 class="post-title">{{ communityDetail.title }}</h1>

      <div class="post-meta">
        <div class="write-info">
          <span class="writer">{{ communityDetail.writerName }}</span>
          <span class="date">{{ communityDetail.createdAt }}</span>
        </div>

        <!-- ✅ 등록일(두번째 줄)과 같은 라인에 조회/좋아요 배치 -->
        <div class="post-stats">
          <span class="stat">
            <span class="icon">👁️</span>
            <span class="num">{{ communityDetail.views }}</span>
          </span>

          <button
            type="button"
            class="like-btn"
            :class="{ active: liked }"
            @click="$emit('toggle-like')"
          >
            <span class="icon">♥</span>
            <span class="num">{{ communityDetail.likes }}</span>
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

const loginUserId = computed(() => loginUserInfo.value?.id);

const isAuthor = computed(() => {
  return (
    loginUserId.value !== undefined &&
    communityDetail.value?.writerId === loginUserId.value
  );
});

const goEdit = () => {
  router.push({
    name: "CommunityEditView",
    params: { id: communityDetail.value.id },
  });
};

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
.detail-wrapper {
  max-width: 1100px;
  width: 100%;
  margin: 40px auto;
  padding: 0 16px;
  box-sizing: border-box;
}

.post-card {
  background-color: #ededed;
  border-radius: 8px;
  box-sizing: border-box;
}

.post-title {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 12px;
  word-break: keep-all;
}

/* ✅ 2줄 그리드로 만들어서 조회/좋아요를 등록일 라인으로 내림 */
.post-meta {
  display: grid;
  grid-template-columns: 1fr auto;
  grid-template-rows: auto auto;
  column-gap: 12px;
  row-gap: 6px;
  width: 100%;
  font-size: 14px;
  padding: 10px 0;
  margin-bottom: 24px;
  border-top: 2px solid #0063f8;
  border-bottom: 2px solid #0063f8;
  box-sizing: border-box;
}

.write-info {
  grid-column: 1;
  grid-row: 1 / span 2;
  display: flex;
  flex-direction: column;
  gap: 4px;
  min-width: 0;
}

.writer {
  font-weight: 700;
  color: #111827;
}

.date {
  font-size: 12px;
  color: #374151;
}

.post-stats {
  grid-column: 2;
  grid-row: 2; /* ✅ 등록일 줄과 같은 라인 */
  display: inline-flex;
  align-items: center;
  gap: 10px;
  justify-content: flex-end;
  flex-wrap: wrap;
}

/* 조회수 pill */
.stat {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 6px 10px;
  border: 1px solid #cbd7ff;
  border-radius: 999px;
  background: #fff;
  color: #0063f8;
  font-weight: 700;
  line-height: 1;
}

.icon {
  font-size: 13px;
  line-height: 1;
}

.num {
  font-size: 13px;
  line-height: 1;
}

/* 좋아요 pill 버튼 */
.like-btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 6px 10px;
  border-radius: 999px;
  border: 1px solid #cbd7ff;
  background: #fff;
  color: #0063f8;
  font-weight: 800;
  cursor: pointer;
  line-height: 1;
}

.like-btn .icon {
  color: #ff4d6d;
}

.like-btn.active {
  background: #0063f8;
  border-color: #0063f8;
  color: #fff;
}

.like-btn.active .icon {
  color: #fff;
}

.post-content {
  font-size: 16px;
  line-height: 1.6;
  white-space: pre-line;
  color: #333;
  word-break: break-word;
}

.post-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 16px;
  padding: 0 16px;
  box-sizing: border-box;
}

.post-actions button {
  padding: 8px 14px;
  font-size: 14px;
  cursor: pointer;
  background-color: #ededed;
  border: 1.5px solid #0063f8 !important;
  border-radius: 20px;
  white-space: nowrap;
}

/* ✅ 태블릿 이하 */
@media (max-width: 768px) {
  .detail-wrapper {
    margin: 24px auto;
    padding: 0 12px;
  }

  .post-title {
    font-size: 20px;
  }

  .post-meta {
    grid-template-columns: 1fr;
    grid-template-rows: auto auto auto;
  }

  .write-info {
    grid-column: 1;
    grid-row: 1;
  }

  .post-stats {
    grid-column: 1;
    grid-row: 2;
    justify-content: flex-start;
  }

  .post-actions {
    padding: 0 12px;
  }
}

/* ✅ 모바일 */
@media (max-width: 480px) {
  .post-title {
    font-size: 18px;
  }

  .post-content {
    font-size: 15px;
  }

  .post-actions {
    flex-wrap: wrap;
    gap: 8px;
  }

  .post-actions button {
    flex: 1;
    min-width: 120px;
  }
}
</style>
