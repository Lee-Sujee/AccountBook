<template>
  <div class="avg-container">
    <h2>평균계산기</h2>

    <div class="form-box">
      <label>상품명 <input v-model="keyword" placeholder="예: 퐁퐁" /></label>
      <label>업태
        <select v-model="category">
          <option value="">전체</option>
          <option value="대형마트">대형마트</option>
          <option value="백화점">백화점</option>
          <option value="기업형슈퍼">기업형슈퍼</option>
          <option value="편의점">편의점</option>
        </select>
      </label>
      <label>금액 <input type="number" v-model.number="userPrice" /></label>
      <button @click="search" :disabled="loading">{{ loading ? '검색 중...' : '검색' }}</button>
    </div>

    <div v-if="searched" class="result-card">
      <div v-if="isAiMatched && items.length > 0" class="ai-badge">
        🔍 검색 결과가 없습니다. <strong>'{{ submitted.keyword }}'</strong> 대신 AI가 🤖 <strong>'{{ items[0].menu }}'</strong>를
        찾았어요!
      </div>

      <div v-if="filteredItems.length > 0 || gptPrice">
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
              <td><span class="judge" :class="judgeClass(row.averagePrice)">{{ row.result }}</span></td>
            </tr>

            <tr v-if="gptPrice" class="ai-price-row">
              <td class="ai-menu-cell">{{ submitted.keyword }}</td>
              <td><span class="ai-tag">AI가 알아본 평균 가격</span></td>
              <td>실시간</td>
              <td class="gpt-price-text">{{ gptPrice }}</td>
              <td>
                <span class="judge" :class="aiJudgeClass">
                  {{ aiJudgeResult }}
                </span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div v-if="gptTip" class="gpt-tip-box">
        <p>💡 <strong>AI 쇼핑 팁:</strong> {{ gptTip }}</p>
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
.avg-container {
  max-width: 900px;
  margin: 40px auto;
  font-family: sans-serif;
}

.form-box {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 24px;
}

input,
select {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

button {
  padding: 12px;
  background: #1f8a4c;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-weight: bold;
}

.result-card {
  background: #f8fafc;
  padding: 20px;
  border-radius: 12px;
  border: 1px solid #e2e8f0;
}

.ai-badge {
  background-color: #eef2ff;
  border-left: 4px solid #6366f1;
  color: #4338ca;
  padding: 12px;
  margin-bottom: 15px;
  border-radius: 4px;
}

.result-table {
  width: 100%;
  border-collapse: collapse;
  background: white;
  border-radius: 8px;
  overflow: hidden;
}

.result-table th,
.result-table td {
  padding: 14px;
  border-bottom: 1px solid #eee;
  text-align: left;
}

.result-table th {
  background: #f1f5f9;
  color: #475569;
  font-size: 0.9rem;
}

/* 여기서부터 AI CSS */
.ai-price-row {
  background-color: #f0f9ff;
}

.ai-menu-cell {
  font-weight: 600;
  color: #1e40af;
}

.ai-tag {
  background: #3b82f6;
  color: white;
  padding: 3px 8px;
  border-radius: 4px;
  font-size: 0.75rem;
  font-weight: bold;
}

.gpt-price-text {
  font-weight: bold;
  color: #2563eb;
  font-size: 1.05rem;
}

.judge {
  font-weight: bold;
}

.judge.expensive {
  color: #d9534f;
}

.judge.cheap {
  color: #1f8a4c;
}

.judge.ok {
  color: #64748b;
}

/* GPT 팁 박스 스타일 */
.gpt-tip-box {
  margin-top: 20px;
  padding: 14px;
  background-color: #fff7ed;
  border-left: 4px solid #f97316;
  border-radius: 6px;
}

.gpt-tip-box p {
  margin: 0;
  color: #7c2d12;
  font-size: 0.95rem;
  line-height: 1.5;
}

.muted {
  color: #94a3b8;
  text-align: center;
  margin-top: 20px;
}
</style>
