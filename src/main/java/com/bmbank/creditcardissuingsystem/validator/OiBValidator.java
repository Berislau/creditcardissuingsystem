package com.bmbank.creditcardissuingsystem.validator;

import com.bmbank.creditcardissuingsystem.exception.InvalidOibException;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OiBValidator {

    public void validateOiB(String oib) {
        if (oib == null || !oib.matches("\\d{11}")) {
            throw new InvalidOibException(
                    "OiB must be a numeric value and exactly 11 digits long.");
        }

        int a = 10;
        for (int i = 0; i < 10; i++) {
            a = a + Integer.parseInt(oib.substring(i, i + 1));
            a = a % 10;
            if (a == 0) {
                a = 10;
            }
            a *= 2;
            a = a % 11;
        }
        int control = 11 - a;
        if (control == 10) {
            control = 0;
        }

        if (control != Integer.parseInt(oib.substring(10))) {
            throw new InvalidOibException("OiB is not valid.");
        }
    }
}
