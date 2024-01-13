package com.baro.common.acceptance.auth;

import static com.baro.common.RestApiTest.DEFAULT_REST_DOCS_PATH;
import static com.baro.common.RestApiTest.requestSpec;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class OAuthAcceptanceSteps {

    public static ExtractableResponse<Response> 리다이렉트_URI_요청(String oauthServiceType) {
        var url = "/auth/oauth/{oauthType}";

        return given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        pathParameters(
                                parameterWithName("oauthType").description("OAuth 로그인 소셜 타입")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("로그인 url")
                        ))
                )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .redirects().follow(false)
                .when().get(url, oauthServiceType)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인_요청() {
        var url = "/auth/oauth/sign-in/{oauthType}";

        return given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        pathParameters(
                                parameterWithName("oauthType").description("OAuth 로그인 소셜 타입")
                        ),
                        queryParameters(
                                parameterWithName("authCode").description("OAuth 소셜로그인 authcode")
                        )
                ))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("authCode", "authCode")
                .when().get(url, "kakao")
                .then().log().all()
                .extract();
    }
}
