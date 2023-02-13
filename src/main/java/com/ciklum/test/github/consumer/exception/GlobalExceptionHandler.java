package com.ciklum.test.github.consumer.exception;

import com.ciklum.test.github.consumer.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    public static final String NOT_FOUND_USER = "Not found github user data, please enter valid username";
    public static final String UNSUPPORTED_ACCEPT_HEADER = "Unsupported 'Accept' header: 'application/xml'. Must accept 'application/json'.";

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(HttpClientErrorException.NotFound ex) {
        log.warn("Problem occurs during invoke remote service", ex);
        ErrorResponse errorDto = new ErrorResponse();
        errorDto.setStatus(ex.getStatusCode().value());
        errorDto.setMessage(NOT_FOUND_USER);
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.warn("Undefined error", ex);
        ErrorResponse errorDto = new ErrorResponse();
        errorDto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorDto.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
