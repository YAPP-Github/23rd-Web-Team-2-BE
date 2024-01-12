package com.baro.auth.exception;

import com.baro.common.exception.RequestException;
import com.baro.common.exception.RequestExceptionType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthException extends RequestException {

    private final AuthExceptionType exceptionType;

    @Override
    public RequestExceptionType exceptionType() {
        return exceptionType;
    }
}
