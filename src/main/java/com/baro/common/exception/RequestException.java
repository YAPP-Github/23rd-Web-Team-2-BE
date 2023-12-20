package com.baro.common.exception;

public abstract class RequestException extends RuntimeException {

    public abstract RequestExceptionType exceptionType();
}
