package com.hk.demo.app.service.opinion.notification;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 本地文件通知实现。
 * 生产环境可替换为消息队列/企业微信/钉钉等通道。
 */
@Service
public class LocalNotificationService implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(LocalNotificationService.class);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Path notifyDir;

    public LocalNotificationService(@Value("${app.notify.storage-dir:./data/notifications}") String dir) {
        this.notifyDir = Paths.get(dir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(notifyDir);
        } catch (IOException e) {
            throw new RuntimeException("无法创建通知存储目录", e);
        }
    }

    @Override
    public void send(String targetType, Long targetId, String title, String content) {
        String line = String.format("[%s] %s -> %s-%d | %s | %s%n",
            FMT.format(LocalDateTime.now()), targetType, targetType, targetId, title, content);
        log.info("[notify] {}", line.trim());
        try {
            Path file = notifyDir.resolve("notifications.log");
            Files.writeString(file, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            log.warn("[notify] write failed", e);
        }
    }
}
