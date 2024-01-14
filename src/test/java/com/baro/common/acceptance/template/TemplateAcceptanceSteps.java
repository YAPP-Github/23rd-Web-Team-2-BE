package com.baro.common.acceptance.template;

import static com.baro.common.RestApiTest.DEFAULT_REST_DOCS_PATH;
import static com.baro.common.RestApiTest.requestSpec;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.snippet.Attributes.key;

import com.baro.auth.domain.Token;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class TemplateAcceptanceSteps {

    public static ExtractableResponse<Response> 템플릿_조회_요청_성공(Token 토큰, String 카테고리, String 정렬) {
        var url = "/templates/{category}";

        return given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        pathParameters(
                                parameterWithName("category")
                                        .description("카테고리")
                                        .attributes(key("카테고리 예시").value(
                                                "ask, report, celebrate, thank, comfort, regard, etc"))
                        ),
                        queryParameters(
                                parameterWithName("sort")
                                        .description("정렬")
                                        .attributes(key("정렬 예시").value(
                                                "new, copy, save"))
                        ),
                        responseFields(
                                fieldWithPath("[].templateId").description("템플릿 id"),
                                fieldWithPath("[].category").description("카테고리 이름"),
                                fieldWithPath("[].subCategory").description("서브 카테고리 이름"),
                                fieldWithPath("[].content").description("템플릿 내용"),
                                fieldWithPath("[].savedCount").description("저장 횟수"),
                                fieldWithPath("[].copiedCount").description("복사 횟수")
                        ))
                )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken())
                .pathParam("category", 카테고리)
                .queryParam("sort", 정렬)
                .when().get(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 템플릿_조회시_응답값이_없는_요청(Token 토큰, String 카테고리, String 정렬) {
        var url = "/templates/{category}";

        return given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        pathParameters(
                                parameterWithName("category")
                                        .description("카테고리")
                                        .attributes(key("카테고리 예시").value(
                                                "ask, report, celebrate, thank, comfort, regard, etc"))
                        ),
                        queryParameters(
                                parameterWithName("sort")
                                        .description("정렬")
                                        .attributes(key("정렬 예시").value(
                                                "new, copy, save"))
                        )
                ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken())
                .pathParam("category", 카테고리)
                .queryParam("sort", 정렬)
                .when().get(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 템플릿_조회_요청_실패(Token 토큰, String 카테고리, String 정렬) {
        var url = "/templates/{category}";

        return given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        pathParameters(
                                parameterWithName("category")
                                        .description("카테고리")
                                        .attributes(key("카테고리 예시").value(
                                                "ask, report, celebrate, thank, comfort, regard, etc"))
                        ),
                        queryParameters(
                                parameterWithName("sort")
                                        .description("정렬")
                                        .attributes(key("정렬 예시").value(
                                                "new, copy, save"))
                        ),
                        responseFields(
                                fieldWithPath("errorCode").description("에러 코드"),
                                fieldWithPath("errorMessage").description("에러 메시지")
                        ))
                )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken())
                .queryParam("sort", 정렬)
                .pathParam("category", 카테고리)
                .when().get(url)
                .then().log().all()
                .extract();
    }
}
