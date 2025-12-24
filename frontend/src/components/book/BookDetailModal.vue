<template>
  <div v-if="isVisible" class="modal-overlay" @click.self="$emit('closeModal')">
    <div class="modal-content" role="dialog" aria-modal="true">
      <div class="modal-header">
        <h3 class="modal-title">가계부 상세 조회</h3>
      </div>

      <div v-if="entry" class="detail-body">
        <table class="detail-table">
          <tbody>
            <tr>
              <th>날짜</th>
              <td>{{ formatDate(entry.createdAt) }}</td>
            </tr>
            <tr>
              <th>구분</th>
              <td>
                <span
                  class="type-badge"
                  :class="entry.type === 'income' ? 'badge-income' : 'badge-expense'"
                >
                  {{ entry.type === 'income' ? '수입' : '지출' }}
                </span>
              </td>
            </tr>
            <tr>
              <th>카테고리</th>
              <td>{{ entry.category }}</td>
            </tr>
            <tr>
              <th>내용</th>
              <td class="cell-wrap">{{ entry.content }}</td>
            </tr>
            <tr>
              <th>금액</th>
              <td class="amount-cell">
                <span :class="entry.type === 'income' ? 'text-income' : 'text-expense'">
                  {{ formatAmount(entry.amount) }}원
                </span>
              </td>
            </tr>
            <tr>
              <th>메모</th>
              <td class="cell-wrap">{{ entry.memo }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="modal-footer">
        <button @click="handleEdit" class="btn btn-secondary">수정</button>
        <button @click="handleDelete" class="btn btn-danger">삭제</button>
        <button @click="emit('closeModal')" class="btn btn-primary">닫기</button>
      </div>
    </div>
  </div>
</template>


<script setup>
import { useBookStore } from '@/stores/book';

const props = defineProps({
    isVisible: { type: Boolean, required: true },
    entry: { type: Object, default: null }
});

const emit = defineEmits(['closeModal', 'editRequest', 'deleteRequest']);
const bookStore = useBookStore();

//버튼
const handleEdit = () => {
    emit('editRequest', props.entry);
}

const handleDelete = async () => {
    if (!props.entry || !props.entry.id) return;

    try {
        await bookStore.deleteEntry(props.entry.id);
    } catch (error) {
        console.error("삭제 요청 실패:", error);
    }
}

//금액 및 날짜 함수
const formatAmount = (amount) => amount ? amount.toLocaleString('ko-KR') : '0';
const formatDate = (dateString) => {
    if (!dateString) return '';
    return new Date(dateString).toLocaleDateString('ko-KR', {
        year: 'numeric', month: '2-digit', day: '2-digit',
        hour: '2-digit', minute: '2-digit'
    });
}
</script>

<style scoped>
/* overlay */
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(17, 24, 39, 0.35);
  backdrop-filter: blur(6px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 18px;
  z-index: 9999;
}

/* modal card */
.modal-content {
  width: min(560px, 100%);
  background: #ededed;
  border-radius: 16px;
  box-shadow: 0 18px 60px rgba(0, 0, 0, 0.25);
  overflow: hidden;
  border: 1px solid rgba(0, 99, 248, 0.12);
}

/* header */
.modal-header {
  padding: 18px 20px;
  background: #ededed;
  border-bottom: 2px solid #0063f8;
}

.modal-title {
  margin: 0;
  font-size: 18px;
  font-weight: 800;
  color: #0063f8;
  letter-spacing: 0.2px;
}

/* body */
.detail-body {
  padding: 16px 20px 6px;
}

/* ✅ table: 배경 없이 회색 라인 + 라벨(th)만 연회색 */
.detail-table {
  width: 100%;
  border-collapse: collapse; /* 라인 깔끔 */
  background: transparent;
}

.detail-table th,
.detail-table td {
  padding: 10px 10px; /* 조금 더 촘촘 */
  font-size: 14px;
  border-bottom: 1px solid #d6d6d6; /* 회색 줄 */
  vertical-align: middle;
}

.detail-table tr:last-child th,
.detail-table tr:last-child td {
  border-bottom: none;
}

/* ✅ 왼쪽 라벨만 살짝 회색 */
.detail-table th {
  width: 28%;
  text-align: left;
  font-weight: 800;
  color: #374151;
  background: rgba(0, 0, 0, 0.04); /* 아주 옅은 회색 */
}

/* 값 칸은 투명(배경 없음) */
.detail-table td {
  background: transparent;
  color: #111827;
  font-weight: 600;
}

.cell-wrap {
  white-space: pre-wrap;
  word-break: break-word;
  font-weight: 500;
}

/* badges */
.type-badge {
  display: inline-flex;
  align-items: center;
  padding: 6px 12px;
  border-radius: 9999px;
  font-size: 12px;
  font-weight: 900;
  letter-spacing: 0.2px;
}

.badge-income {
  background: rgba(16, 185, 129, 0.12);
  color: #0f766e;
  border: 1px solid rgba(16, 185, 129, 0.28);
}

.badge-expense {
  background: rgba(239, 68, 68, 0.12);
  color: #b91c1c;
  border: 1px solid rgba(239, 68, 68, 0.28);
}

/* amount 강조 */
.amount-cell {
  font-size: 16px;
}

.text-income {
  color: #0f766e;
  font-weight: 900;
}

.text-expense {
  color: #b91c1c;
  font-weight: 900;
}

/* ✅ footer buttons: 사이즈 줄임 */
.modal-footer {
  padding: 10px 16px 14px;
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  background: #ededed;
}

.btn {
  border: none;
  cursor: pointer;
  border-radius: 9999px;
  padding: 7px 12px;      /* 작게 */
  font-size: 12px;        /* 작게 */
  font-weight: 800;
  white-space: nowrap;
  line-height: 1;
  transition: transform 0.06s ease, filter 0.12s ease;
}

.btn:active {
  transform: translateY(1px);
}

.btn-primary {
  background: #0063f8;
  color: #fff;
}

.btn-secondary {
  color: #0063f8;
  border: 1px solid #0063f8;
}

.btn-danger {
    border: 1px solid #0063f8;
  color: #0063f8;
}

.btn:hover {
  filter: brightness(0.96);
}

/* 모바일 대응 */
@media (max-width: 480px) {
  .modal-header {
    padding: 16px;
  }

  .detail-body {
    padding: 14px 16px 6px;
  }

  .modal-footer {
    padding: 10px 12px 12px;
    flex-wrap: wrap;
    justify-content: center;
  }

  .btn {
    flex: 0 0 auto;     /* 꽉 채우지 않게 */
    min-width: 72px;    /* 너무 작지 않게 */
  }
}
</style>
