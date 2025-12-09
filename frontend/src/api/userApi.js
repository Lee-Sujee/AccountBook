import instance from "./axiosInstance";

// 회원가입
export const signUp = (userData) => {
    return instance.post("/user/signUp", userData);
}

// 로그인
export const loginUser = (loginData) => {
    return instance.post("/user/login", loginData);
}

// 마이페이지
export const getMyPage = () => {
    return instance.get("/user/myPage");
}

// 내 정보 수정
export const updateMyPageApi = (data) => {
  return instance.put("/user/myPage", data);
}

// 비밀번호 변경
export const changePassword = (data) => {
  return instance.put("/user/password", data);
}