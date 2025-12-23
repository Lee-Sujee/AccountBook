import { ref } from "vue";
import { defineStore } from "pinia";
import router from "@/router";
import { signUp, loginUser, getMyPage, updateMyPageApi, changePassword } from "@/api/userApi";

export const useAuthStore = defineStore('auth', () => {
  const loginUserInfo = ref(
    localStorage.getItem("loginUser")
      ? JSON.parse(localStorage.getItem("loginUser"))
      : null
  );

  const accessToken = ref(localStorage.getItem("accessToken") || null);
  const myPageInfo = ref(null);

  // 회원가입
  const insertUser = async (userData) => {
    try {
      await signUp(userData);
      alert("회원가입 완료!");
      router.push({ name: "login" });
    } catch (err) {
      console.error("회원가입 실패:", err);
      alert("회원가입 실패");
    }
  };

  // 로그인
  const login = async (loginData) => {
    try {
      const res = await loginUser(loginData);
      const data = res.data;

      accessToken.value = data.token;
      localStorage.setItem("accessToken", data.token);

      loginUserInfo.value = {
        id: data.id,
        email: data.email,
        name: data.name,
      };
      localStorage.setItem("loginUser", JSON.stringify(loginUserInfo.value));

      localStorage.setItem("showStats", "false");

      router.push({ name: "home" });
    } catch (err) {
      console.error("로그인 실패:", err);
      alert("아이디 또는 비밀번호가 올바르지 않습니다.");
    }
  };

  // 로그아웃
  const logout = () => {
    accessToken.value = null;
    loginUserInfo.value = null;
    localStorage.removeItem("accessToken");
    localStorage.removeItem("loginUser");
    router.push({ name: "login" });
  };

  // 마이페이지 조회
  const fetchMyPage = async () => {
    try {
      const res = await getMyPage();
      myPageInfo.value = res.data;
    } catch (err) {
      console.error("마이페이지 조회 실패:", err);
    }
  };

  // 정보 수정
  const updateMyPage = async (updateData) => {
    try {
      const res = await updateMyPageApi(updateData);
      myPageInfo.value = res.data;
      return res;
    } catch (err) {
      console.error("정보 수정 실패:", err);
      alert("정보 수정 실패");
      throw err;
    }
  };

  // 비밀번호 변경
  const changePw = async (pwData) => {
    try {
      const res = await changePassword(pwData);
      alert("비밀번호 변경 완료!");
      return res;
    } catch (err) {
      console.error("비밀번호 변경 실패:", err);
      alert("비밀번호 변경 실패");
      throw err;
    }
  };

  return {
    loginUserInfo,
    accessToken,
    myPageInfo,
    insertUser,
    login,
    logout,
    fetchMyPage,
    updateMyPage,
    changePw,
  };
});
