package com.baro.test;

import com.baro.common.exception.ApiResponseType;

public enum TestApiResponseType implements ApiResponseType {

    GET_TEST("TE01", "Get test success"),
    POST_TEST("TE02", "Post test success"),
    DELETE_TEST("TE03", "Put test success"),
    ;

    private final String code;
    private final String message;

    TestApiResponseType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
