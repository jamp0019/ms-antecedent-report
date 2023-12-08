package com.invexdijin.msantecedentreport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsAntecedentReportApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsAntecedentReportApplication.class, args);
	}

}
