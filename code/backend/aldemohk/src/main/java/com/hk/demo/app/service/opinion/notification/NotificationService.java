package com.hk.demo.app.service.opinion.notification;

/**
 * 通知服务接口 — 统一发送系统通知。
 * 当前实现为本地文件日志，后续可替换为消息队列/推送服务。
 */
public interface NotificationService {

    /**
     * 发送通知。
     *
     * @param targetType 目标类型（UNIT/DEPT）
     * @param targetId   目标 ID
     * @param title      通知标题
     * @param content    通知内容
     */
    void send(String targetType, Long targetId, String title, String content);
}
