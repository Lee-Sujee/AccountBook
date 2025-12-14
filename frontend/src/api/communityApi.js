import instance from "./axiosInstance";


// 전체 리스트
export const getCommunityList = () => {
    return instance.get("/community");
}

export const createCommunityApi = (writeData) => {
    return instance.post("/community/write", writeData)
}

export const fetchCommunityDetailApi = (id) => {
    return instance.get(`/community/${id}`)
}

export const increaseCommunityViewApi = (id) => {
  return instance.post(`/community/${id}/view`);
}

export const toggleBoardLikeApi = (id) => {
    return instance.post(`/community/${id}/like`);
}

export const updateCommunityApi = (id, data) => {
  return instance.put(`/community/${id}`, data);
}

export const deleteCommunityApi = (id) => {
    return instance.delete(`/community/${id}`);
}