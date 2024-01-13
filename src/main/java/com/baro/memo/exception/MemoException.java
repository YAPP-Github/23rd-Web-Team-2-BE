package com.baro.memo.exception;

import com.baro.common.exception.RequestException;
import com.baro.common.exception.RequestExceptionType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemoException extends RequestException {

    private final MemoExceptionType exceptionType;

    @Override
    public RequestExceptionType exceptionType() {
        return exceptionType;
    }
}
