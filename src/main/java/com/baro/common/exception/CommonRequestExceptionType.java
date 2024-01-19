package com.baro.common.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum CommonRequestExceptionType implements RequestExceptionType {

    MISSING_PARAMETER_EXCEPTION("CM01", "Request parameter is empty", HttpStatus.BAD_REQUEST),
    INVALID_TYPE_REQUEST_EXCEPTION("CM02", "Request type is invalid", HttpStatus.BAD_REQUEST),
    ;

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

    @Override
    public String errorCode() {
        return errorCode;
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
