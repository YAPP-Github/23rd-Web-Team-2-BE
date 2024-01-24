package com.baro.archive.exception;

import com.baro.common.exception.RequestExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum ArchiveExceptionType implements RequestExceptionType {

    NOT_EXIST_ARCHIVE("AR01", "존재하지 않는 아카이브 입니다.", HttpStatus.NOT_FOUND),
    ARCHIVED_TEMPLATE("AR02", "이미 저장한 템플릿입니다.", HttpStatus.BAD_REQUEST),
    NOT_ARCHIVED_TEMPLATE("AR03", "저장하지 않은 템플릿입니다.", HttpStatus.BAD_REQUEST),
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
