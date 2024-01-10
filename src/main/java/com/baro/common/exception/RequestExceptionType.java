package com.baro.common.exception;

import org.springframework.http.HttpStatus;

public interface RequestExceptionType {

    String errorCode();

    String errorMessage();

    HttpStatus httpStatus();
}
