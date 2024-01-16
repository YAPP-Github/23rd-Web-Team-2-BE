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
import java.util.ArrayList;
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
                                content_결괏값이_있는_템플릿_페이지네이션_조회_Slice_필드()
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
                        ),
                        responseFields(
                                content_결괏값이_없는_템플릿_페이지네이션_조회_Slice_필드()
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
                                parameterWithName("category").description("카테고리")
                                        .attributes(key("카테고리 예시").value(
                                                "ask, report, celebrate, thank, comfort, regard, etc"))
                        ),
                        queryParameters(
                                parameterWithName("sort").description("정렬")
                                        .attributes(key("정렬 예시").value("new, copy, save"))
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

    private static List<FieldDescriptor> content_결괏값이_있는_템플릿_페이지네이션_조회_Slice_필드() {
        var responseFields = new ArrayList<>(content_결괏값이_없는_템플릿_페이지네이션_조회_Slice_필드());
        responseFields.addAll(템플릿_content());
        return responseFields;
    }


    private static List<FieldDescriptor> content_결괏값이_없는_템플릿_페이지네이션_조회_Slice_필드() {
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
}
