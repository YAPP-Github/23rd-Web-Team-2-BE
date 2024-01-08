package com.baro.auth.exception;

import com.baro.common.exception.RequestExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum AuthExceptionType implements RequestExceptionType {

    CLIENT_AND_TOKEN_IS_NOT_MATCH("클라이언트와 토큰이 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    IP_ADDRESS_DOES_NOT_EXIST("비정상적인 요청입니다.", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_DOES_NOT_EXIST("리프레시 토큰이 존재하지 않습니다.", HttpStatus.BAD_REQUEST),
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
