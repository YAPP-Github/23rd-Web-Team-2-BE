package com.baro.memofolder.exception;

import com.baro.common.exception.RequestExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum MemoFolderExceptionType implements RequestExceptionType {

    NAME_DUPLICATION("MF01", "중복된 이름의 폴더가 존재합니다.", HttpStatus.BAD_REQUEST),
    OVER_MAX_SIZE_NAME("MF02", "폴더 이름은 최대 20자까지 가능합니다.", HttpStatus.BAD_REQUEST),
    NOT_EXIST_MEMO_FOLDER("MF03", "존재하지 않는 폴더입니다.", HttpStatus.NOT_FOUND),
    NOT_MATCH_OWNER("MF04", "폴더의 소유자가 아닙니다.", HttpStatus.FORBIDDEN),
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
