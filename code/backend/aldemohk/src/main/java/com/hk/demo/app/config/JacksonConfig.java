package com.hk.demo.app.config;

import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;
import tools.jackson.databind.module.SimpleModule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 全局 Jackson 配置：统一 LocalDateTime 序列化 / 反序列化格式。
 *
 * 序列化：yyyy-MM-dd HH:mm:ss
 * 反序列化：兼容 yyyy-MM-dd HH:mm:ss
 */
@Configuration
public class JacksonConfig {

    private static final String PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(PATTERN);

    @Bean
    public JsonMapperBuilderCustomizer dateTimeJsonMapperCustomizer() {
        return builder -> {
            SimpleModule module = new SimpleModule("opinion-datetime");
            module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(FORMATTER));
            module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(FORMATTER));
            builder.addModule(module);
        };
    }
}
