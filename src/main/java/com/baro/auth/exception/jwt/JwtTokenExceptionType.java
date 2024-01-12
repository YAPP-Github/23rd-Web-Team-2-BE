package com.baro.auth.exception.jwt;

import com.baro.common.exception.RequestExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum JwtTokenExceptionType implements RequestExceptionType {

    EXPIRED_JWT_TOKEN("JW01", "만료된 토큰 입니다.", HttpStatus.BAD_REQUEST),
    INVALID_JWT_TOKEN("JW02", "유효하지 않은 토큰 입니다.", HttpStatus.BAD_REQUEST),
    NOT_BEARER_SCHEME("JW03", "Bearer 타입이 아닙니다.", HttpStatus.BAD_REQUEST),
    AUTHORIZATION_NULL("JW04", "Authorization 헤더가 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_NULL("JW05", "Refresh Token이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

    @Override
    public String errorCode() {
        return errorCode;
    }

    @Override
    public String errorMessage() {
        return errorMessage;
    }

    @Override
    public HttpStatus httpStatus() {
        return httpStatus;
    }
}
