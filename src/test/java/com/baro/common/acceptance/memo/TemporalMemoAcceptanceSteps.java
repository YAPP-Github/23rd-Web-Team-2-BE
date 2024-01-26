package com.baro.common.acceptance.memo;

import static com.baro.common.RestApiTest.DEFAULT_REST_DOCS_PATH;
import static com.baro.common.RestApiTest.requestSpec;
import static com.baro.common.acceptance.AcceptanceSteps.예외_응답;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;

import com.baro.auth.domain.Token;
import com.baro.memo.presentation.dto.ApplyCorrectionRequest;
import com.baro.memo.presentation.dto.ArchiveTemporalMemoRequest;
import com.baro.memo.presentation.dto.SaveTemporalMemoRequest;
import com.baro.memo.presentation.dto.UpdateTemporalMemoRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class TemporalMemoAcceptanceSteps {

    public static final String 크기_초과_컨텐츠 = "글".repeat(501);
    public static final SaveTemporalMemoRequest 끄적이는_메모_바디 = new SaveTemporalMemoRequest("끄적이는 메모 컨텐츠");
    public static final SaveTemporalMemoRequest 크기_초과_끄적이는_메모_작성_바디 = new SaveTemporalMemoRequest(크기_초과_컨텐츠);
    public static final UpdateTemporalMemoRequest 끄적이는_메모_수정_바디 = new UpdateTemporalMemoRequest("끄적이는 메모 수정 컨텐츠");
    public static final UpdateTemporalMemoRequest 크기_초과_끄적이는_메모_수정_바디 = new UpdateTemporalMemoRequest(크기_초과_컨텐츠);
    public static final ApplyCorrectionRequest 맞춤법_검사_결과_반영_바디 = new ApplyCorrectionRequest("맞춤법 검사 결과");
    public static final ApplyCorrectionRequest 크기_초과_맞춤법_검사_결과_반영_바디 = new ApplyCorrectionRequest(크기_초과_컨텐츠);

    public static ArchiveTemporalMemoRequest 메모_아카이브_요청_바디(Long 메모_폴더_ID) {
        return new ArchiveTemporalMemoRequest(메모_폴더_ID);
    }

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
                        responseFields(예외_응답()))
                ).header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken()).body(바디)
                .when().post("/temporal-memos")
                .then().log().all()
                .extract();
    }

    public static Long 끄적이는메모를_생성하고_ID를_반환한다(Token 토큰, SaveTemporalMemoRequest 바디) {
        String location = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
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
                        pathParameters(
                                parameterWithName("temporalMemoId").description("끄적이는 메모 ID")
                        ),
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
                        pathParameters(
                                parameterWithName("temporalMemoId").description("끄적이는 메모 ID")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        requestFields(
                                fieldWithPath("content").description("끄적이는 메모 내용")
                        ),
                        responseFields(예외_응답()))
                ).contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken()).body(바디)
                .when().patch("/temporal-memos/{temporalMemoId}", 끄적이는_메모_ID)
                .then().log().all()
                .extract();
    }


    public static ExtractableResponse<Response> 끄적이는메모_삭제_요청(Token 토큰, Long 끄적이는_메모_ID) {
        return RestAssured.given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        pathParameters(
                                parameterWithName("temporalMemoId").description("끄적이는 메모 ID")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ))
                ).contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken())
                .when().delete("/temporal-memos/{temporalMemoId}", 끄적이는_메모_ID)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 잘못된_끄적이는메모_삭제_요청(Token 토큰, Long 끄적이는_메모_ID) {
        return RestAssured.given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        pathParameters(
                                parameterWithName("temporalMemoId").description("끄적이는 메모 ID")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        responseFields(예외_응답()))
                ).contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken())
                .when().delete("/temporal-memos/{temporalMemoId}", 끄적이는_메모_ID)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 끄적이는_메모_맞춤법_검사_결과_반영_요청(Token 토큰, Long 끄적이는_메모_ID,
                                                                        ApplyCorrectionRequest 바디) {
        return RestAssured.given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        pathParameters(
                                parameterWithName("temporalMemoId").description("끄적이는 메모 ID")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        requestFields(
                                fieldWithPath("content").description("맞춤법 검사 결과")
                        ))
                ).contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken()).body(바디)
                .when().patch("/temporal-memos/{temporalMemoId}/correction", 끄적이는_메모_ID)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 잘못된_끄적이는_메모_맞춤법_검사_결과_반영_요청(Token 토큰, Long 끄적이는_메모_ID,
                                                                            ApplyCorrectionRequest 바디) {
        return RestAssured.given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        pathParameters(
                                parameterWithName("temporalMemoId").description("끄적이는 메모 ID")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        requestFields(
                                fieldWithPath("content").description("맞춤법 검사 결과")
                        ),
                        responseFields(예외_응답()))
                ).contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken()).body(바디)
                .when().patch("/temporal-memos/{temporalMemoId}/correction", 끄적이는_메모_ID)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 끄적이는_메모_조회_요청(Token 토큰, LocalDate 시작_날짜, LocalDate 끝_날짜) {
        return RestAssured.given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        queryParameters(
                                parameterWithName("startDate").description("끄적이는 메모 조회 시작 날짜"),
                                parameterWithName("endDate").description("끄적이는 메모 조회 끝 날짜")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        responseFields(
                                fieldWithPath("[].createdAt").description("끄적이는 메모 생성 날짜"),
                                fieldWithPath("[].temporalMemos").description("끄적이는 메모 목록"),
                                fieldWithPath("[].temporalMemos[].id").description("끄적이는 메모 ID"),
                                fieldWithPath("[].temporalMemos[].content").description("끄적이는 메모 내용"),
                                fieldWithPath("[].temporalMemos[].correctionContent").description("끄적이는 메모 맞춤법 검사 결과"),
                                fieldWithPath("[].temporalMemos[].isCorrected").description("끄적이는 메모 맞춤법 검사 결과 반영 여부"),
                                fieldWithPath("[].temporalMemos[].isArchived").description("끄적이는 메모 아카이브 여부"),
                                fieldWithPath("[].temporalMemos[].createdAt").description("끄적이는 메모 생성 시간")
                        ))
                ).contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("startDate", 시작_날짜.toString())
                .queryParam("endDate", 끝_날짜.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken())
                .when().get("/temporal-memos")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 잘못된_끄적이는_메모_조회_요청(Token 토큰, LocalDate 시작_날짜, LocalDate 끝_날짜) {
        return RestAssured.given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        queryParameters(
                                parameterWithName("startDate").description("끄적이는 메모 조회 시작 날짜"),
                                parameterWithName("endDate").description("끄적이는 메모 조회 끝 날짜")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        responseFields(예외_응답()))
                ).contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("startDate", 시작_날짜.toString())
                .queryParam("endDate", 끝_날짜.toString())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken())
                .when().get("/temporal-memos")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 끄적이는메모_아카이빙_요청(Token 토큰, Long 끄적이는_메모_ID,
                                                               ArchiveTemporalMemoRequest 바디) {
        return RestAssured.given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        pathParameters(
                                parameterWithName("temporalMemoId").description("끄적이는 메모 ID")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        requestFields(
                                fieldWithPath("memoFolderId").description("메모 폴더 ID")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("아카이브된 메모 경로")
                        ))
                ).contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken()).body(바디)
                .when().post("/temporal-memos/{temporalMemoId}/archive", 끄적이는_메모_ID)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 잘못된_끄적이는메모_아카이빙_요청(Token 토큰, Long 끄적이는_메모_ID,
                                                                   ArchiveTemporalMemoRequest 바디) {
        return RestAssured.given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        pathParameters(
                                parameterWithName("temporalMemoId").description("끄적이는 메모 ID")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        requestFields(
                                fieldWithPath("memoFolderId").description("메모 폴더 ID")
                        ),
                        responseFields(예외_응답()))
                ).contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken()).body(바디)
                .when().post("/temporal-memos/{temporalMemoId}/archive", 끄적이는_메모_ID)
                .then().log().all()
                .extract();
    }

}
