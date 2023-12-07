package com.baro.common.exception;

abstract class RequestException extends RuntimeException{
    abstract RequestExceptionType exceptionType();
}
