<template>
  <div class="avg-container">
    <h2>평균계산기</h2>

    <div class="form-box">
      <label>
        상품명(키워드)
        <input v-model="keyword" placeholder="예: 호떡 / 비타500" />
      </label>

      <label>
        업태
        <select v-model="category">
          <option value="">선택하세요</option>
          <option value="대형마트">대형마트</option>
          <option value="백화점">백화점</option>
          <option value="기업형슈퍼">기업형슈퍼</option>
          <option value="편의점">편의점</option>
          <!-- 필요하면 DB 업태 더 추가 -->
        </select>
      </label>

      <label>
        내가 쓴 금액
        <input type="number" v-model.number="userPrice" min="0" />
      </label>

      <button @click="search" :disabled="loading">
        {{ loading ? '검색 중...' : '검색' }}
      </button>
    </div>

    <!-- 검색 버튼을 눌렀을 때만 결과 영역 표시 -->
    <div v-if="searched" class="result-card">
      <div class="summary">
        <p><strong>검색 키워드:</strong> {{ submitted.keyword }}</p>
        <p><strong>업태:</strong> {{ submitted.category }}</p>
        <p><strong>내가 쓴 금액:</strong> {{ submitted.userPrice.toLocaleString() }}원</p>
      </div>

      <div v-if="filteredItems.length">
        <table class="result-table">
          <thead>
            <tr>
              <th>상품명</th>
              <th>업태</th>
              <th>조사일</th>
              <th>평균가</th>
              <th>판정</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="row in filteredItems" :key="row.menu + '|' + row.category">
              <td>{{ row.menu }}</td>
              <td>{{ row.category }}</td>
              <td>{{ row.surveyDate }}</td>
              <td>{{ Number(row.price).toLocaleString() }}원</td>
              <td>
                <span class="judge" :class="judgeClass(Number(row.price))">
                  {{ judgeText(Number(row.price)) }}
                </span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <p v-else class="muted">검색 결과가 없습니다.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import instance from '@/api/axiosInstance'

const keyword = ref('')
const category = ref('')
const userPrice = ref(0)

const loading = ref(false)

//검색 버튼 누른 뒤에만 결과 영역 표시
const searched = ref(false)

//서버에서 받은 원본 리스트
const items = ref([])

//"검색 버튼을 누른 순간"의 값 스냅샷(이 값으로만 결과 영역 표기)
const submitted = ref({
  keyword: '',
  category: '',
  userPrice: 0,
})

//입력값이 바뀌면 결과 숨기기(검색 눌러야만 다시 뜸)
watch([keyword, category, userPrice], () => {
  searched.value = false
})

//프론트에서 업태 필터링
const filteredItems = computed(() => {
  if (!Array.isArray(items.value)) return []
  return items.value.filter((row) => row.category === submitted.value.category)
})

const search = async () => {
  const k = keyword.value.trim()
  const c = category.value
  const up = userPrice.value

  if (!k) {
    alert('상품명(키워드)을 입력하세요')
    return
  }
  if (!c) {
    alert('업태를 선택하세요')
    return
  }
  if (up <= 0) {
    alert('내가 쓴 금액을 입력하세요')
    return
  }

  loading.value = true
  items.value = [] // 이전 결과가 남아 보이지 않게 비우기

  try {
    //검색 버튼 누른 시점의 값 저장
    submitted.value = { keyword: k, category: c, userPrice: up }

    //keyword로만 검색
    const res = await instance.get('/api/v1/comparison/latest/search', {
      params: { keyword: k },
    })

    items.value = Array.isArray(res.data) ? res.data : []

    //결과 표시
    searched.value = true
  } catch (e) {
    console.error(e)
    searched.value = false
    alert('검색 중 오류가 발생했습니다.')
  } finally {
    loading.value = false
  }
}

const judgeText = (avgPrice) => {
  const up = submitted.value.userPrice
  if (up > avgPrice * 1.1) return '비싼 소비예요 😥'
  if (up < avgPrice * 0.9) return '아주 합리적인 소비예요 👍'
  return '적정한 소비예요 🙂'
}

const judgeClass = (avgPrice) => {
  const up = submitted.value.userPrice
  if (up > avgPrice * 1.1) return 'expensive'
  if (up < avgPrice * 0.9) return 'cheap'
  return 'ok'
}
</script>

<style scoped>
.avg-container {
  max-width: 900px;
  margin: 40px auto;
}

.form-box {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 24px;
}

input,
select {
  padding: 8px;
  margin-top: 4px;
}

button {
  padding: 10px;
  background: #1f8a4c;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

button:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.result-card {
  background: #f8fafc;
  padding: 16px;
  border-radius: 12px;
}

.summary {
  display: grid;
  gap: 6px;
  margin-bottom: 12px;
}

.result-table {
  width: 100%;
  border-collapse: collapse;
}

.result-table th,
.result-table td {
  border-bottom: 1px solid #e5e7eb;
  padding: 10px;
  text-align: left;
  vertical-align: middle;
}

.judge {
  font-weight: bold;
}

.judge.cheap {
  color: #1f8a4c;
}

.judge.ok {
  color: #0f172a;
}

.judge.expensive {
  color: #d9534f;
}

.muted {
  color: #64748b;
  margin-top: 8px;
}
</style>
