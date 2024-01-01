package com.baro.auth.exception.oauth;

import com.baro.common.exception.RequestException;
import com.baro.common.exception.RequestExceptionType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OAuthException extends RequestException {

    private final OAuthExceptionType exceptionType;

    @Override
    public RequestExceptionType exceptionType() {
        return exceptionType;
    }
}
