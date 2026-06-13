package com.hk.demo.core.trace;

import org.slf4j.MDC;

/**
 * TraceId 上下文工具类，用于在日志链路中透传请求标识。
 */
public final class TraceContext {

    public static final String TRACE_ID_KEY = "traceId";
    public static final String TRACE_ID_HEADER = "X-Trace-Id";

    private TraceContext() {
    }

    /**
     * 写入当前线程的 TraceId。
     *
     * @param traceId 请求追踪标识
     */
    public static void setTraceId(String traceId) {
        MDC.put(TRACE_ID_KEY, traceId);
    }

    /**
     * 读取当前线程中的 TraceId。
     *
     * @return traceId
     */
    public static String getTraceId() {
        return MDC.get(TRACE_ID_KEY);
    }

    /**
     * 清理当前线程中的 TraceId。
     */
    public static void clear() {
        MDC.remove(TRACE_ID_KEY);
    }
}
