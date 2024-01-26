package com.baro.common.acceptance.template;

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
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.snippet.Attributes.key;

import com.baro.auth.domain.Token;
import com.baro.template.presentation.dto.ArchiveTemplateRequest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;

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
                                페이지네이션_조회_필드()
                        )
                                .and(템플릿_content())
                ))
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
                        ),
                        responseFields(페이지네이션_조회_필드())
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
                                parameterWithName("category").description("카테고리")
                                        .attributes(key("카테고리 예시").value(
                                                "ask, report, celebrate, thank, comfort, regard, etc"))
                        ),
                        queryParameters(
                                parameterWithName("sort").description("정렬")
                                        .attributes(key("정렬 예시").value("new, copy, save"))
                        ),
                        responseFields(예외_응답())
                ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken())
                .queryParam("sort", 정렬)
                .pathParam("category", 카테고리)
                .when().get(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 템플릿_복사_요청_성공(Token 토큰, Long 템플릿_ID) {
        var url = "/templates/{templateId}/copy";

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
                .when().patch(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 템플릿_복사_요청_실패(Token 토큰, Long 템플릿_ID) {
        var url = "/templates/{templateId}/copy";

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
                .when().patch(url)
                .then().log().all()
                .extract();
    }

    private static List<FieldDescriptor> 페이지네이션_조회_필드() {
        return List.of(
                fieldWithPath("content").description("템플릿 데이터"),
                fieldWithPath("pageable").description("페이지 정보"),
                fieldWithPath("pageable.pageNumber").description("현재 페이지 번호"),
                fieldWithPath("pageable.pageSize").description("한 페이지에 표시될 항목 수"),
                fieldWithPath("pageable.sort").description("정렬에 관한 정보"),
                fieldWithPath("pageable.sort.empty").description("정렬이 비어있는지 여부"),
                fieldWithPath("pageable.sort.sorted").description("정렬 여부"),
                fieldWithPath("pageable.sort.unsorted").description("정렬 여부"),
                fieldWithPath("pageable.offset").description("현재 페이지의 오프셋"),
                fieldWithPath("pageable.paged").description("페이징이 적용되었는지 여부"),
                fieldWithPath("pageable.unpaged").description("페이징이 적용되지 않았는지 여부"),
                fieldWithPath("size").description("현재 페이지의 항목 사이즈"),
                fieldWithPath("number").description("현재 페이지 번호"),
                fieldWithPath("sort").description("정렬에 관한 정보"),
                fieldWithPath("sort.empty").description("정렬 유무"),
                fieldWithPath("sort.sorted").description("정렬 여부"),
                fieldWithPath("sort.unsorted").description("정렬 여부"),
                fieldWithPath("last").description("현재 페이지가 마지막 페이지인지 여부"),
                fieldWithPath("first").description("현재 페이지가 첫 페이지인지 여부"),
                fieldWithPath("numberOfElements").description("현재 페이지의 항목 수"),
                fieldWithPath("empty").description("현재 페이지가 비어있는지 여부")
        );
    }

    private static List<FieldDescriptor> 템플릿_content() {
        return List.of(
                fieldWithPath("content[].templateId").description("템플릿 id"),
                fieldWithPath("content[].category").description("템플릿 카테고리"),
                fieldWithPath("content[].subCategory").description("템플릿 서브 카테고리"),
                fieldWithPath("content[].content").description("템플릿 내용"),
                fieldWithPath("content[].savedCount").description("템플릿 저장 횟수"),
                fieldWithPath("content[].copiedCount").description("템플릿 복사 횟수")
        );
    }

    public static ExtractableResponse<Response> 템플릿_아카이브_요청_성공(Token 토큰, Long 템플릿_ID, ArchiveTemplateRequest 바디) {
        var url = "/templates/{templateId}/archive";

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
        var url = "/templates/{templateId}/archive";

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
        var url = "/templates/{templateId}/archive";

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
        var url = "/templates/{templateId}/archive";

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
