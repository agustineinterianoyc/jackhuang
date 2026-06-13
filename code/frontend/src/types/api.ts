export interface ApiResponse<T = any> {
  code: number
  message: string
  data: T
}

export interface PageParams {
  page: number
  pageSize: number
}

export interface PageResult<T> {
  records: T[]
  total: number
}

export class RequestError extends Error {
  code: number
  type: 'business' | 'http' | 'network' | 'timeout' | 'cancel'

  constructor(
    message: string,
    code: number = -1,
    type: RequestError['type'] = 'business',
  ) {
    super(message)
    this.name = 'RequestError'
    this.code = code
    this.type = type
  }
}
