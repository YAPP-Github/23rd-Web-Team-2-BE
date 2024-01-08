package com.baro.auth.exception.jwt;

import com.baro.common.exception.RequestException;
import com.baro.common.exception.RequestExceptionType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtTokenException extends RequestException {

    private final JwtTokenExceptionType jwtTokenExceptionType;

    @Override
    public RequestExceptionType exceptionType() {
        return jwtTokenExceptionType;
    }
}
