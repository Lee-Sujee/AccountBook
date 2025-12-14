import { ref } from "vue";
import { useRouter } from "vue-router";
import { defineStore } from "pinia";
import {
  getCommunityList,
  createCommunityApi,
  fetchCommunityDetailApi,
  updateCommunityApi,
  deleteCommunityApi,
  toggleBoardLikeApi,
} from "@/api/communityApi";

export const useCommunityStore = defineStore("community", () => {
  const router = useRouter();

  const communityList = ref([]);
  const communityDetail = ref(null);
  const currentDetailId = ref(null);

  const liked = ref(false);
  const likes = ref(0);

  // 전체 조회
  const fetchCommunityList = () => {
    return getCommunityList()
      .then((res) => {
        communityList.value = res.data;
      })
      .catch((err) => {
        console.error("게시판 불러오기 실패:", err);
      });
  };

  // 게시글 작성
  const createCommunity = (writeData) => {
    return createCommunityApi(writeData)
      .then((res) => {
        alert("게시글이 등록되었습니다.");
        router.push({ name: "CommunityDetail", params: { id: res.data.id } });
      })
      .catch((err) => {
        console.error("게시글 등록 실패:", err);
        throw err;
      });
  };

  // 상세 조회
  const fetchCommunityDetail = (id) => {
    currentDetailId.value = id;
    communityDetail.value = null;

    return fetchCommunityDetailApi(id)
      .then((res) => {
        if (currentDetailId.value !== id) return;

        communityDetail.value = res.data;
        liked.value = res.data.liked ?? false;
        likes.value = res.data.likes ?? 0;
      })
      .catch((err) => {
        console.error("게시글 상세 조회 실패:", err);
      });
  };

  // 좋아요 토글 (서버 기준 값만 사용)
  const toggleLike = (boardId) => {
    return toggleBoardLikeApi(boardId)
      .then((res) => {
        const result = res.data; // { liked: boolean, likes: number }

        liked.value = result.liked;
        likes.value = result.likes;

        if (communityDetail.value) {
          communityDetail.value.likes = result.likes;
        }

        return result;
      })
      .catch((err) => {
        console.error("좋아요 처리 실패:", err);
        throw err;
      });
  };

  // 게시글 수정
  const updateCommunity = (id, updateData) => {
    return updateCommunityApi(id, updateData)
      .then(() => fetchCommunityDetail(id))
      .catch((err) => {
        console.error("게시글 수정 실패:", err);
        throw err;
      });
  };

  // 게시글 삭제
  const deleteCommunity = (id) => {
    return deleteCommunityApi(id)
      .then(() => {
        communityDetail.value = null;
      })
      .catch((err) => {
        console.error("게시글 삭제 실패:", err);
        throw err;
      });
  };

  return {
    communityList,
    communityDetail,
    liked,
    likes,
    fetchCommunityList,
    createCommunity,
    fetchCommunityDetail,
    updateCommunity,
    deleteCommunity,
    toggleLike,
  };
});
