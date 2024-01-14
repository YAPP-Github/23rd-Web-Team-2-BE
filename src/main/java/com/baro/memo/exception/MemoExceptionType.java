package com.baro.memo.exception;

import com.baro.common.exception.RequestExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum MemoExceptionType implements RequestExceptionType {

    OVER_MAX_SIZE_CONTENT("MO01", "메모의 최대 길이는 500자입니다.", HttpStatus.BAD_REQUEST),
    NOT_EXIST_MEMO("MO02", "존재하지 않는 메모 입니다.", HttpStatus.NOT_FOUND),
    NOT_MATCH_OWNER("MO03", "메모 접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
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
