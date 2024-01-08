package com.baro.auth.exception.jwt;

import com.baro.common.exception.RequestExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum JwtTokenExceptionType implements RequestExceptionType {

    EXPIRED_JWT_TOKEN("만료된 토큰 입니다.", HttpStatus.BAD_REQUEST),
    INVALID_JWT_TOKEN("유효하지 않은 토큰 입니다.", HttpStatus.BAD_REQUEST),
    NOT_BEARER_SCHEME("Bearer 타입이 아닙니다.", HttpStatus.BAD_REQUEST),
    AUTHORIZATION_NULL("Authorization 헤더가 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String errorMessage;
    private final HttpStatus httpStatus;

    @Override
    public String errorMessage() {
        return errorMessage;
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }
}
