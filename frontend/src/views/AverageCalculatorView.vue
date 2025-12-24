<template>
  <div class="avg-container">
    <h2 class="title">평균계산기</h2>

    <div class="form-section">
      <div class="input-group">
        <label>상품명 (키워드)</label>
        <input v-model="keyword" placeholder="상품명을 입력하세요" class="underline-input" />
      </div>
      <div class="input-group">
        <label>업태</label>
        <select v-model="category" class="underline-input">
          <option value="">전체</option>
          <option value="대형마트">대형마트</option>
          <option value="백화점">백화점</option>
          <option value="기업형슈퍼">기업형슈퍼</option>
          <option value="편의점">편의점</option>
        </select>
      </div>
      <div class="input-group">
        <label>내가 쓴 금액</label>
        <div class="price-input-wrapper">
          <input type="number" v-model.number="userPrice" placeholder="0" class="underline-input" />
          <span class="currency">원</span>
        </div>
      </div>
    </div>

    <div class="btn-wrapper">
      <button @click="search" :disabled="loading" class="btn-search">
        {{ loading ? '검색 중...' : '검색' }}
      </button>
    </div>

    <div v-if="searched" class="result-card">
      <div class="search-summary">
        <p>검색 키워드 : <span class="highlight">{{ submitted.keyword }}</span></p>
        <p>업태 : <span class="highlight">{{ submitted.category || '전체' }}</span></p>
        <p>내가 쓴 금액 : <span class="highlight">{{ Number(submitted.userPrice).toLocaleString() }}원</span></p>
      </div>

      <div v-if="isAiMatched && items.length > 0" class="ai-notice-banner">
        검색 결과가 없습니다.
        <strong>'{{ submitted.keyword }}'</strong> 대신 AI가 🤖
        <strong>'{{ items[0].menu }}'</strong>를 찾았습니다!
      </div>

      <div v-if="filteredItems.length > 0 || gptPrice" class="table-wrapper">
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
            <tr v-for="(row, index) in filteredItems" :key="index">
              <td>{{ row.menu }}</td>
              <td>{{ row.category }}</td>
              <td>{{ row.surveyDate }}</td>
              <td>{{ Number(row.averagePrice).toLocaleString() }}원</td>
              <td>
                <span class="judge-text" :class="judgeClass(row.averagePrice)">
                  {{ row.result }}
                </span>
              </td>
            </tr>

            <tr v-if="gptPrice" class="ai-price-row">
              <td>{{ submitted.keyword }}</td>
              <td><span class="ai-badge">AI 평균 추정가</span></td>
              <td>실시간</td>
              <td>{{ gptPrice }}</td>
              <td>
                <span class="judge-text" :class="aiJudgeClass">
                  {{ aiJudgeResult }}
                </span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div v-if="gptTip" class="ai-tip-container">
        <div class="ai-avatar">🤖</div>
        <div class="ai-speech-bubble">
          <div class="tip-header">
            <strong>AI 쇼핑 어드바이저의 팁</strong>
          </div>
          <p class="tip-content">{{ gptTip }}</p>
        </div>
      </div>

      <p v-else-if="filteredItems.length === 0" class="muted">결과가 없습니다.</p>
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
const searched = ref(false)

const items = ref([])
const submitted = ref({})

const gptPrice = ref('')
const gptTip = ref('')

const isAiMatched = computed(() =>
  items.value.length > 0 &&
  !items.value[0].menu.includes(submitted.value.keyword)
)

const aiJudgeResult = computed(() => {
  if (!gptPrice.value) return '분석불가'
  const avg = parseInt(gptPrice.value.replace(/[^0-9]/g, ''))
  const user = submitted.value.userPrice
  if (user > avg * 1.1) return '비싼 소비예요'
  if (user < avg * 0.9) return '아주 합리적인 소비예요'
  return '적정한 소비예요'
})

const aiJudgeClass = computed(() => {
  if (!gptPrice.value) return 'ok'
  const avg = parseInt(gptPrice.value.replace(/[^0-9]/g, ''))
  const user = submitted.value.userPrice
  if (user > avg * 1.1) return 'expensive'
  if (user < avg * 0.9) return 'cheap'
  return 'ok'
})

const filteredItems = computed(() => {
  if (!items.value.length) return []
  if (!submitted.value.category || submitted.value.category === '전체') return items.value
  return items.value.filter(item => item.category === submitted.value.category)
})

watch([keyword, category, userPrice], () => {
  searched.value = false
})

const search = async () => {
  if (!keyword.value.trim() || userPrice.value <= 0) {
    alert('입력값을 확인해주세요.')
    return
  }

  loading.value = true
  gptPrice.value = ''
  gptTip.value = ''

  try {
    submitted.value = {
      keyword: keyword.value,
      category: category.value,
      userPrice: userPrice.value
    }

    const res = await instance.get('/api/v1/comparison/price', {
      params: {
        menu: keyword.value,
        category: '전체',
        userPrice: userPrice.value
      }
    })

    items.value = res.data.dbResults || []
    const fullAdvice = res.data.gptAdvice || ''

    if (fullAdvice.includes('[가격:')) {
      try {
        gptPrice.value = fullAdvice.split('[가격:')[1].split(']')[0].trim()
      } catch {
        gptPrice.value = ''
      }
    }

    if (fullAdvice.includes('[팁:]')) {
      gptTip.value = fullAdvice.split('[팁:]')[1].trim()
    } else {
      gptTip.value = fullAdvice
    }

    searched.value = true
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const judgeClass = (avg) => {
  const user = submitted.value.userPrice
  if (user > avg * 1.1) return 'expensive'
  if (user < avg * 0.9) return 'cheap'
  return 'ok'
}
</script>

<style scoped>
.avg-container {
  width: 100%;
  max-width: 1000px;
  margin: 0 auto;
  padding: 40px 20px;
  background-color: #ededed;
  min-height: 100vh;
  font-family: 'Pretendard', sans-serif;
  box-sizing: border-box;
}

.title {
  color: #0063f8;
  font-weight: 700;
  font-size: 22px;
  margin-bottom: 40px;
}

.form-section {
  display: flex;
  justify-content: center;
  gap: 60px;
  margin-bottom: 40px;
  flex-wrap: wrap;
}

.input-group label {
  color: #0063f8;
  font-weight: 700;
  font-size: 14px;
  display: block;
  margin-bottom: 15px;
}

.underline-input {
  border: none;
  outline: none;
  background: transparent;
  border-bottom: 2px solid #0063f8;
  padding: 8px 0;
  font-size: 16px;
  font-weight: 600;
  width: 200px;
}

.price-input-wrapper {
  display: flex;
  align-items: center;
  border-bottom: 2px solid #0063f8;
  width: 200px;
}

.price-input-wrapper .underline-input {
  border-bottom: none;
  width: 100%;
}

.btn-wrapper {
  display: flex;
  justify-content: center;
  margin-bottom: 60px;
}

.btn-search {
  background-color: #0063f8;
  color: #ffffff;
  border: none;
  padding: 12px 40px;
  border-radius: 25px;
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
}

.result-card {
  background-color: #ffffff;
  padding: 50px;
  border-radius: 30px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.03);
  max-width: 1000px;
  width: 100%;
  margin: 0 auto;
  box-sizing: border-box;
}

.search-summary {
  margin-bottom: 40px;
}

.search-summary p {
  font-size: 18px;
  font-weight: 700;
  color: #0063f8;
  margin: 12px 0;
}

.search-summary .highlight {
  color: #333;
  margin-left: 10px;
}

.table-wrapper {
  margin-bottom: 40px;
  overflow-x: auto;
}

.result-table {
  min-width: 600px;
  width: 100%;
  border-collapse: collapse;
}

.result-table th {
  padding: 15px 0;
  color: #0063f8;
  border-bottom: 2px solid #0063f8;
  font-size: 15px;
  font-weight: 700;
}

.result-table td {
  padding: 18px 0;
  text-align: center;
  border-bottom: 1px solid #ffffff;
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

.ai-price-row {
  background-color: #f8fbff;
}

.judge-text {
  font-weight: 800;
}

.judge-text.expensive {
  color: #f4863b;
}

.judge-text.cheap {
  color: #44ad47;
}

.judge-text.ok {
  color: #333;
}

/* AI 쇼핑팁 */
.ai-tip-container {
  display: flex;
  gap: 15px;
  margin-top: 30px;
  padding-top: 30px;
  border-top: 1px solid #f1f3f5;
  align-items: flex-start;
  white-space: pre-line;
}

.ai-avatar {
  width: 45px;
  height: 45px;
  background-color: #eef4ff;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 24px;
  flex-shrink: 0;
  border: 1px solid #d0e1ff;
}

.ai-speech-bubble {
  position: relative;
  background: #f8fbff;
  border: 1px solid #d0e1ff;
  border-radius: 15px;
  padding: 18px 20px;
  width: 100%;
}

.ai-speech-bubble::before {
  content: '';
  position: absolute;
  left: -10px;
  top: 15px;
  border-width: 5px 10px 5px 0;
  border-style: solid;
  border-color: transparent #d0e1ff transparent transparent;
}

.ai-speech-bubble::after {
  content: '';
  position: absolute;
  left: -8px;
  top: 15px;
  border-width: 5px 10px 5px 0;
  border-style: solid;
  border-color: transparent #f8fbff transparent transparent;
}

.tip-header {
  margin-bottom: 8px;
}

.tip-header strong {
  color: #0063f8;
  font-size: 15px;
  font-weight: 700;
}

.tip-content {
  margin: 0 !important;
  font-size: 14px;
  color: #4b5563;
  line-height: 1.6;
  word-break: keep-all;
}

.ai-badge {
  color: #0063f8;
  font-weight: 800;
}

@media (max-width: 768px) {
  .avg-container {
    padding: 20px;
  }

  .title {
    text-align: center;
  }

  .form-section {
    flex-direction: column;
    align-items: center;
    gap: 24px;
  }

  .input-group,
  .underline-input,
  .price-input-wrapper {
    width: 100%;
    max-width: 300px;
  }

  .result-card {
    padding: 24px;
  }

  .ai-tip-container {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  .ai-speech-bubble::before,
  .ai-speech-bubble::after {
    display: none;
  }
}
</style>