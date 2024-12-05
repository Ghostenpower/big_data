import axiosInstance from './index';

export const getOneStockInfo = async (stockInfo) => {
  try {
    const response = await axiosInstance.post('/stock/getOneStockInfo',stockInfo);
    return response;  // 返回数据
  } catch (error) {
    // 错误处理可以根据需要进行拓展
    throw new Error('获取单个股票信息失败');
  }
};

export const getStockInfoByIndustry = async (stockInfo) => {
  try {
    const response = await axiosInstance.post('/stock/getStockInfoByIndustry',stockInfo);
    return response;  // 返回数据
  } catch (error) {
    // 错误处理可以根据需要进行拓展
    throw new Error('获取股票信息失败');
  }
};

export const getStockDailyData = async (stockInfo) => {
  try {
    const response = await axiosInstance.post(`/stock/getStockDailyData`,stockInfo);
    return response;
  } catch (error) {
    throw new Error(`获取股票日线数据 ${stockInfo} 失败`);
  }
};

export const getNewIndexData = async (stockInfo) => {
  try {
    const response = await axiosInstance.post(`/stock/getNewIndexData`,stockInfo);
    return response;
  } catch (error) {
    throw new Error(`获取指数日线数据 ${stockInfo} 失败`);
  }
};

export const getSectorInfo = async () => {
  try {
    const response = await axiosInstance.post(`/stock/getSectorInfo`);
    return response;
  } catch (error) {
    throw new Error(`获取指数日线数据 ${stockInfo} 失败`);
  }
};

export const getAllIndexData = async () => {
  try {
    const response = await axiosInstance.post(`/stock/getAllIndexData`);
    return response;
  } catch (error) {
    throw new Error(`获取指数日线数据 ${stockInfo} 失败`);
  }
};

export const getAllStockInfo = async () => {
  try {
    const response = await axiosInstance.post(`/stock/getAllStockInfo`);
    return response;
  } catch (error) {
    throw new Error(`获取指数日线数据 ${stockInfo} 失败`);
  }
};

// 你还可以封装更多的 API 请求
