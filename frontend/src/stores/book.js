import { defineStore } from "pinia";
import { ref } from "vue";
import { getBookList, createBookEntry, updateBookEntry, deleteBookEntry } from "@/api/bookApi";

export const useBookStore = defineStore('book', () => {
  const bookEntries = ref([]);
  const selectEntry = ref(null);
  const isFormModalVisible = ref(false);
  const isDetailModalVisible = ref(false);

  const closeModal = () => {
    isFormModalVisible.value = false;
    isDetailModalVisible.value = false;
    selectEntry.value = null;
  };

  const openCreateModal = () => {
    selectEntry.value = null;
    isFormModalVisible.value = true;
  };

  const openDetailModal = (entryData) => {
    selectEntry.value = entryData;
    isDetailModalVisible.value = true;
  };

  const openEditModal = (entryData) => {
    selectEntry.value = entryData;
    isFormModalVisible.value = true;
    isDetailModalVisible.value = false;
  };

  const handleSuccess = async (message) => {
    alert(message);
    closeModal();
    await loadBookList();
  }

  //JWT 포함 요청
  const loadBookList = async () => {
    try {
      const res = await getBookList();
      bookEntries.value = res.data;
    } catch (err) {
      console.error("가계부 목록 로드 실패:", err);
      alert("가계부 목록을 불러오는데 실패했습니다. 로그인 상태를 확인하세요.");
    }
  };

  const createEntry = async (bookData) => {
    try {
      await createBookEntry(bookData);
      return handleSuccess("가계부 항목이 성공적으로 등록되었습니다!");
    } catch (err) {
      console.error("항목 등록 실패:", err);
      alert("항목 등록에 실패했습니다.");
      throw err;
    }
  };

  const updateEntry = async (bookId, bookData) => {
    try {
      await updateBookEntry(bookId, bookData);
      return handleSuccess("가계부 항목이 성공적으로 수정되었습니다!");
    } catch (err) {
      console.error("항목 수정 실패:", err);
      alert("항목 수정에 실패했습니다.");
      throw err;
    }
  };

  const deleteEntry = async (bookId) => {
    if (!confirm("정말 삭제하시겠습니까?")) return;
    try {
      await deleteBookEntry(bookId);
      return handleSuccess("가계부 항목이 성공적으로 삭제되었습니다!");
    } catch (err) {
      console.error("항목 삭제 실패:", err);
      alert("삭제에 실패했습니다. 권한을 확인하세요.");
      throw err;
    }
  };

  return {
    bookEntries,
    selectEntry,
    isFormModalVisible,
    isDetailModalVisible,
    loadBookList,
    createEntry,
    updateEntry,
    deleteEntry,
    openCreateModal,
    openEditModal,
    openDetailModal,
    closeModal,
  };
});
