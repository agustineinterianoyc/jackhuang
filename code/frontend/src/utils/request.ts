import axios, { AxiosError, type AxiosRequestConfig, type AxiosResponse } from 'axios'
import { Message } from '@arco-design/web-vue'
import type { ApiResponse } from '@/types/api'
import { RequestError } from '@/types/api'

const HTTP_MSG: Record<number, string> = {
  400: '请求参数有误',
  401: '登录已过期，请重新登录',
  403: '没有访问权限',
  404: '请求的资源不存在',
  500: '服务器异常，请稍后再试',
  502: '网关错误',
  503: '服务暂不可用',
}

const instance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL as string || '',
  timeout: 15000,
  headers: { 'Content-Type': 'application/json' },
})

instance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error),
)

instance.interceptors.response.use(
  (response: AxiosResponse<ApiResponse>) => {
    const { code, message } = response.data
    if (code !== 0 && code !== 200) {
      const err = new RequestError(message || '请求失败', code, 'business')
      Message.error(err.message)
      return Promise.reject(err)
    }
    return response
  },
  (error: AxiosError) => {
    if (axios.isCancel(error)) {
      return Promise.reject(new RequestError('请求已取消', -1, 'cancel'))
    }

    if (error.code === 'ECONNABORTED') {
      const err = new RequestError('请求超时', -1, 'timeout')
      Message.error(err.message)
      return Promise.reject(err)
    }

    if (!error.response) {
      const err = new RequestError('网络连接异常', -1, 'network')
      Message.error(err.message)
      return Promise.reject(err)
    }

    const status = error.response.status
    const msg = HTTP_MSG[status] || `请求失败 (${status})`

    if (status === 401) {
      localStorage.removeItem('token')
    }

    const err = new RequestError(msg, status, 'http')
    Message.error(err.message)
    return Promise.reject(err)
  },
)

async function request<T>(config: AxiosRequestConfig): Promise<T> {
  const response = await instance.request<ApiResponse<T>>(config)
  return response.data.data as T
}

export function get<T>(url: string, params?: Record<string, any>, config?: AxiosRequestConfig) {
  return request<T>({ method: 'GET', url, params, ...config })
}

export function post<T>(url: string, data?: any, config?: AxiosRequestConfig) {
  return request<T>({ method: 'POST', url, data, ...config })
}

export function put<T>(url: string, data?: any, config?: AxiosRequestConfig) {
  return request<T>({ method: 'PUT', url, data, ...config })
}

export function del<T>(url: string, config?: AxiosRequestConfig) {
  return request<T>({ method: 'DELETE', url, ...config })
}

export default instance
