package com.hk.demo.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.hk.demo")
@MapperScan("com.hk.demo.app.mapper")
@EnableScheduling
public class AldemohkApplication {

	public static void main(String[] args) {
		SpringApplication.run(AldemohkApplication.class, args);
	}

}
