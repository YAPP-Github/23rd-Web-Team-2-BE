package com.baro.auth.presentation.oauth;

import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.anyString;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;

import com.baro.auth.application.AuthService;
import com.baro.auth.infra.oauth.kakao.KakaoOAuthClient;
import com.baro.auth.infra.oauth.kakao.KakaoRequestApi;
import com.baro.auth.infra.oauth.kakao.dto.KakaoMemberResponse;
import com.baro.auth.infra.oauth.kakao.dto.KakaoMemberResponse.KakaoAccount;
import com.baro.auth.infra.oauth.kakao.dto.KakaoTokenResponse;
import com.baro.common.RestApiDocumentationTest;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OAuthApiDocumentTest extends RestApiDocumentationTest {

    @MockBean
    KakaoRequestApi kakaoRequestApi;

    @Autowired
    KakaoOAuthClient oAuthClient;

    @Autowired
    AuthService authService;

    @Test
    void OAuth_로그인시_필요한_url을_반환한다() {
        // given
        var url = "/auth/oauth/{oauthType}";

        // when
        var response = given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        pathParameters(
                                parameterWithName("oauthType").description("OAuth 로그인 소셜 타입")
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
    void OAuth로_로그인한다() {
        // given
        var url = "/auth/oauth/sign-in/{oauthType}";

        setOAuthTokenRequest();
        setOAuthMemberInfoRequest();

        // when
        var response = given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        pathParameters(
                                parameterWithName("oauthType").description("OAuth 로그인 소셜 타입")
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

    private void setOAuthTokenRequest() {
        given(kakaoRequestApi.requestToken(anyMap()))
                .willReturn(new KakaoTokenResponse("Bearer", "accessToken", "idToken",
                        1000, "refreshToken", 1000, "scope"));
    }

    private void setOAuthMemberInfoRequest() {
        given(kakaoRequestApi.requestMemberInfo(anyString()))
                .willReturn(new KakaoMemberResponse(1L, "nickname",
                        new KakaoMemberResponse.Properties("nickname"),
                        new KakaoAccount(false, new KakaoAccount.Profile("nickname"), "email")
                ));
    }
}
