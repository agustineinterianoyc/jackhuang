package com.hk.demo.core.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvc 基础配置，当前主要用于统一注册跨域规则。
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final CorsProperties corsProperties;

    public WebMvcConfig(CorsProperties corsProperties) {
        this.corsProperties = corsProperties;
    }

    /**
     * 为所有接口注册跨域配置，便于前后端本地联调。
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOriginPatterns(corsProperties.getAllowedOriginPatterns().toArray(String[]::new))
            .allowedMethods(corsProperties.getAllowedMethods().toArray(String[]::new))
            .allowedHeaders(corsProperties.getAllowedHeaders().toArray(String[]::new))
            .allowCredentials(corsProperties.isAllowCredentials());
    }
}
