package com.baro.common.acceptance.archive;

import static com.baro.common.RestApiTest.DEFAULT_REST_DOCS_PATH;
import static com.baro.common.RestApiTest.requestSpec;
import static com.baro.common.acceptance.AcceptanceSteps.예외_응답;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;

import com.baro.archive.presentation.dto.GetArchiveRequest;
import com.baro.auth.domain.Token;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class ArchiveAcceptanceSteps {

    public static ExtractableResponse<Response> 아카이브_탭_조회_요청_성공(Token token, GetArchiveRequest 바디) {
        var url = "/archives";

        return given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
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
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.accessToken()).body(바디)
                .when().get(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 아카이브_탭_조회_요청_실패(Token token, GetArchiveRequest 바디) {
        var url = "/archives";

        return given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                                responseFields(예외_응답())
                        )
                )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.accessToken()).body(바디)
                .when().get(url)
                .then().log().all()
                .extract();
    }
}
