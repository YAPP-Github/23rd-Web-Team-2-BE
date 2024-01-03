package com.baro.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = {RequestException.class})
    ResponseEntity<String> handleBadRequestException(RequestException e) {
        log.warn("[handleRequestException throw RequestException : {}", e.getMessage());
        RequestExceptionType exceptionType = e.exceptionType();
        return ResponseEntity.status(exceptionType.httpStatus()).body(exceptionType.errorMessage());
    }

    @ExceptionHandler(value = {MaxUploadSizeExceededException.class})
    public ResponseEntity<String> fileSizeLimitExceeded(MaxUploadSizeExceededException e) {
        log.warn("[handleRequestException throw MaxUploadSizeExceededException : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File size limit exceeded");
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<String> handleException(HttpServletRequest request, Exception e) {
        log.error("[ERROR] Unexpected error occurred. FROM URI: {}, ", request.getRequestURI(), e);
        return ResponseEntity.internalServerError().body("Unknown error occurred. Please try later");
    }
}
