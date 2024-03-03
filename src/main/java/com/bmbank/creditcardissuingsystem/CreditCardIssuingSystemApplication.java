package com.bmbank.creditcardissuingsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.bmbank.creditcardissuingsystem"})
public class CreditCardIssuingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CreditCardIssuingSystemApplication.class, args);
    }
}
