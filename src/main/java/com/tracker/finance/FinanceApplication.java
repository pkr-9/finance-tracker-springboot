package com.tracker.finance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.context.annotation.ComponentScan;

// @SpringBootApplication(scanBasePackages = "com.tracker.finance")
// @ComponentScan(basePackages = "com.tracker.finance")
@SpringBootApplication
public class FinanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinanceApplication.class, args);
    }
}
