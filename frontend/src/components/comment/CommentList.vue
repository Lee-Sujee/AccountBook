<template>
  <div class="list-wrapper">
    <div class="list-header">
      <h2>댓글목록</h2>
    </div>

    <div v-if="commentList.length === 0">
      <p>댓글이 없습니다.</p>
    </div>

    <div v-else>
      <CommentItem
        v-for="comment in commentList"
        :key="comment.id"
        :comment="comment"
        @edit-comment="startEdit"
        @delete-comment="startDelete"
      />
    
    </div>

    <CommentForm
      :boardId="boardId"
      :initialData="editingComment"
    />
  </div>
</template>

<script setup>
import { useCommunityStore } from '@/stores/community';
import { computed, onMounted, ref } from 'vue';
import CommentItem from './CommentItem.vue';
import CommentForm from './CommentForm.vue';

const props = defineProps({
  boardId: Number
});

const store = useCommunityStore();
const commentList = computed(() => store.commentList);

const editingComment = ref(null);

onMounted(() => {
  store.getCommentList(props.boardId);
});

const startEdit = (comment) => {
  editingComment.value = comment;
};

const startDelete = (comment) => {
  if (!confirm('정말 이 댓글을 삭제하시겠습니까?')) return;
  store.deleteComment(props.boardId, comment.id);
}
</script>
