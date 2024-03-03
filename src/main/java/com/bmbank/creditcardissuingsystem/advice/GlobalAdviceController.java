package com.bmbank.creditcardissuingsystem.advice;

import com.bmbank.creditcardissuingsystem.exception.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalAdviceController {

    @ExceptionHandler({
        FileAlreadyExistsException.class,
        InvalidStatusException.class,
        UserAlreadyExistsException.class,
        InvalidOibException.class,
        InvalidUserName.class,
        IllegalStatusIdException.class
    })
    public ResponseEntity<String> handleBadRequest(final RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({FileStorageException.class})
    public ResponseEntity<String> handleInternalErrorRequest(final RuntimeException e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
}
