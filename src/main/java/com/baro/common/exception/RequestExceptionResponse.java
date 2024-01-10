package com.baro.common.exception;

public record RequestExceptionResponse(
        String errorCode,
        String errorMessage
) {

    public static RequestExceptionResponse from(RequestExceptionType requestExceptionType) {
        return new RequestExceptionResponse(
                requestExceptionType.errorCode(),
                requestExceptionType.errorMessage()
        );
    }
}
