package com.baro.common.acceptance.member;

import static com.baro.common.RestApiTest.DEFAULT_REST_DOCS_PATH;
import static com.baro.common.RestApiTest.requestSpec;
import static com.baro.common.acceptance.AcceptanceSteps.예외_응답;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import com.baro.auth.domain.Token;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;

public class MemberAcceptanceSteps {

    public static ExtractableResponse<Response> 내_프로필_조회_요청(Token 토큰) {
        return RestAssured.given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        responseFields(
                                fieldWithPath("id").description("회원 ID"),
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("nickname").description("회원 닉네임"),
                                fieldWithPath("email").description("회원 이메일"),
                                fieldWithPath("profileImageUrl").description("회원 프로필 이미지 URL"),
                                fieldWithPath("oAuthServiceType").description("OAuth 서비스 타입")
                        ))
                )
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken())
                .when().get("/members/profile/me")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 잘못된_프로필_조회_요청(Token 토큰) {
        return RestAssured.given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        responseFields(예외_응답()))
                )
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken())
                .when().get("/members/profile/me")
                .then().log().all()
                .extract();
    }
}
