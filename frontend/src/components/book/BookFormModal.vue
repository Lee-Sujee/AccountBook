<template>
  <div v-if="isVisible" class="modal-overlay" @click.self="$emit('closeModal')">
    <div class="modal-content">
      <div class="modal-header">
        <h3 class="modal-title">{{ isEditMode ? '항목 수정' : '새 항목 등록' }}</h3>
        <button @click="$emit('closeModal')" class="close-btn">&times;</button>
      </div>

      <form @submit.prevent="handleSubmit" class="form-container">
        <div class="form-group type-selector">
          <div class="radio-wrapper">
            <input type="radio" id="expense" value="expense" v-model="form.type" required />
            <label for="expense" class="type-btn expense">지출</label>

            <input type="radio" id="income" value="income" v-model="form.type" required />
            <label for="income" class="type-btn income">수입</label>
          </div>
        </div>

        <div class="input-grid">
          <div class="form-group">
            <label for="createdAt">날짜</label>
            <input type="datetime-local" id="createdAt" v-model="form.createdAt" class="underline-input" required />
          </div>

          <div class="form-group">
            <label for="category">카테고리</label>
            <select id="category" v-model="form.category" class="underline-input select-box" required>
              <option disabled value="">선택</option>
              <option v-for="cat in categoryOptions" :key="cat" :value="cat">
                {{ cat }}
              </option>
            </select>
          </div>
        </div>

        <div class="form-group">
          <label for="content">내용</label>
          <input type="text" id="content" v-model="form.content" class="underline-input" placeholder="내용을 입력하세요"
            required />
        </div>

        <div class="form-group">
          <label for="amount">금액 (원)</label>
          <input type="number" id="amount" v-model.number="form.amount" class="underline-input amount-input"
            placeholder="0" required />
        </div>

        <div class="form-group">
          <label for="memo">메모</label>
          <textarea id="memo" v-model="form.memo" class="underline-input memo-area" rows="2"
            placeholder="메모를 입력하세요"></textarea>
        </div>

        <div class="modal-footer">
          <button type="submit" class="btn btn-primary">{{ isEditMode ? '수정 완료' : '등록하기' }}</button>
          <button type="button" @click="$emit('closeModal')" class="btn btn-ghost">취소</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, computed } from 'vue'
import { useBookStore } from '@/stores/book'

const props = defineProps({
  isVisible: { type: Boolean, required: true },
  entryToEdit: { type: Object, default: null },
})

const emit = defineEmits(['closeModal'])
const bookStore = useBookStore()

const toDatetimeLocal = (value) => {
  if (!value) return ''
  const d = value instanceof Date ? value : new Date(value)
  const pad = (n) => String(n).padStart(2, '0')
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`
}

const getInitialState = () => ({
  id: null,
  createdAt: toDatetimeLocal(new Date()),
  category: '',
  content: '',
  type: 'expense',
  amount: 0,
  memo: '',
})

const form = ref(getInitialState())

const isEditMode = computed(() => !!props.entryToEdit && !!props.entryToEdit.id)

const incomeCategory = ['급여', '용돈', '이자', '기타수입']
const expenseCategory = ['식비', '교통비', '쇼핑', '경조사/회비', '마트/편의점', '커피', '생활용품', '기타']
const categoryOptions = computed(() => (form.value.type === 'income' ? incomeCategory : expenseCategory))

// 모달이 열릴 때 데이터를 체크하여 초기화 또는 채우기
watch(
  [() => props.isVisible, () => props.entryToEdit],
  ([newVisible, newEntry]) => {
    if (newVisible) {
      if (newEntry) {
        form.value = {
          ...newEntry,
          createdAt: toDatetimeLocal(newEntry.createdAt),
        }
      } else {
        form.value = getInitialState()
      }
    }
  },
  { immediate: true }
)

watch(
  () => form.value.type,
  () => { if (!isEditMode.value) form.value.category = '' }
)

const handleSubmit = async () => {
  if (form.value.amount <= 0) {
    alert('금액은 1원 이상이어야 합니다.')
    return
  }

  const dataToSend = { ...form.value }

  try {
    if (isEditMode.value) {
      await bookStore.updateEntry(form.value.id, dataToSend)
    } else {
      await bookStore.createEntry(dataToSend)
    }
    emit('closeModal')
  } catch (error) {
    console.error('폼 제출 실패:', error)
  }
}
</script>

<style scoped>
/* overlay (상세조회 모달과 동일 톤) */
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(17, 24, 39, 0.35);
  backdrop-filter: blur(6px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 18px;
  z-index: 1100;
}

/* modal card */
.modal-content {
  width: min(520px, 100%);
  background: #ededed;
  border-radius: 16px;
  box-shadow: 0 18px 60px rgba(0, 0, 0, 0.25);
  overflow: hidden;
  border: 1px solid rgba(0, 99, 248, 0.12);
}

/* header */
.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
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

.close-btn {
  width: 34px;
  height: 34px;
  border-radius: 9999px;
  border: 1px solid rgba(0, 99, 248, 0.18);
  background: #fff;
  color: #0063f8;
  font-size: 22px;
  line-height: 1;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.close-btn:hover {
  filter: brightness(0.97);
}

/* form container */
.form-container {
  padding: 16px 20px 12px;
}

/* label */
.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  font-size: 12px;
  font-weight: 800;
  color: #0063f8;
  margin-bottom: 8px;
}

/* inputs: 깔끔한 언더라인 스타일 */
.underline-input {
  width: 100%;
  border: none;
  border-bottom: 1px solid #d6d6d6; /* 회색 라인 */
  padding: 10px 6px;
  font-size: 14px;
  font-weight: 600;
  color: #111827;
  background: transparent;
  outline: none;
  box-sizing: border-box;
}

.underline-input:focus {
  border-bottom-color: #0063f8;
}
.underline-input,
input,
select,
textarea {
  font-family: inherit;
  font-size: 14px;
  font-weight: 600;
}

/* 특히 datetime-local 강제 */
input[type="datetime-local"] {
  font-family: inherit !important;
  font-size: 14px !important;
  font-weight: 600 !important;
  color: #111827;
}
.select-box {
  appearance: none;
}

/* textarea는 약간만 박스로 */
.memo-area {
  border: 1px solid #d6d6d6;
  border-radius: 10px;
  padding: 10px 10px;
  resize: none;
  background: rgba(255, 255, 255, 0.45);
}

.memo-area:focus {
  border-color: #0063f8;
  background: rgba(255, 255, 255, 0.7);
}

/* grid */
.input-grid {
  display: grid;
  grid-template-columns: 1.2fr 1fr;
  gap: 14px;
}

/* type selector (지출/수입) */
.type-selector {
  display: flex;
  justify-content: center;
  margin-bottom: 18px;
}

.radio-wrapper {
  display: flex;
  gap: 6px;
  width: 100%;
  background: rgba(255, 255, 255, 0.6);
  border: 1px solid rgba(0, 99, 248, 0.12);
  padding: 6px;
  border-radius: 14px;
}

.radio-wrapper input[type="radio"] {
  display: none;
}

.type-btn {
  flex: 1;
  text-align: center;
  padding: 10px 0;
  border-radius: 12px;
  cursor: pointer;
  font-weight: 900;
  font-size: 13px;
  color: #374151;
  transition: filter 0.15s ease, transform 0.06s ease;
  user-select: none;
}

.type-btn:active {
  transform: translateY(1px);
}

/* 선택 전: 살짝 톤 */
.type-btn.expense {
  background: rgba(239, 68, 68, 0.10);
}

.type-btn.income {
  background: rgba(16, 185, 129, 0.12);
}

/* 선택 후 */
input#expense:checked + .expense {
  background: #ef4444;
  color: #fff;
}

input#income:checked + .income {
  background: #10b981;
  color: #fff;
}

/* footer buttons (상세조회 모달처럼 작고 pill) */
.modal-footer {
  margin-top: 10px;
  padding: 0 0 6px;
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

.btn {
  border: none;
  cursor: pointer;
  border-radius: 9999px;
  padding: 7px 12px; /* ✅ 작게 */
  font-size: 12px;
  font-weight: 800;
  line-height: 1;
  white-space: nowrap;
  transition: transform 0.06s ease, filter 0.12s ease;
}

.btn:active {
  transform: translateY(1px);
}

.btn-primary {
  background: #0063f8;
  color: #fff;
}

.btn-ghost {
  background: #ffffff;
  color: #0063f8;
  border: 1px solid #cbd7ff;
}

.btn:hover {
  filter: brightness(0.96);
}

/* 모바일 대응 */
@media (max-width: 480px) {
  .modal-header {
    padding: 16px;
  }

  .form-container {
    padding: 14px 16px 10px;
  }

  .input-grid {
    grid-template-columns: 1fr;
    gap: 12px;
  }

  .modal-footer {
    justify-content: center;
    flex-wrap: wrap;
    gap: 8px;
    padding-bottom: 10px;
  }

  .btn {
    min-width: 92px;
  }
}
</style>
