package com.ciklum.test.github.consumer.exception;

import com.ciklum.test.github.consumer.dto.ErrorRS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    public static final int INVALID_ACCEPT_HEADER_FORMAT = 406;
    public static final int INTERNAL_SERVER_ERROR_STATUS = 500;

    public static final String NOT_FOUND_USER = "Not found github user data, please enter valid username";
    public static final String UNSUPPORTED_ACCEPT_HEADER = "Unsupported 'Accept' header: 'application/xml'. Must accept 'application/json'.";

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<ErrorRS> handleNotFoundException(HttpClientErrorException.NotFound ex) {
        log.warn("Problem occurs during invoke remote service", ex);
        ErrorRS errorDto = new ErrorRS();
        errorDto.setStatus(ex.getStatusCode().value());
        errorDto.setMessage(NOT_FOUND_USER);
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRS> handleException(Exception ex) {
        log.warn("Undefined error", ex);
        ErrorRS errorDto = new ErrorRS();
        errorDto.setStatus(INTERNAL_SERVER_ERROR_STATUS);
        errorDto.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
