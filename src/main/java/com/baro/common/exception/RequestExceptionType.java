package com.baro.common.exception;

import org.springframework.http.HttpStatus;

interface RequestExceptionType {

    String exceptionCode();

    String errorMessage();

    HttpStatus httpStatus();
}
