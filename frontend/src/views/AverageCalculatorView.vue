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
        <span class="search-icon">🔍</span> 검색 결과가 없습니다.
        <strong>'{{ submitted.keyword }}'</strong> 대신 AI가 🤖 <strong>'{{ items[0].menu }}'</strong>를 찾았어요!
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
              <td><span class="ai-badge">AI가 확인한 실시간 평균</span></td>
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
  if (user > avg * 1.1) return "비싼 소비예요 😥";
  if (user < avg * 0.9) return "아주 합리적인 소비예요 👍";
  return "적정한 소비예요 🙂";
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
/* 전체 배경 */
.avg-container {
  max-width: 1000px;
  margin: 40px auto;
  padding: 0 20px;
  color: #040000;
}

.title {
  color: #0063f8;
  font-weight: 800;
  font-size: 28px;
  margin-bottom: 40px;
}

/* 상단 입력폼 */
.form-section {
  display: flex;
  justify-content: space-between;
  gap: 40px;
  margin-bottom: 30px;
}

.input-group {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.input-group label {
  color: #0063f8;
  font-weight: 700;
  font-size: 14px;
  margin-bottom: 10px;
}

/* 입력창들 */
.underline-input {
  border: none;
  outline: none;
  background: transparent;
  border-bottom: 2px solid #0063f8;
  padding: 10px 0;
  font-size: 16px;
  font-weight: 600;
  width: 100%;
  transition: border-color 0.2s;
}

.underline-input:focus {
  border-bottom: 2px solid #0046b3;
}

.price-input-wrapper {
  display: flex;
  align-items: center;
  border-bottom: 2px solid #0063f8;
}

.price-input-wrapper .underline-input {
  border-bottom: none;
  flex: 1;
}

.currency {
  font-weight: 600;
  color: #040000;
  padding-left: 5px;
}

/* 검색 버튼 */
.btn-wrapper {
  display: flex;
  justify-content: center;
  margin: 40px 0;
}

.btn-search {
  background-color: #0063f8;
  color: #ffffff;
  border: none;
  padding: 12px 60px;
  border-radius: 30px;
  font-size: 16px;
  font-weight: 700;
  cursor: pointer;
  transition: all 0.2s ease-in-out;
}

.btn-search:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0, 99, 248, 0.3);
}

.btn-search:disabled {
  background-color: #a0c4ff;
  cursor: not-allowed;
}

/* 결과 카드 및 요약 */
.result-card {
  background-color: #f1f5f9;
  padding: 30px;
  border-radius: 20px;
  animation: fadeIn 0.5s ease-out;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.search-summary {
  margin-bottom: 25px;
}

.search-summary p {
  font-size: 18px;
  font-weight: 700;
  color: #0063f8;
  margin: 8px 0;
}

.search-summary .highlight {
  color: #040000;
  margin-left: 10px;
}

/* AI 매칭 알림 배너 */
.ai-notice-banner {
  background-color: #ffffff;
  padding: 15px 25px;
  border-radius: 12px;
  margin-bottom: 20px;
  font-size: 15px;
  border-left: 5px solid #0063f8;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
  display: flex;
  align-items: center;
  gap: 10px;
}

/* 결과 테이블 */
.table-wrapper {
  background: #ffffff;
  border-radius: 15px;
  padding: 15px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.result-table {
  width: 100%;
  border-collapse: collapse;
}

.result-table th {
  padding: 15px;
  color: #0063f8;
  border-bottom: 2px solid #0063f8;
  font-size: 14px;
  text-align: center;
}

.result-table td {
  padding: 18px 15px;
  text-align: center;
  border-bottom: 1px solid #f0f0f0;
  font-size: 14px;
  font-weight: 600;
}

.menu-name {
  color: #040000;
  text-align: left !important;
  padding-left: 25px !important;
}

/* AI 실시간 평균 */
.ai-price-row {
  background-color: #f8fbff !important;
}

.ai-price-row td {
  border-bottom: none;
}

/* AI 배지 */
.ai-badge {
  color: #0063f8;
  font-weight: 800;
  background-color: #eef5ff;
  padding: 5px 12px;
  border-radius: 8px;
  font-size: 13px;
  display: inline-block;
  border: 1px solid #d0e3ff;
}

.gpt-price-text {
  color: #0063f8;
  font-weight: 800 !important;
  font-size: 15px;
}

.judge-text {
  font-weight: 800;
  font-size: 14px;
}

.judge-text.expensive {
  color: #f4863b;
}

/* 비싼 소비 - 주황 */
.judge-text.cheap {
  color: #44ad47;
}

/* 합리적 소비 - 초록 */
.judge-text.ok {
  color: #040000;
}

/* 적정 소비 - 블랙 */

/* 8. AI 쇼핑 팁 박스 (하단) */
.gpt-tip-box {
  margin-top: 25px;
  padding: 20px 25px;
  background-color: #ffffff;
  border-left: 6px solid #f4863b;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
}

.tip-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  color: #f4863b;
  font-weight: 800;
  font-size: 16px;
}

.tip-icon {
  margin-right: 8px;
  font-size: 18px;
}

.tip-content {
  color: #4b4b4b;
  font-size: 14px;
  line-height: 1.7;
  margin: 0;
}

.muted {
  text-align: center;
  color: #888;
  margin-top: 20px;
}
</style>
