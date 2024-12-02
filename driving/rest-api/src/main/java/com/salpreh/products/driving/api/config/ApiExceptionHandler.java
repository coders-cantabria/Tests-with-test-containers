package com.salpreh.products.driving.api.config;

import com.salpreh.products.application.exceptions.base.NotFoundException;
import com.salpreh.products.driving.api.models.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ApiErrorResponse> handleProductNotFoundException(NotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
      .body(toApiErrorResponse(ex));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleException(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(toApiErrorResponse(ex));
  }

  private ApiErrorResponse toApiErrorResponse(Throwable t) {
    return ApiErrorResponse.builder()
      .message(t.getMessage())
      .build();
  }
}
