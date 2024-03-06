package com.bmbank.creditcardissuingsystem.validator;

import com.bmbank.creditcardissuingsystem.exception.IllegalStatusIdException;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatusIdValidator {

    public void validateStatusId(long statusId) {
        if (statusId < 1 || statusId > 2) {
            throw new IllegalStatusIdException("StatusId must be 1 or 2");
        }
    }
}
