package com.baro.auth.exception;

import com.baro.common.exception.RequestException;
import com.baro.common.exception.RequestExceptionType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthException extends RequestException {

    private final AuthExceptionType authExceptionType;

    @Override
    public RequestExceptionType exceptionType() {
        return authExceptionType;
    }
}
