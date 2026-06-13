package com.hk.demo.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.hk.demo")
@MapperScan("com.hk.demo.app.mapper")
public class AldemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AldemoApplication.class, args);
	}

}
