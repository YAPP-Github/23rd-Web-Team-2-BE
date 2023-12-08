package com.baro.common.response;

import com.baro.common.exception.ApiResponseType;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

@Getter
@JsonPropertyOrder({"responseCode", "message", "data"})
public class ApiResponseBody<T> {

    private final String responseCode;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final T data;

    protected ApiResponseBody(final String responseCode, final String message, final T data) {
        this.responseCode = responseCode;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponseBody<T> of(final ApiResponseType responseType, final T data) {
        return new ApiResponseBody<>(responseType.getCode(), responseType.getMessage(), data);
    }

    public static <T> ApiResponseBody<T> from(final ApiResponseType responseType) {
        return new ApiResponseBody<>(responseType.getCode(), responseType.getMessage(), null);
    }
}
