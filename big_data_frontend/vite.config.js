import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  server: {
    proxy: {
      // 将所有请求以 /api 开头的 URL 转发到后端
      '/api': {
        target: 'https://api.tushare.pro',  // 你的后端 API 地址
        changeOrigin: true,  // 修改请求头中的 Origin 字段
        rewrite: (path) => path.replace(/^\/api/, ''),  // 可选，重写路径
      },
    },
  },
})
