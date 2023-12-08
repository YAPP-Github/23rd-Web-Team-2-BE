package com.baro.common.response;

import com.baro.common.exception.ApiResponseType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class ApiResponse<T> extends ResponseEntity<T> {

    private ApiResponse(T body, MultiValueMap<String, String> headers, HttpStatusCode statusCode) {
        super(body, headers, statusCode);
    }

    public static ApiResponse<ApiResponseBody<ApiResponseType>> ok(ApiResponseType type) {
        ApiResponseBody<ApiResponseType> response = ApiResponseBody.from(type);
        return new ApiResponse<>(response, null, HttpStatus.OK);
    }

    public static <T, R extends ApiResponseBody<T>> ApiResponse<R> ok(ApiResponseType type, T data) {
        ApiResponseBody<T> tApiResponseBody = ApiResponseBody.of(type, data);
        return (ApiResponse<R>) new ApiResponse<>(tApiResponseBody, null, HttpStatus.OK);
    }

    public static ApiResponse<ApiResponseBody<ApiResponseType>> created(ApiResponseType type, String location) {
        ApiResponseBody<ApiResponseType> response = ApiResponseBody.from(type);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", location);
        return new ApiResponse<>(response, httpHeaders, HttpStatus.CREATED);
    }

    public static ApiResponse<ApiResponseBody<ApiResponseType>> noContent(ApiResponseType type) {
        ApiResponseBody<ApiResponseType> response = ApiResponseBody.from(type);
        return new ApiResponse<>(response, null, HttpStatus.NO_CONTENT);
    }
}
