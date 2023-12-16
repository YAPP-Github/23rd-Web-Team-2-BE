package com.baro.common.exception;

import org.springframework.http.HttpStatus;

final class LoginRequestException extends RequestException{
    @Override
    RequestExceptionType exceptionType() {
        return new RequestExceptionType() {
            @Override
            public String exceptionCode() {
                return "EX01";
            }

            @Override
            public String errorMessage() {
                return "아이디 혹은 비밀번호를 입력해주세요.";
            }

            @Override
            public HttpStatus httpStatus() {
                return HttpStatus.BAD_REQUEST;
            }
        };
    }
}
