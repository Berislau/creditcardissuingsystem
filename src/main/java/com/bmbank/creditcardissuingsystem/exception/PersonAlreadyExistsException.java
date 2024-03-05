package com.bmbank.creditcardissuingsystem.exception;

public class PersonAlreadyExistsException extends RuntimeException {
  public PersonAlreadyExistsException(String message) {
    super(message);
  }
}
