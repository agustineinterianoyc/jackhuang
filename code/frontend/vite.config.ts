import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// 后端代理目标：
// - 本机直接 npm run dev   -> 默认 http://127.0.0.1:8080
// - Docker 容器内运行       -> 通过 docker-compose 设置 VITE_DEV_PROXY_TARGET=http://aldemohk-backend:8080
const proxyTarget = process.env.VITE_DEV_PROXY_TARGET || 'http://127.0.0.1:8080'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  server: {
    host: '0.0.0.0',
    proxy: {
      '/api': {
        target: proxyTarget,
        changeOrigin: true,
      },
    },
  },
})
