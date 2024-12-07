package com.salpreh.products.application.exceptions.base;

public class ValidationException extends RuntimeException {

  public ValidationException(String message) {
    super(message);
  }
}
