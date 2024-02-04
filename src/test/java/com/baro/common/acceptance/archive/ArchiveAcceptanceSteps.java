package com.baro.common.acceptance.archive;

import static com.baro.common.RestApiTest.DEFAULT_REST_DOCS_PATH;
import static com.baro.common.RestApiTest.requestSpec;
import static com.baro.common.acceptance.AcceptanceSteps.예외_응답;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

import com.baro.archive.presentation.dto.ModifyArchiveRequest;
import com.baro.auth.domain.Token;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class ArchiveAcceptanceSteps {

    public static ExtractableResponse<Response> 아카이브_탭_조회_요청_성공(Token token, Long 폴더ID, String 탭이름) {
        var url = "/archives/folder/{folderId}";

        return given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        pathParameters(
                                parameterWithName("folderId").description("폴더 id")
                        ),
                        queryParameters(
                                parameterWithName("tabName").description("탭 이름")
                        ),
                        responseFields(
                                fieldWithPath("[].archiveId").description("아카이브 id"),
                                fieldWithPath("[].tabName").description("아카이브 탭 이름"),
                                fieldWithPath("[].categoryName").description("템플릿 카테고리 이름").optional(),
                                fieldWithPath("[].content").description("아카이브 내용"),
                                fieldWithPath("[].copiedCount").description("템플릿 복사 횟수").optional(),
                                fieldWithPath("[].savedCount").description("템플릿 저장 횟수").optional()
                        ))
                )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.accessToken())
                .pathParam("folderId", 폴더ID)
                .queryParam("tabName", 탭이름)
                .when().get(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 아카이브_탭_조회_요청_실패(Token token, Long 폴더ID, String 탭이름) {
        var url = "/archives/folder/{folderId}";

        return given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                                responseFields(예외_응답())
                        )
                )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.accessToken())
                .pathParam("folderId", 폴더ID)
                .queryParam("tabName", 탭이름)
                .when().get(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 아카이브_수정_요청_성공(Token token, Long 아카이브ID, ModifyArchiveRequest 수정할내용) {
        var url = "/archives/{archiveId}";

        return given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        requestFields(
                                fieldWithPath("content").description("아카이브 내용")
                        )
                ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.accessToken()).body(수정할내용)
                .pathParam("archiveId", 아카이브ID)
                .when().patch(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 아카이브_수정_요청_실패(Token token, Long 아카이브ID, ModifyArchiveRequest 수정할내용) {
        var url = "/archives/{archiveId}";

        return given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        requestFields(
                                fieldWithPath("content").description("아카이브 내용")
                        ),
                        responseFields(예외_응답())
                ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.accessToken()).body(수정할내용)
                .pathParam("archiveId", 아카이브ID)
                .when().patch(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 아카이브_삭제_요청_성공(Token token, Long 아카이브ID) {
        var url = "/archives/{archiveId}";

        return given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        pathParameters(
                                parameterWithName("archiveId").description("아카이브 id")
                        )
                ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.accessToken())
                .pathParam("archiveId", 아카이브ID)
                .when().delete(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 아카이브_삭제_요청_실패(Token token, Long 아카이브ID) {
        var url = "/archives/{archiveId}";

        return given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        pathParameters(
                                parameterWithName("archiveId").description("아카이브 id")
                        ),
                        responseFields(예외_응답())
                ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.accessToken())
                .pathParam("archiveId", 아카이브ID)
                .when().delete(url)
                .then().log().all()
                .extract();
    }
}
