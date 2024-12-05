import axios from 'axios';

// 创建一个 Axios 实例
const axiosInstance = axios.create({
    baseURL: 'http://localhost:9090',  // API 的基础 URL
    timeout: 10000,  // 请求超时设置
    headers: {
        'Content-Type': 'application/json',  // 请求体的类型
    },
});

// 请求拦截器
axiosInstance.interceptors.request.use(
    config => {
        // 你可以在这里加上公共的请求头或者其他请求配置
        // 例如，添加认证 token
        const token = localStorage.getItem('authToken');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

// 响应拦截器
axiosInstance.interceptors.response.use(
    response => {
        return response.data;  // 只返回响应的数据部分
    },
    error => {
        // 错误处理，比如全局错误提示
        console.error('请求错误:', error.response);
        return Promise.reject(error);
    }
);

export default axiosInstance;
