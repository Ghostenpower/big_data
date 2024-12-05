// tushareAPI.js
import apiClient from './index1';

// 获取指数每日行情数据
const getIndexDailyData = (params) => {
  const data = {
    api_name: 'index_daily',  // 接口名称
    params: params,  // 请求参数
    fields: [
      'ts_code',
      'trade_date',
      'close',
      'open',
      'high',
      'low',
      'pre_close',
      'change',
      'pct_chg',
      'vol',
      'amount',
    ],  // 请求返回的字段
  };

  return apiClient.post('/api/wctapi/apis/index_daily', data);  // 使用 apiClient 发送请求，URL 留空，已经在 apiClient 中配置了 baseURL
};

export { getIndexDailyData };
