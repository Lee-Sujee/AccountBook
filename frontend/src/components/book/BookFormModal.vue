<<template>
    <div v-if="isVisible" class="modal-overlay" @click.self="$emit('closeModal')">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title">{{ isEditMode ? '가계부 항목 수정' : '새 항목 등록' }}</h3>
                <button @click="$emit('closeModal')" class="close-btn">&times;</button>
            </div>

            <form @submit.prevent="handleSubmit">
                <div class="form-group">
                    <label>구분</label>
                    <div>
                        <input type="radio" id="income" value="income" v-model="form.type" required>
                        <label for="income" class="type-label income-label">수입</label>
                        <input type="radio" id="expense" value="expense" v-model="form.type" required>
                        <label for="expense" class="type-label expense-label">지출</label>
                    </div>
                </div>

                <div class="form-group">
                    <label for="category">카테고리</label>
                    <!--구분에 따라서 카테고리 변화-->
                    <select id="category" v-model="form.category" required>
                        <option disabled value="">카테고리 선택</option>
                        <option v-for="category in categoryOptions"
                        :key="category"
                        :value="category">
                    {{ category }}</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="content">내용</label>
                    <input type="text" id="content" v-model="form.content">
                </div>

                <div class="form-group">
                    <label for="amount">금액 (원)</label>
                    <input type="number" id="amount" v-model.number="form.amount" required>
                </div>

                <div class="form-group">
                    <label for="memo">메모</label>
                    <textarea id="memo" v-model="form.memo" rows="3" required></textarea>
                </div>

                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary">{{ isEditMode ? '수정 완료' : '등록' }}</button>
                    <button type="button" @click="$emit('closeModal')" class="btn btn-secondary">취소</button>
                </div>
            </form>
        </div>
    </div>
</template>

<script setup>
import {ref, watch, computed } from 'vue';
import { useBookStore } from '@/stores/book';

const props = defineProps({
    isVisible: {type: Boolean, required: true},
    entryToEdit: {type: Object, default: null}
});

const emit = defineEmits(['closeModal']);
const bookStore = useBookStore();

//처음 초기화 상태 
const initialFormState = { id: null, category: '', content: '', type: 'expense', amount: 0, memo: '' };
const form = ref({...initialFormState});
const isEditMode = computed(()=> !!props.entryToEdit && !!props.entryToEdit.id);

//수정 데이터 폼 초기화
watch(()=>props.entryToEdit, (newEntry) => {
    if(newEntry) {
        form.value = {...newEntry};
    } else {
        form.value = {...initialFormState};
    }
}, {immediate: true});

//폼 제출시 store 액션
const handleSubmit = async () => {
    if(form.value.amount <= 0) {
        alert("금액은 1원 이상이어야 합니다.");
        return;
    }

    const dataToSend = {
        category: form.value.category,
        content: form.value.content,
        type: form.value.type,
        amount: form.value.amount,
        memo: form.value.memo,
    };

    try {
        //수정모드일 때 updateEntry 호출 (수정폼)
        if(isEditMode.value) {
            await bookStore.updateEntry(form.value.id, dataToSend);
        } else {
            //아니면 createEntry 호출 (생성폼)
            await bookStore.createEntry(dataToSend);
        }
    }
     catch (error) {
        console.error("폼 제출 실패:", error)
     }
};

//카테고리 (income)
const incomeCategory = [
    '급여',
    '용돈',
    '이자',
    '기타수입'
];

//카테고리 (expense)
const expenseCategory = [
    '식비',
    '교통비',
    '쇼핑',
    '경조사/회비',
    '마트/편의점',
    '커피',
    '생활용품',
    '기타'
];

const categoryOptions = computed(()=>{
    return form.value.type === 'income' ? incomeCategory : expenseCategory;
});

//type 바뀌면 기존 카테고리도 초기화
watch(()=>form.value.type, ()=>{
    form.value.category = '';
});

</script>

<style scoped>
.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.6);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
}

.modal-content {
    background: white;
    padding: 30px;
    border-radius: 12px;
    width: 90%;
    max-width: 500px; 
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
    position: relative;
    max-height: 90vh;
    overflow-y: auto; 
}

.modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-bottom: 1px solid #eee;
    padding-bottom: 15px;
    margin-bottom: 20px;
}

.modal-title {
    font-size: 1.5em;
    color: #333;
    font-weight: 700;
}

.close-btn {
    background: none;
    border: none;
    font-size: 1.8em;
    cursor: pointer;
    color: #aaa;
    line-height: 1;
}
.close-btn:hover {
    color: #333;
}

.form-group {
    margin-bottom: 20px;
}

.form-group label {
    display: block;
    margin-bottom: 8px;
    font-weight: 600;
    color: #555;
}

.form-group input[type="text"],
.form-group input[type="number"],
.form-group textarea {
    width: 100%;
    padding: 10px 12px;
    border: 1px solid #ccc;
    border-radius: 6px;
    box-sizing: border-box;
    transition: border-color 0.2s;
    font-size: 1em;
}

.form-group input:focus,
.form-group textarea:focus {
    border-color: #007bff; 
    outline: none;
}

.form-group textarea {
    resize: vertical;
}

.radio-group {
    display: flex;
    gap: 20px;
    align-items: center;
}

.type-label {
    display: inline-block;
    padding: 8px 15px;
    border-radius: 20px;
    cursor: pointer;
    transition: background-color 0.2s;
    font-weight: 500;
}

input[type="radio"] {
    display: none;
}

.income-label {
    background-color: #e6f7ff;
    color: #007bff;
    border: 1px solid #91d5ff;
}
.expense-label {
    background-color: #fff1f0;
    color: #ff4d4f;
    border: 1px solid #ffa39e;
}

input[type="radio"]:checked + .income-label {
    background-color: #007bff;
    color: white;
}
input[type="radio"]:checked + .expense-label {
    background-color: #ff4d4f;
    color: white;
}

.modal-footer {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
    padding-top: 20px;
    border-top: 1px solid #eee;
}

.btn {
    padding: 10px 20px;
    border: none;
    border-radius: 6px;
    cursor: pointer;
    font-weight: 600;
    transition: background-color 0.2s;
}

.btn-primary {
    background-color: #007bff;
    color: white;
}

.btn-primary:hover {
    background-color: #0056b3;
}

.btn-secondary {
    background-color: #6c757d;
    color: white;
}

.btn-secondary:hover {
    background-color: #5a6268;
}
</style>