package com.admin.ac.ding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages ={"com.admin.ac.ding.mapper"})
public class DingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(DingAppApplication.class, args);
	}
}
