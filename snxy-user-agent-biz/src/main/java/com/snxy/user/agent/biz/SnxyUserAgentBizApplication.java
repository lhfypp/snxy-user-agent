package com.snxy.user.agent.biz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.snxy.user.agent.biz","com.snxy.user.agent.dao"})
public class SnxyUserAgentBizApplication {

	public static void main(String[] args) {
		SpringApplication.run(SnxyUserAgentBizApplication.class, args);
	}
}
