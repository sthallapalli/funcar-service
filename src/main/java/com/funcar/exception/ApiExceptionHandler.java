package com.funcar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = RuntimeException.class)
    protected ResponseEntity<Object> handleInternalServerException(
            Exception ex) {
        ApiExceptionMessage apiError = new ApiExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), ex);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        ApiExceptionMessage apiError = new ApiExceptionMessage(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex) {
        ApiExceptionMessage apiError = new ApiExceptionMessage(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiExceptionMessage apiException) {
        return new ResponseEntity<>(apiException, apiException.getStatus());
    }

}
