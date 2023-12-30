package com.baro.oauth.presentation;

import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import com.baro.common.RestApiDocumentationTest;

import java.nio.charset.StandardCharsets;

import com.baro.oauth.infra.kakao.KakaoOAuthClient;
import com.baro.oauth.infra.kakao.KakaoRequestApi;
import com.baro.oauth.infra.kakao.dto.KakaoMemberResponse;
import com.baro.oauth.infra.kakao.dto.KakaoTokenResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static org.mockito.Mockito.*;

class OAuthApiDocumentTest extends RestApiDocumentationTest {

    @MockBean
    KakaoRequestApi kakaoRequestApi;

    @Autowired
    KakaoOAuthClient oAuthClient;

    @Test
    void oauth_sign_in_url() {
        // given
        var url = "/oauth/{oAuthService}";

        // when
        var response = given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        pathParameters(
                                parameterWithName("oAuthService").description("OAuth 로그인 소셜 타입")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("로그인 url")
                        ))
                ).redirects().follow(false)
                .when().get(url, "kakao")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.MOVED_PERMANENTLY.value());
        String location = new String(response.header("Location").getBytes(StandardCharsets.UTF_8));
        assertThat(location).isNotNull();
    }

    @Test
    void oauth_sign_in() {
        // given
        var url = "/oauth/sign-in/{oAuthService}";

        when(kakaoRequestApi.requestToken(anyMap()))
                .thenReturn(new KakaoTokenResponse("Bearer", "accessToken", "idToken",
                        1000, "refreshToken", 1000, "scope"));
        when(kakaoRequestApi.requestMemberInfo(anyString()))
                .thenReturn(new KakaoMemberResponse(1L, "nickname",
                        new KakaoMemberResponse.Properties("nickname"), null));

        // when
        var response = given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        pathParameters(
                                parameterWithName("oAuthService").description("OAuth 로그인 소셜 타입")
                        ),
                        queryParameters(
                                parameterWithName("authCode").description("OAuth 소셜로그인 authcode")
                        )
                ))
                .queryParam("authCode", "authCode")
                .when().get(url, "kakao")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
