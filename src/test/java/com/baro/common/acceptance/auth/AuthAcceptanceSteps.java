package com.baro.common.acceptance.auth;

import static com.baro.common.RestApiTest.DEFAULT_REST_DOCS_PATH;
import static com.baro.common.RestApiTest.requestSpec;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;

import com.baro.auth.domain.Token;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class AuthAcceptanceSteps {

    public static ExtractableResponse<Response> 토큰_재발급_요청(Token token) {
        var url = "/auth/reissue";

        return given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        queryParameters(
                                parameterWithName("refreshToken").description("refreshToken")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").description("액세스 토큰"),
                                fieldWithPath("refreshToken").description("리프레시 토큰")
                        ))
                )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.accessToken())
                .queryParam("refreshToken", "Bearer " + token.refreshToken())
                .when().get(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 잘못된_토큰_재발급_요청(Token token) {
        var url = "/auth/reissue";

        return given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        queryParameters(
                                parameterWithName("refreshToken").description("refreshToken")
                        ),
                        responseFields(
                                fieldWithPath("errorCode").description("에러 코드"),
                                fieldWithPath("errorMessage").description("에러 메시지")
                        ))
                )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.accessToken())
                .queryParam("refreshToken", "Bearer " + token.refreshToken())
                .when().get(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> Bearer_타입이_아닌_토큰_재발급_요청(Token token) {
        var url = "/auth/reissue";

        return given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        queryParameters(
                                parameterWithName("refreshToken").description("refreshToken")
                        ),
                        responseFields(
                                fieldWithPath("errorCode").description("에러 코드"),
                                fieldWithPath("errorMessage").description("에러 메시지")
                        ))
                )
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.accessToken())
                .queryParam("refreshToken", "Basic " + token.refreshToken())
                .when().get(url)
                .then().log().all()
                .extract();
    }
}
