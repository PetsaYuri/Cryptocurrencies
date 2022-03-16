package com.testTask.cryptocurrency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class CryptocurrencyApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptocurrencyApplication.class, args);
	}

}
