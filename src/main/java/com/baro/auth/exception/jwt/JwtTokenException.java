package com.baro.auth.exception.jwt;

import com.baro.auth.exception.oauth.OAuthExceptionType;
import com.baro.common.exception.RequestException;
import com.baro.common.exception.RequestExceptionType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtTokenException extends RequestException {

    private final OAuthExceptionType oAuthExceptionType;

    @Override
    public RequestExceptionType exceptionType() {
        return oAuthExceptionType;
    }
}
