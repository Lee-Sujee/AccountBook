<template>
  <div class="deposit-list">
    <h4>저축 내역</h4>

    <div v-if="depositList.length === 0">
      아직 저축 내역이 없습니다.
    </div>

    <template v-else>
      <table class="deposit-table">
        <thead>
          <tr>
            <th>날짜</th>
            <th>금액</th>
          </tr>
        </thead>

        <tbody>
          <tr
            v-for="d in pagedDeposits"
            :key="d.id"
            class="clickable-row"
            @click="openEdit(d)"
          >
            <td>{{ formatDate(d.createdAt) }}</td>
            <td>{{ formatAmount(d.amount) }}</td>
          </tr>
        </tbody>
      </table>

      <!-- ✅ 페이지네이션 -->
      <div class="pagination" v-if="totalPages > 1">
        <button
          class="page-btn"
          :disabled="currentPage === 1"
          @click="goPage(currentPage - 1)"
        >
          ‹
        </button>

        <button
          v-for="p in visiblePages"
          :key="p"
          class="page-btn"
          :class="{ active: p === currentPage }"
          @click="goPage(p)"
        >
          {{ p }}
        </button>

        <button
          class="page-btn"
          :disabled="currentPage === totalPages"
          @click="goPage(currentPage + 1)"
        >
          ›
        </button>
      </div>
    </template>

    <!-- 수정 모달 -->
    <div v-if="isEditOpen" class="modal-backdrop" @click.self="closeEdit">
      <div class="modal">
        <h5>저축 금액 수정</h5>

        <div class="field">
          <label>날짜</label>
          <div class="value">{{ formatDate(selectedDeposit?.createdAt || '') }}</div>
        </div>

        <div class="field">
          <label>금액</label>
          <input type="number" min="0" v-model.number="editAmount" class="input" />
        </div>

        <div class="actions">
          <button class="btn ghost" @click="closeEdit">취소</button>
          <button class="btn primary" @click="emitEdit">저장</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref, computed, watch } from "vue";
import { useChallengeDepositStore } from "@/stores/challengeDeposit";
import { storeToRefs } from "pinia";

const props = defineProps({
  challengeId: { type: Number, required: true },
});

const emit = defineEmits(["edit-deposit"]);

const depositStore = useChallengeDepositStore();
const { depositList } = storeToRefs(depositStore);

onMounted(async () => {
  await depositStore.getDepositList(props.challengeId);
});

/* ---- format ---- */
const formatDate = (dateTime) => (dateTime ? dateTime.split("T")[0] : "");
const formatAmount = (amount) => Number(amount).toLocaleString() + "원";

/* ---- modal edit ---- */
const isEditOpen = ref(false);
const selectedDeposit = ref(null);
const editAmount = ref(0);

const openEdit = (deposit) => {
  selectedDeposit.value = deposit;
  editAmount.value = Number(deposit.amount ?? 0);
  isEditOpen.value = true;
};

const closeEdit = () => {
  isEditOpen.value = false;
  selectedDeposit.value = null;
  editAmount.value = 0;
};

const emitEdit = () => {
  if (!selectedDeposit.value) return;
  if (editAmount.value == null || editAmount.value < 0) {
    alert("금액을 올바르게 입력해 주세요.");
    return;
  }

  emit("edit-deposit", {
    depositId: selectedDeposit.value.id,
    amount: editAmount.value,
  });

  closeEdit();
};

/* ---- pagination ---- */
const pageSize = 8;          // ✅ 한 페이지에 보여줄 행 개수 (원하면 5/10 등으로 변경)
const pageGroupSize = 5;     // ✅ 페이지 버튼은 항상 5개씩

const currentPage = ref(1);

const totalPages = computed(() =>
  Math.max(1, Math.ceil(depositList.value.length / pageSize))
);

const pagedDeposits = computed(() => {
  const start = (currentPage.value - 1) * pageSize;
  return depositList.value.slice(start, start + pageSize);
});

const currentGroupStart = computed(() => {
  return Math.floor((currentPage.value - 1) / pageGroupSize) * pageGroupSize + 1;
});

const visiblePages = computed(() => {
  const start = currentGroupStart.value;
  const end = Math.min(totalPages.value, start + pageGroupSize - 1);
  const pages = [];
  for (let p = start; p <= end; p++) pages.push(p);
  return pages;
});

const goPage = (p) => {
  if (p < 1 || p > totalPages.value) return;
  currentPage.value = p;
};

/* 데이터가 갱신되면 현재 페이지가 범위를 벗어나지 않게 */
watch(
  () => depositList.value.length,
  () => {
    if (currentPage.value > totalPages.value) currentPage.value = totalPages.value;
    if (currentPage.value < 1) currentPage.value = 1;
  }
);
</script>

<style scoped>
.deposit-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 8px;
  font-size: 15px;
}

.deposit-table th,
.deposit-table td {
  border-bottom: 1px solid #d1d1d1;
  padding: 6px 8px;
  text-align: center;
}

.deposit-table th {
  font-weight: 600;
  border-bottom: 2px solid #0063f8;
}

.deposit-list h4 {
  font-size: 17px;
  color: #0063f8;
}

.clickable-row {
  cursor: pointer;
}
.clickable-row:hover {
  background: #f4f8ff;
}

/* ✅ pagination */
.pagination {
  margin-top: 12px;
  display: flex;
  justify-content: center;
  gap: 6px;
  flex-wrap: wrap;
}

.page-btn {
  min-width: 34px;
  height: 34px;
  padding: 0 10px;
  border-radius: 999px;
  border: 1px solid #d7def0;
  background: #ededed;
  color: #0063f8;
  font-weight: 700;
  cursor: pointer;
}

.page-btn.active {
  background: #0063f8;
  border-color: #0063f8;
  color: #fff;
}

.page-btn:disabled {
  opacity: 0.45;
  cursor: not-allowed;
}

/* modal */
.modal-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.35);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 16px;
  z-index: 9999;
}

.modal {
  width: min(420px, 100%);
  max-width: 100%;
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  box-sizing: border-box;
  overflow: hidden;
}

.field {
  margin: 10px 0;
}

.field label {
  display: block;
  font-size: 13px;
  color: #666;
  margin-bottom: 4px;
}

.value {
  font-size: 14px;
}

.input {
  width: 100%;
  max-width: 100%;
  box-sizing: border-box;
  padding: 10px;
  border: 1px solid #d1d1d1;
  border-radius: 8px;
}

.actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 14px;
}

.btn {
  padding: 9px 12px;
  border-radius: 999px;
  border: 1px solid transparent;
  font-weight: 700;
}

.btn.ghost {
  background: #fff;
  border-color: #d1d1d1;
}

.btn.primary {
  background: #0063f8;
  color: #ededed;
}

.btn.primary:disabled {
  opacity: 0.6;
}

/* 모바일 */
@media (max-width: 480px) {
  .page-btn {
    min-width: 30px;
    height: 30px;
  }
}
</style>
