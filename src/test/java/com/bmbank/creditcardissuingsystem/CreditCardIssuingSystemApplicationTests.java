package com.bmbank.creditcardissuingsystem;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class CreditCardIssuingSystemApplicationTests {

    @Autowired private ApplicationContext context;

    @Test
    void contextLoads() {
        assertNotNull(context);
    }
}
