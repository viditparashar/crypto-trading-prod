package com.crypto_prod.trading_prod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class TradingProdApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradingProdApplication.class, args);
	}

}
