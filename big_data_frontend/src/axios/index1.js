// apiClient.js
import axios from 'axios';

// 创建一个 Axios 实例
const apiClient = axios.create({
  baseURL: '/',  // 基础 URL
  headers: {
    'Content-Type': 'application/json',  // 请求头，确保是 JSON 格式
  },
});

// 获取 Token（可以从本地存储、环境变量等地方获取）
const getToken = () => {
  return 'd8310278872ce5bfd1ec468fdfa6060f893c7d665a15805703d240d7';  // 示例：直接返回 Token 字符串
};

// 请求拦截器：将 token 自动添加到请求头
apiClient.interceptors.request.use(config => {
  const token = getToken();  // 获取 Token
  if (token) {
    config.headers['token'] = token;  // 将 token 添加到请求头
  }
  return config;
}, error => {
  return Promise.reject(error);
});

// 响应拦截器（可以统一处理错误等）
apiClient.interceptors.response.use(response => {
  return response;
}, error => {
  return Promise.reject(error);
});

// 导出 API 客户端
export default apiClient;
