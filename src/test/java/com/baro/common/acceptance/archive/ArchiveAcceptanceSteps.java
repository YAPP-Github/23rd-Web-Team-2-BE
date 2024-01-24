package com.baro.common.acceptance.archive;

import static com.baro.common.RestApiTest.DEFAULT_REST_DOCS_PATH;
import static com.baro.common.RestApiTest.requestSpec;
import static com.baro.common.acceptance.AcceptanceSteps.예외_응답;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;

import com.baro.auth.domain.Token;
import com.baro.memo.presentation.dto.ArchiveTemporalMemoRequest;
import com.baro.template.presentation.dto.ArchiveTemplateRequest;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class ArchiveAcceptanceSteps {

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
                .when().post("/archives/temporal-memos/{temporalMemoId}", 끄적이는_메모_ID)
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
                .when().post("/archives/temporal-memos/{temporalMemoId}", 끄적이는_메모_ID)
                .then().log().all()
                .extract();
    }


    public static ExtractableResponse<Response> 템플릿_아카이브_요청_성공(Token 토큰, Long 템플릿_ID, ArchiveTemplateRequest 바디) {
        var url = "/archives/templates/{templateId}";

        return given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        pathParameters(
                                parameterWithName("templateId").description("템플릿 ID")
                        ),
                        requestFields(
                                fieldWithPath("memoFolderId").description("메모 폴더 ID")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("아카이브된 템플릿 경로")
                        )
                ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken()).body(바디)
                .pathParam("templateId", 템플릿_ID)
                .when().post(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 템플릿_아카이브_요청_실패(Token 토큰, Long 템플릿_ID, ArchiveTemplateRequest 바디) {
        var url = "/archives/templates/{templateId}";

        return given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        pathParameters(
                                parameterWithName("templateId").description("템플릿 ID")
                        ),
                        requestFields(
                                fieldWithPath("memoFolderId").description("메모 폴더 ID")
                        ),
                        responseFields(예외_응답())
                ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken()).body(바디)
                .pathParam("templateId", 템플릿_ID)
                .when().post(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 템플릿_아카이브_취소_요청_성공(Token 토큰, Long 템플릿_ID) {
        var url = "/archives/templates/{templateId}";

        return given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        pathParameters(
                                parameterWithName("templateId").description("템플릿 ID")
                        )
                ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken())
                .pathParam("templateId", 템플릿_ID)
                .when().delete(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 템플릿_아카이브_취소_요청_실패(Token 토큰, Long 템플릿_ID) {
        var url = "/archives/templates/{templateId}";

        return given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        pathParameters(
                                parameterWithName("templateId").description("템플릿 ID")
                        ),
                        responseFields(예외_응답())
                ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken())
                .pathParam("templateId", 템플릿_ID)
                .when().delete(url)
                .then().log().all()
                .extract();
    }
}
