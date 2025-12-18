import axios from "axios";

// axios 인스턴스 생성
const instance = axios.create({
<<<<<<< HEAD
    baseURL: "http://localhost:8080",
    withCredentials: true,
=======
  baseURL: "http://localhost:8080",
>>>>>>> 4dc210cf13e77fab7974674fce106b8fe9a33c16
});

// 모든 요청에 JWT 토큰 자동 추가
instance.interceptors.request.use((config) => {
  const token = localStorage.getItem("accessToken");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default instance;
