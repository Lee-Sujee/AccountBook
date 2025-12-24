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
/* 기존 스타일 유지 */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1100;
  backdrop-filter: blur(2px);
}

.modal-content {
  background: #ffffff;
  padding: 30px;
  border-radius: 20px;
  width: 90%;
  max-width: 420px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}

.modal-title {
  font-size: 20px;
  font-weight: 800;
  color: #0063f8;
  margin: 0;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #999;
}

.form-group {
  margin-bottom: 22px;
}

.form-group label {
  display: block;
  font-size: 13px;
  font-weight: 700;
  color: #0063f8;
  margin-bottom: 8px;
}

.underline-input {
  width: 100%;
  border: none;
  border-bottom: 2px solid rgba(0, 99, 248, 0.2);
  padding: 8px 4px;
  font-size: 15px;
  font-weight: 600;
  color: #333;
  background: transparent;
  outline: none;
  box-sizing: border-box;
}

.underline-input:focus {
  border-bottom-color: #0063f8;
}

.input-grid {
  display: grid;
  grid-template-columns: 1.2fr 1fr;
  gap: 15px;
}

.type-selector {
  display: flex;
  justify-content: center;
  margin-bottom: 30px;
}

.radio-wrapper {
  display: flex;
  background: #f0f0f0;
  padding: 4px;
  border-radius: 12px;
  width: 100%;
}

.radio-wrapper input[type="radio"] {
  display: none;
}

.type-btn {
  flex: 1;
  text-align: center;
  padding: 10px;
  border-radius: 9px;
  cursor: pointer;
  font-weight: 700;
  font-size: 14px;
  color: #666;
  transition: all 0.2s;
}

input[id="expense"]:checked+.expense {
  background: #f04400;
  color: #fff;
}

input[id="income"]:checked+.income {
  background: #029b07;
  color: #fff;
}

.modal-footer {
  margin-top: 30px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  align-items: center;
}

.btn {
  width: 100%;
  padding: 14px;
  border-radius: 25px;
  font-weight: 700;
  font-size: 15px;
  cursor: pointer;
  border: none;
}

.btn-primary {
  background: #0063f8;
  color: #fff;
}

.btn-ghost {
  background: transparent;
  color: #666;
  font-size: 14px;
}

.btn-ghost:hover {
  text-decoration: underline;
}

.memo-area {
  border: 1.5px solid rgba(0, 99, 248, 0.2);
  border-radius: 8px;
  padding: 10px;
}
</style>