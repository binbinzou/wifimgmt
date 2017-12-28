package com.wifishared.wifimgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableAutoConfiguration
@EnableHystrix
@ComponentScan(basePackages = "com.wifishared")
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
