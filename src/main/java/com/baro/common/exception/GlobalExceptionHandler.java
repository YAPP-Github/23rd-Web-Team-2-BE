package com.baro.common.exception;

import static com.baro.common.exception.CommonRequestExceptionType.MISSING_PARAMETER_EXCEPTION;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(value = {RequestException.class})
    ResponseEntity<RequestExceptionResponse> handleBadRequestException(RequestException e) {
        RequestExceptionType exceptionType = e.exceptionType();
        log.warn("RequestException: [{}] - {}", exceptionType.errorCode(), exceptionType.errorMessage());
        return ResponseEntity.status(exceptionType.httpStatus())
                .body(RequestExceptionResponse.from(exceptionType));
    }

    @ExceptionHandler(value = {MaxUploadSizeExceededException.class})
    public ResponseEntity<String> fileSizeLimitExceeded(MaxUploadSizeExceededException e) {
        log.warn("[handleRequestException throw MaxUploadSizeExceededException : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body("File size limit exceeded");
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    public ResponseEntity<RequestExceptionResponse> handleMissingRequestParameters(
            MissingServletRequestParameterException e) {
        log.warn("[handleRequestException throw MissingServletRequestParameterException : {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(RequestExceptionResponse.from(MISSING_PARAMETER_EXCEPTION));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<String> handleException(HttpServletRequest request, Exception e) {
        log.error("[ERROR] Unexpected error occurred. FROM URI: {}, ", request.getRequestURI(), e);
        return ResponseEntity.internalServerError()
                .body("Unknown error occurred. Please try later");
    }
}
