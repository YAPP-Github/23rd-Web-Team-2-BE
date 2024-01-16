package com.baro.memo.exception;

import com.baro.common.exception.RequestExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum TemporalMemoExceptionType implements RequestExceptionType {

    NOT_EXIST_TEMPORAL_MEMO("TM02", "존재하지 않는 끄적이는 메모 입니다.", HttpStatus.NOT_FOUND),
    NOT_MATCH_OWNER("TM03", "끄적이는 메모에 대한 접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
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
