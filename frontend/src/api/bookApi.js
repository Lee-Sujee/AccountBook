import instance from "./axiosInstance";

// GET /book
export const getBookList = (params) => instance.get("/book", { params });

// POST /book
export const createBookEntry = (bookData) => instance.post("/book", bookData);

// GET /book/{id}
export const getBookDetail = (bookId) => instance.get(`/book/${bookId}`);

// PUT /book/{id}
export const updateBookEntry = (bookId, bookData) => instance.put(`/book/${bookId}`, bookData);

// DELETE /book/{id}
export const deleteBookEntry = (bookId) => instance.delete(`/book/${bookId}`);

// 재정관리
export const analyzeFinancesApi = (data) => instance.post(`/book/analyze-finances`, data);
