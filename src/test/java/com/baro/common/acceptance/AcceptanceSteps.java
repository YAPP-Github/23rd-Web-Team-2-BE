package com.baro.common.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpStatus;

public class AcceptanceSteps {

    public static final HttpStatus 성공 = HttpStatus.OK;
    public static final HttpStatus 생성됨 = HttpStatus.CREATED;
    public static final HttpStatus 리디렉션 = HttpStatus.MOVED_PERMANENTLY;
    public static final HttpStatus 잘못된_요청 = HttpStatus.BAD_REQUEST;

    public static void 응답값을_검증한다(
            ExtractableResponse<Response> 응답,
            HttpStatus 예상상태
    ) {
        assertThat(응답.statusCode()).isEqualTo(예상상태.value());
    }

    public static void 응답의_Location_헤더가_존재한다(
            ExtractableResponse<Response> 응답
    ) {
        assertThat(응답.header("Location").getBytes(StandardCharsets.UTF_8)).isNotNull();
    }

    public static void 응답의_특정_필드값을_검증한다(
            ExtractableResponse<Response> 응답,
            String 필드,
            Object 값
    ) {
        assertThat(응답.body().jsonPath().getString(필드)).isEqualTo(값);
    }

    public static void 응답의_개수를_검증한다(
            ExtractableResponse<Response> 응답,
            String json키,
            int 값
    ) {
        assertThat(응답.body().jsonPath().getList(json키)).hasSize(값);
    }
}
