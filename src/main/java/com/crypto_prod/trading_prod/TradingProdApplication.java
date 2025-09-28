package com.crypto_prod.trading_prod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.crypto_prod.trading_prod.repository")
@EntityScan(basePackages = "com.crypto_prod.trading_prod.model")
public class TradingProdApplication {
    public static void main(String[] args) {
        SpringApplication.run(TradingProdApplication.class, args);
    }
}
