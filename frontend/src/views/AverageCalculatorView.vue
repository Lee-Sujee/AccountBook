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
        <span class="search-icon"></span> 검색 결과가 없습니다.
        <strong>'{{ submitted.keyword }}'</strong> 대신 AI가 🤖 <strong>'{{ items[0].menu }}'</strong>를 찾았습니다!
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
              <td class="menu-name">{{ row.menu }}</td>
              <td>{{ row.category }}</td>
              <td>{{ row.surveyDate }}</td>
              <td class="price-text">{{ Number(row.averagePrice).toLocaleString() }}원</td>
              <td>
                <span class="judge-text" :class="judgeClass(row.averagePrice)">{{ row.result }}</span>
              </td>
            </tr>

            <tr v-if="gptPrice" class="ai-price-row">
              <td class="ai-menu-cell">{{ submitted.keyword }}</td>
              <td><span class="ai-badge">AI가 확인한 평균 추정가</span></td>
              <td class="ai-status">실시간</td>
              <td class="gpt-price-text">{{ gptPrice }}</td>
              <td>
                <span class="judge-text" :class="aiJudgeClass">{{ aiJudgeResult }}</span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div v-if="gptTip" class="gpt-tip-box">
        <div class="tip-header">
          <span class="tip-icon">💡</span>
          <strong>AI 쇼핑 팁</strong>
        </div>
        <p class="tip-content">{{ gptTip }}</p>
      </div>

      <p v-else-if="filteredItems.length === 0" class="muted">결과가 없습니다.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import instance from '@/api/axiosInstance'

const keyword = ref(''); const category = ref(''); const userPrice = ref(0);
const loading = ref(false); const searched = ref(false);
const items = ref([]); const submitted = ref({});
const gptPrice = ref(''); const gptTip = ref('');

const isAiMatched = computed(() => items.value.length > 0 && !items.value[0].menu.includes(submitted.value.keyword));

// AI 판정 결과 계산 (텍스트)
const aiJudgeResult = computed(() => {
  if (!gptPrice.value) return '분석불가';
  const avg = parseInt(gptPrice.value.replace(/[^0-9]/g, '')); // "5,500원" -> 5500
  const user = submitted.value.userPrice;
  if (user > avg * 1.1) return "비싼 소비예요";
  if (user < avg * 0.9) return "아주 합리적인 소비예요";
  return "적정한 소비예요";
});

// AI 판정 스타일 클래스
const aiJudgeClass = computed(() => {
  if (!gptPrice.value) return 'ok';
  const avg = parseInt(gptPrice.value.replace(/[^0-9]/g, ''));
  const user = submitted.value.userPrice;
  if (user > avg * 1.1) return 'expensive';
  if (user < avg * 0.9) return 'cheap';
  return 'ok';
});

const filteredItems = computed(() => {
  if (!items.value.length) return [];
  if (!submitted.value.category || submitted.value.category === '전체') return items.value;
  return items.value.filter(item => item.category === submitted.value.category);
});

watch([keyword, category, userPrice], () => { searched.value = false; });

const search = async () => {
  if (!keyword.value.trim() || userPrice.value <= 0) return alert('입력값을 확인해주세요.');
  loading.value = true;
  gptPrice.value = ''; gptTip.value = '';

  try {
    submitted.value = { keyword: keyword.value, category: category.value, userPrice: userPrice.value };
    const res = await instance.get('/api/v1/comparison/price', {
      params: { menu: keyword.value, category: '전체', userPrice: userPrice.value }
    });

    items.value = res.data.dbResults || [];
    const fullAdvice = res.data.gptAdvice || '';

    if (fullAdvice.includes('[가격:')) {
      gptPrice.value = fullAdvice.split('[가격:')[1].split(']')[0].trim();
      gptTip.value = fullAdvice.split('[팁:')[1].split(']')[0].trim();
    } else {
      gptTip.value = fullAdvice;
    }
    searched.value = true;
  } catch (e) { console.error(e); } finally { loading.value = false; }
};

const judgeClass = (avg) => {
  const up = submitted.value.userPrice;
  if (up > avg * 1.1) return 'expensive';
  if (up < avg * 0.9) return 'cheap';
  return 'ok';
};
</script>

<style scoped>
/* 전체 컨테이너 및 배경 설정 */
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
  font-size: 15px;
  margin-bottom: 40px;
  font-size: 22px;
}

/* 입력폼 */
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

/* 검색 버튼 */
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

/* 결과 */
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

/* AI 매칭 */
.ai-badge {
  font-size: 15px;
  color: #333;
  margin-bottom: 25px;
}

/* 결과 테이블 */
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

.ai-highlight-text {
  color: #0063f8;
  font-weight: 800;
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

.gpt-tip-box {
  border-top: 1px solid #f1f3f5;
  padding-top: 30px;
  display: flex;
  align-items: flex-start;
  gap: 10px;
}

.gpt-tip-box p {
  margin: 0;
  font-size: 14px;
  color: #555;
  line-height: 1.6;
}

.gpt-tip-box strong {
  color: #f4863b;
  white-space: nowrap;
}

.ai-badge {
  color: #0063f8;
  font-weight: 800;
}

/* --- 반응형 미디어 쿼리 추가 --- */
@media (max-width: 768px) {
  .avg-container {
    padding: 20px;
  }

  .title {
    font-size: 20px;
    margin-bottom: 24px;
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

  .search-summary p {
    font-size: 15px;
  }
}
</style>