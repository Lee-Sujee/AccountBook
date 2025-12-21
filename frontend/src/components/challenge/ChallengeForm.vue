<template>
    <div>
        <form class="post-form" @submit.prevent="submit">
            <div class="form-group">
                <label for="startDate">시작일</label>
                <input id="startDate" type="date" v-model="startDate" />
            </div>
            <div class="form-group">
                <label for="endDate">종료일</label>
                <input id="endDate" type="date" v-model="endDate" />
            </div>
            <div class="form-group">
                <label for="target">목표금액</label>
                <input id="target" type="number" v-model="target" />
            </div>
            <div class="form-group">
                <label for="description">목적</label>
                <input id="description" type="text" v-model="description" />
            </div>
            <div class="form-actions">
                <button type="submit" class="submit-btn" :disabled="!isValid">등록</button>
            </div>
        </form>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { defineEmits } from 'vue';
const emit = defineEmits(["submit"]);

const props = defineProps({
  initialData: {
    type: Object,
    default: null
  }
})

const startDate = ref(props.initialData?.startDate ?? '')
const endDate = ref(props.initialData?.endDate ?? '')
const target = ref(props.initialData?.target ?? '')
const description = ref(props.initialData?.description ?? '')

const isValid = computed(() => {
    return description.value.trim() !== "" 
        && startDate.value !== "" && endDate.value !== ""
        && target.value > 0;
})

const submit = () => {
    if(!isValid.value){
        alert("챌린지 목적을 입력해주세요");
        return;
    }
    emit("submit", {
        startDate: startDate.value,
        endDate: endDate.value,
        target: target.value,
        description: description.value,
    })
}
</script>

<style scoped>
h2 {
  text-align: center;
  margin-bottom: 16px;
  font-size: 20px;
  font-weight: 600;
}

.post-form {
  max-width: 420px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  background-color: #ffffff;
}

.form-group {
  display: flex;
  flex-direction: column;
  margin-bottom: 14px;
}

.form-group label {
  margin-bottom: 6px;
  font-size: 14px;
  font-weight: 500;
  color: #333;
}

.form-group input {
  padding: 8px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.form-group input:focus {
  outline: none;
  border-color: #2196f3;
}

.form-actions {
  margin-top: 18px;
}

.submit-btn {
  width: 100%;
  padding: 10px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  font-size: 15px;
  font-weight: 500;
  cursor: pointer;
}

.submit-btn:disabled {
  background-color: #c8e6c9;
  cursor: not-allowed;
}

</style>