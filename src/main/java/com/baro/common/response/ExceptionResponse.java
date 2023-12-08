package com.baro.common.response;

import com.baro.common.exception.RequestExceptionType;

public class ExceptionResponse<T extends RequestExceptionType> {

    private final String exceptionCode;
    private final String errorMessage;

    public ExceptionResponse(final T exceptionType) {
        this.exceptionCode = exceptionType.exceptionCode();
        this.errorMessage = exceptionType.errorMessage();
    }

    public String exceptionCode() {
        return exceptionCode;
    }

    public String errorMessage() {
        return errorMessage;
    }
}
