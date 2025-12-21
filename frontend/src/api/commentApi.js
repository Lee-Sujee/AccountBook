import instance from "./axiosInstance";

// 댓글 전체조회
export const getCommentListApi = (boardId) => {
    return instance.get(`/community/${boardId}/comments`)
}

// 댓글 작성
export const createCommentApi = (boardId, content) => {
  return instance.post(`/community/${boardId}/comments`,
    {content: content},
    {headers: {'Content-Type': 'application/json'}}
  );
}

// 댓글 수정
export const updateCommentApi = (commentId, content) => {
    return instance.put(`/community/comments/${commentId}`,
    {content: content},
    {headers: {'Content-Type': 'application/json'}}
  );
}

// 댓글 삭제
export const deleteCommentApi = (commentId) => {
    return instance.delete(`/community/comments/${commentId}`)
}