package com.baro.common.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CommonRequestExceptionType implements RequestExceptionType {

    MISSING_PARAMETER_EXCEPTION("Request parameter is empty", HttpStatus.BAD_REQUEST),
    ;

    private final String errorMessage;
    private final HttpStatus httpStatus;

    @Override
    public String errorMessage() {
        return errorMessage;
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }
}