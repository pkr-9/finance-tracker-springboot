package com.tracker.finance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

// @SpringBootApplication(exclude = R2dbcAutoConfiguration.class)
@SpringBootApplication(scanBasePackages = "com.tracker.finance")
@ComponentScan(basePackages = "com.tracker.finance")
public class FinanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinanceApplication.class, args);
    }
}
