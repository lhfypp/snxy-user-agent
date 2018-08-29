package com.snxy.user.agent.dao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.snxy.user.agent.dao.mapper")
public class SnxyUserAgentDaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SnxyUserAgentDaoApplication.class, args);
	}
}
