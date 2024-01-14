package com.baro.common.acceptance.memo;

import static com.baro.common.RestApiTest.DEFAULT_REST_DOCS_PATH;
import static com.baro.common.RestApiTest.requestSpec;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import com.baro.auth.domain.Token;
import com.baro.memo.presentation.dto.SaveTemporalMemoRequest;
import com.baro.memo.presentation.dto.UpdateTemporalMemoRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class TemporalMemoAcceptanceSteps {

    public static ExtractableResponse<Response> 끄적이는메모_생성_요청(Token 토큰, SaveTemporalMemoRequest 바디) {
        return RestAssured.given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        requestFields(
                                fieldWithPath("content").description("끄적이는 메모 내용")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("생성된 끄적이는 메모 경로")
                        ))
                ).contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken()).body(바디)
                .when().post("/temporal-memos")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 잘못된_끄적이는_메모_생성_요청(Token 토큰, SaveTemporalMemoRequest 바디) {
        return RestAssured.given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        responseFields(
                                fieldWithPath("errorCode").description("에러 코드"),
                                fieldWithPath("errorMessage").description("에러 메시지")
                        ))
                ).header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken()).body(바디)
                .when().post("/temporal-memos")
                .then().log().all()
                .extract();
    }

    public static Long 끄적이는메모를_생성하고_ID를_반환한다(Token 토큰, SaveTemporalMemoRequest 바디) {
        String location = RestAssured.given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        requestFields(
                                fieldWithPath("content").description("끄적이는 메모 내용")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("생성된 끄적이는 메모 경로")
                        ))
                ).contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken()).body(바디)
                .when().post("/temporal-memos")
                .then().log().all()
                .extract()
                .response().header(HttpHeaders.LOCATION);

        String[] split = location.split("/");
        return Long.parseLong(split[split.length - 1]);
    }

    public static ExtractableResponse<Response> 끄적이는메모_수정_요청(Token 토큰, Long 끄적이는_메모_ID, UpdateTemporalMemoRequest 바디) {
        return RestAssured.given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        requestFields(
                                fieldWithPath("content").description("끄적이는 메모 내용")
                        ))
                ).contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken()).body(바디)
                .when().patch("/temporal-memos/{temporalMemoId}", 끄적이는_메모_ID)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 잘못된_끄적이는메모_수정_요청(Token 토큰, Long 끄적이는_메모_ID,
                                                                 UpdateTemporalMemoRequest 바디) {
        return RestAssured.given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        requestFields(
                                fieldWithPath("content").description("끄적이는 메모 내용")
                        ),
                        responseFields(
                                fieldWithPath("errorCode").description("에러 코드"),
                                fieldWithPath("errorMessage").description("에러 메시지")
                        ))
                ).contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken()).body(바디)
                .when().patch("/temporal-memos/{temporalMemoId}", 끄적이는_메모_ID)
                .then().log().all()
                .extract();
    }
}
