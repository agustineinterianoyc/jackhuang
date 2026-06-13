package com.hk.demo.core.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 文档基础配置。
 */
@Configuration
public class OpenApiConfig {

    /**
     * 定义项目的 Swagger 基础信息。
     *
     * @return OpenAPI 配置对象
     */
    @Bean
    public OpenAPI baseOpenApi() {
        return new OpenAPI().info(new Info()
            .title("Aldemo API")
            .description("公司培训项目（aldemo）后端接口文档")
            .version("v1.0.0")
            .contact(new Contact().name("aldemo")));
    }
}
