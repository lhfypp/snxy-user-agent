package com.snxy.user.agent.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringCloudApplication
@ComponentScan({"com.snxy.user.agent.web","com.snxy.user.agent.biz","com.snxy.user.agent.dao"})
public class SnxyUserAgentWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(SnxyUserAgentWebApplication.class, args);
	}
}
