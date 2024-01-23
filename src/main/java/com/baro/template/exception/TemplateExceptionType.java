package com.baro.template.exception;

import com.baro.common.exception.RequestExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum TemplateExceptionType implements RequestExceptionType {

    INVALID_CATEGORY("TE01", "존재하지 않는 카테고리입니다.", HttpStatus.BAD_REQUEST),
    INVALID_TEMPLATE("TE02", "존재하지 않는 템플릿입니다.", HttpStatus.NOT_FOUND),
    ARCHIVED_TEMPLATE("TE03", "이미 저장한 템플릿입니다.", HttpStatus.BAD_REQUEST),
    NOT_ARCHIVED_TEMPLATE("TE04", "저장하지 않은 템플릿입니다.", HttpStatus.BAD_REQUEST),
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
