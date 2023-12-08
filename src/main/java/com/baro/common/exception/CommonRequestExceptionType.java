package com.baro.common.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CommonRequestExceptionType implements RequestExceptionType {
    METHOD_ARGUMENT_EXCEPTION("CE01", "Method argument not valid", HttpStatus.BAD_REQUEST),
    MISSING_PARAMETER_EXCEPTION("CE02", "Request parameter is empty", HttpStatus.BAD_REQUEST),
    ;

    private final String exceptionCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

    @Override
    public String exceptionCode() {
        return exceptionCode;
    }

    @Override
    public String errorMessage() {
        return errorMessage;
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }
}
