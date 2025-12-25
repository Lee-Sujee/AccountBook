<template>
  <div class="list-wrapper">
    <div class="list-header">
      <h4>댓글</h4>
    </div>

    <div v-if="commentList.length === 0">
      <p>아직 등록된 댓글이 없습니다.</p>
    </div>

    <div v-else>
      <CommentItem
        v-for="comment in pagedComments"
        :key="comment.id"
        :comment="comment"
        @edit-comment="startEdit"
        @delete-comment="startDelete"
      />

      <!-- 페이지네이션 -->
      <div v-if="totalPages > 1" class="pagination">
        <button class="page-btn" :disabled="currentPage === 1" @click="goPage(1)">«</button>
        <button class="page-btn" :disabled="currentPage === 1" @click="goPage(currentPage - 1)">‹</button>

        <button
          v-for="p in visiblePages"
          :key="p"
          class="page-btn"
          :class="{ active: p === currentPage }"
          @click="goPage(p)"
        >
          {{ p }}
        </button>

        <button class="page-btn" :disabled="currentPage === totalPages" @click="goPage(currentPage + 1)">›</button>
        <button class="page-btn" :disabled="currentPage === totalPages" @click="goPage(totalPages)">»</button>
      </div>
    </div>

    <CommentForm
      :boardId="boardId"
      :initialData="editingComment"
    />
  </div>
</template>

<script setup>
import { useCommunityStore } from "@/stores/community";
import { computed, onMounted, ref, watch } from "vue";
import CommentItem from "./CommentItem.vue";
import CommentForm from "./CommentForm.vue";

const props = defineProps({
  boardId: Number,
});

const store = useCommunityStore();
const commentList = computed(() => store.commentList ?? []);

const editingComment = ref(null);

/* 페이지네이션 설정 */
const pageSize = 5;        // 한 페이지에 댓글은 5개
const pageGroupSize = 5;   // 페이지 버튼은 항상 5개씩
const currentPage = ref(1);

const totalPages = computed(() => {
  return Math.max(1, Math.ceil(commentList.value.length / pageSize));
});

const pagedComments = computed(() => {
  const start = (currentPage.value - 1) * pageSize;
  return commentList.value.slice(start, start + pageSize);
});

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
};

/* 댓글 목록 로딩 */
onMounted(() => {
  store.getCommentList(props.boardId);
});

/* 게시글이 바뀌면 댓글 다시 불러오고 페이지 초기화 */
watch(
  () => props.boardId,
  (newId) => {
    if (!newId) return;
    currentPage.value = 1;
    editingComment.value = null;
    store.getCommentList(newId);
  }
);

watch(
  () => commentList.value.length,
  () => {
    if (currentPage.value > totalPages.value) currentPage.value = totalPages.value;
    if (commentList.value.length === 0) currentPage.value = 1;
  }
);

const startEdit = (comment) => {
  editingComment.value = comment;
};

const startDelete = async (comment) => {
  if (!confirm("정말 이 댓글을 삭제하시겠습니까?")) return;
  await store.deleteComment(props.boardId, comment.id);
};
</script>

<style scoped>
.list-header {
  border-bottom: 2px solid #0063f8;
  padding-bottom: 8px;
  margin-bottom: 12px;
}

.pagination {
  margin-top: 14px;
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
  background: #ededed;
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
