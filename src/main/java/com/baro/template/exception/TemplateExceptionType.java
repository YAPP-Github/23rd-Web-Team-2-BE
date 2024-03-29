package com.baro.template.exception;

import com.baro.common.exception.RequestExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum TemplateExceptionType implements RequestExceptionType {

    INVALID_CATEGORY("TE01", "존재하지 않는 카테고리입니다.", HttpStatus.BAD_REQUEST),
    INVALID_TEMPLATE("TE02", "존재하지 않는 템플릿입니다.", HttpStatus.NOT_FOUND),
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
