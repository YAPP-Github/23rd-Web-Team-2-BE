package com.baro.archive.exception;

import com.baro.common.exception.RequestException;
import com.baro.common.exception.RequestExceptionType;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ArchiveException extends RequestException {

    private final ArchiveExceptionType exceptionType;

    @Override
    public RequestExceptionType exceptionType() {
        return exceptionType;
    }
}
