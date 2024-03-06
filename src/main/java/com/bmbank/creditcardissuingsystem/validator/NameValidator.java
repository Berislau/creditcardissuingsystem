package com.bmbank.creditcardissuingsystem.validator;

import com.bmbank.creditcardissuingsystem.exception.InvalidUserName;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NameValidator {

    public void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new InvalidUserName("User name can't be null or blank");
        }
    }
}
