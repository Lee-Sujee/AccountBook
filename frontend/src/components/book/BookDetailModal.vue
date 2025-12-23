<template>
    <div v-if="isVisible" class="modal-overlay" @click.self="$emit('closeModal')">
        <div class="modal-content">
            <div class="modal-header">
                <h3 class="modal-title">가계부 상세 조회</h3>
            </div>

            <div v-if="entry" class="detail-body-only-table">
                <table class="detail-table">
                    <thead>
                        <tr>
                            <th>날짜</th>
                            <td>{{ formatDate(entry.createdAt) }}</td>
                        </tr>
                        <tr>
                            <th>구분</th>
                            <td :class="entry.type === 'income' ? 'text-income' : 'text-expense'">
                                {{ entry.type === 'income' ? '수입' : '지출' }}
                            </td>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <th>카테고리</th>
                            <td>{{ entry.category }}</td>
                        </tr>
                        <tr>
                            <th>내용</th>
                            <td>{{ entry.content }}</td>
                        </tr>
                        <tr>
                            <th>금액</th>
                            <td>{{ formatAmount(entry.amount) }}원</td>
                        </tr>
                        <tr>
                            <th>메모</th>
                            <td>{{ entry.memo }}</td>
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
//이건 몰라서 gpt가 다 알려줌,,
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
.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
}

.modal-content {
    background: white;
    padding: 25px;
    border-radius: 8px;
    width: 90%;
    max-width: 500px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.detail-body-only-table {
    padding: 10px 0;
}

.detail-table {
    width: 100%;
    border-collapse: collapse;
}

.detail-table th,
.detail-table td {
    padding: 12px;
    border-bottom: 1px solid #eee;
    text-align: left;
}

.detail-table th {
    width: 30%;
    background-color: #f7f7f7;
    font-weight: bold;
}

.text-income {
    color: #029b07 !important;
    font-weight: bold;
}

.text-expense {
    color: #f04400 !important;
    font-weight: bold;
}
</style>