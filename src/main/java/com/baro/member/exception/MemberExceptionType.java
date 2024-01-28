package com.baro.member.exception;

import com.baro.common.exception.RequestExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum MemberExceptionType implements RequestExceptionType {

    NOT_EXIST_MEMBER("ME01", "존재하지 않는 회원 입니다.", HttpStatus.BAD_REQUEST),
    NOT_MATCH_MEMBER("ME02", "회원 정보가 일치하지 않습니다.", HttpStatus.FORBIDDEN),
    NICKNAME_DUPLICATION("ME03", "이미 존재하는 닉네임 입니다.", HttpStatus.BAD_REQUEST),
    OVER_MAX_SIZE_NICKNAME("ME04", "닉네임은 최대 30자까지 입력 가능합니다.", HttpStatus.BAD_REQUEST),
    EMPTY_NICKNAME("ME05", "빈 닉네임은 입력할 수 없습니다.", HttpStatus.BAD_REQUEST),
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
