package com.baro.common.acceptance.member;

import static com.baro.common.RestApiTest.DEFAULT_REST_DOCS_PATH;
import static com.baro.common.RestApiTest.requestSpec;
import static com.baro.common.acceptance.AcceptanceSteps.예외_응답;
import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import com.baro.auth.domain.Token;
import com.baro.member.presentation.dto.DeleteMemberRequest;
import com.baro.member.presentation.dto.UpdateMemberProfileRequest;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class MemberAcceptanceSteps {

    public static final UpdateMemberProfileRequest 프로필_수정_바디 = new UpdateMemberProfileRequest("바로", "닉네임");
    public static final UpdateMemberProfileRequest 길이가_초과된_프로필_수정_바디 = new UpdateMemberProfileRequest("바로",
            "닉네임".repeat(11));
    public static final UpdateMemberProfileRequest 빈_닉네임_프로필_수정_바디 = new UpdateMemberProfileRequest("바로", "");

    public static final MultipartFile 프로필_이미지 = new MockMultipartFile("image", "image", "multipart/form-data",
            "image".getBytes());

    public static final DeleteMemberRequest 회원_탈퇴_요청 = new DeleteMemberRequest("탈퇴 사유");

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

    public static ExtractableResponse<Response> 내_프로필_수정_요청(Token 토큰, UpdateMemberProfileRequest 요청) {
        return RestAssured.given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        requestFields(
                                fieldWithPath("name").description("회원 이름"),
                                fieldWithPath("nickname").description("회원 닉네임")
                        )
                ))
                .contentType("application/json")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken())
                .body(요청)
                .when().patch("/members/profile/me")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 잘못된_내_프로필_수정_요청(Token 토큰, UpdateMemberProfileRequest 요청) {
        return RestAssured.given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        ),
                        responseFields(예외_응답())
                ))
                .contentType("application/json")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken())
                .body(요청)
                .when().patch("/members/profile/me")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 프로필_이미지_삭제_요청(Token 토큰) {
        return RestAssured.given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        )
                ))
                .contentType("application/json")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken())
                .when().delete("/members/image")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 프로필_이미지_수정_요청(Token 토큰, MultipartFile 이미지) {
        var multiPart = 멀티_파트_파일_생성(이미지);

        return RestAssured.given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        )
                ))
                .contentType("multipart/form-data")
                .multiPart(multiPart)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken())
                .when().patch("/members/image")
                .then().log().all()
                .extract();
    }

    private static MultiPartSpecification 멀티_파트_파일_생성(MultipartFile 이미지) {
        try {
            return new MultiPartSpecBuilder(이미지.getBytes())
                    .controlName("profileImage")
                    .fileName(이미지.getName())
                    .charset(UTF_8)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ExtractableResponse<Response> 회원_탈퇴_요청(Token 토큰, DeleteMemberRequest 요청) {
        return RestAssured.given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("인증 토큰")
                        )
                ))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + 토큰.accessToken())
                .body(요청)
                .contentType("application/json")
                .when().delete("/members")
                .then().log().all()
                .extract();
    }
}
