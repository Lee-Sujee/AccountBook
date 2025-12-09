import { ref } from "vue";
import { defineStore } from "pinia";
import router from "@/router";
import { signUp, loginUser, getMyPage, updateMyPageApi, changePassword } from "@/api/userApi";


export const useAuthStore = defineStore('auth', () => {
  
  // 로그인 된 사용자
  const loginUserInfo = ref(
    localStorage.getItem("loginUser")
      ? JSON.parse(localStorage.getItem("loginUser"))
      : null
  );

  // accessToken 상태
  const accessToken = ref(localStorage.getItem("accessToken") || null);

  //회원가입
  const insertUser = (userData) => {
    signUp(userData)
      .then((res) => {
        console.log("회원가입 성공:", res);
        alert("회원가입이 완료되었습니다!");
        router.push({ name: "login" });
      })
      .catch((err) => {
        console.error("회원가입 실패:", err);
        alert("회원가입에 실패했습니다.");
      });
  };

  //로그인
  const login = (loginData) => {
    loginUser(loginData)
      .then((res) => {
        console.log("로그인 성공:", res);

        const data = res.data; 

        accessToken.value = data.token; 

        loginUserInfo.value = {
          id: data.id,
          email: data.email,
          name: data.name,
        };

        // 로컬 스토리지 저장
        localStorage.setItem("accessToken", data.token);
        localStorage.setItem("loginUser", JSON.stringify(loginUserInfo.value));

        router.push({ name: "home" });
      })
      .catch((err) => {
        console.error("로그인 실패:", err);
        alert("아이디 또는 비밀번호가 올바르지 않습니다.");
      });
  };

  //로그아웃
  const logout = () => {
    accessToken.value = null;
    loginUserInfo.value = null;

    localStorage.removeItem("accessToken");
    localStorage.removeItem("loginUser");

    router.push({ name: "login" });
  };

  // 마이페이지 정보 가져오기
  const myPageInfo = ref(null);

  const fetchMyPage = () => {
    getMyPage()
      .then((res) => {
        console.log("마이페이지 데이터:", res.data);
       myPageInfo.value = res.data;
     })
      .catch((err) => {
        console.error("마이페이지 불러오기 실패:", err);
        alert("마이페이지 정보를 불러오지 못했습니다.");
      });
  };

  // 내 정보 수정
  const updateMyPage = (updateData) => {
  return updateMyPageApi(updateData)
    .then((res) => {
      myPageInfo.value = res.data;
 
    });
};

const changePw = (pwData) => {
  return changePassword(pwData)
    .then((res) => {
      alert("비밀번호가 성공적으로 변경되었습니다!");
      return res;
    })
    .catch((err) => {
      console.error("비밀번호 변경 실패:", err);
      alert("비밀번호 변경에 실패했습니다.");
      throw err;
    });
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