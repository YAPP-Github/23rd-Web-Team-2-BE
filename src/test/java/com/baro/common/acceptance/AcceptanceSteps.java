package com.baro.common.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.FieldDescriptor;

public class AcceptanceSteps {

    public static final HttpStatus 성공 = HttpStatus.OK;
    public static final HttpStatus 생성됨 = HttpStatus.CREATED;
    public static final HttpStatus 응답값_없음 = HttpStatus.NO_CONTENT;
    public static final HttpStatus 리디렉션 = HttpStatus.MOVED_PERMANENTLY;
    public static final HttpStatus 잘못된_요청 = HttpStatus.BAD_REQUEST;
    public static final HttpStatus 권한_없음 = HttpStatus.FORBIDDEN;
    public static final HttpStatus 존재하지_않음 = HttpStatus.NOT_FOUND;

    public static void 응답값을_검증한다(
            ExtractableResponse<Response> 응답,
            HttpStatus 예상상태
    ) {
        assertThat(응답.statusCode()).isEqualTo(예상상태.value());
    }

    public static void 응답의_Location_헤더가_존재한다(
            ExtractableResponse<Response> 응답
    ) {
        assertThat(응답.header(HttpHeaders.LOCATION).getBytes(StandardCharsets.UTF_8)).isNotNull();
    }

    public static void 응답의_특정_필드값을_검증한다(
            ExtractableResponse<Response> 응답,
            String 필드,
            Object 값
    ) {
        assertThat(응답.body().jsonPath().getString(필드)).isEqualTo(값);
    }

    public static FieldDescriptor[] 예외_응답() {
        return new FieldDescriptor[]{
                fieldWithPath("errorCode").description("에러 코드"),
                fieldWithPath("errorMessage").description("에러 메시지")
        };
    }
}
