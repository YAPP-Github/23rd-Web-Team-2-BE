package com.baro.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = {RequestException.class})
    protected ResponseEntity<?> handleBadRequestException(RequestException e) {
        log.info("handleRequestException throw RequestException : {}", e.exceptionType().exceptionCode());
        return ExceptionResponse.badRequest(e.exceptionType());
    }

    class ExceptionResponse {

        static ResponseEntity<?> badRequest(RequestExceptionType exceptionType) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionType);
        }
    }
}
