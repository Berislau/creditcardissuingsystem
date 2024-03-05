package com.bmbank.creditcardissuingsystem.exception;

public class FileAlreadyExistsException extends RuntimeException {
  public FileAlreadyExistsException(String message) {
    super(message);
  }
}
