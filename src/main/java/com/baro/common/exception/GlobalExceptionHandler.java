package com.baro.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = {RequestException.class})
    protected ResponseEntity<?> handleBadRequestException(RequestException e) {
        log.error("handleBadRequestException throw BadRequestException : {}", e.getMessage());
        return ApiResponse.badRequest(e.getMessage(), null);
    }

}
