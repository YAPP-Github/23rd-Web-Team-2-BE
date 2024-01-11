package com.baro.auth.presentation;

import com.baro.auth.application.AuthService;
import com.baro.auth.application.TokenDecrypter;
import com.baro.auth.application.dto.SignInDto;
import com.baro.auth.domain.Token;
import com.baro.auth.exception.jwt.JwtTokenException;
import com.baro.auth.exception.jwt.JwtTokenExceptionType;
import com.baro.common.RestApiDocumentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static com.epages.restdocs.apispec.RestAssuredRestDocumentationWrapper.document;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;

public class AuthApiDocumentTest extends RestApiDocumentationTest {

    @MockBean
    TokenDecrypter tokenDecrypter;

    @Autowired
    AuthService authService;

    @Test
    void reissue_성공() {
        // given
        var url = "/auth/reissue";
        SignInDto dto = new SignInDto("name", "email", "1", "kakao");
        Token token = authService.signIn(dto);

        // when
        var response = given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        queryParameters(
                                parameterWithName("refreshToken").description("refreshToken")
                        )
                ))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.accessToken())
                .queryParam("refreshToken", token.refreshToken())
                .when().get(url)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void refresh토큰이_null일경우_예외발생() {
        // given
        var url = "/auth/reissue";
        SignInDto dto = new SignInDto("name", "email", "1", "kakao");
        Token token = authService.signIn(dto);

        // when
        var response = given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        queryParameters(
                                parameterWithName("refreshToken").description("refreshToken")
                        )
                ))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.accessToken())
                .queryParam("refreshToken")
                .when().get(url)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void refresh토큰이_만료된_경우_예외발생() {
        // given
        var url = "/auth/reissue";
        SignInDto dto = new SignInDto("name", "email", "1", "kakao");
        Token token = authService.signIn(dto);
        doThrow(new JwtTokenException(JwtTokenExceptionType.EXPIRED_JWT_TOKEN))
                .when(tokenDecrypter).decryptRefreshToken(token.refreshToken());

        // when
        var response = given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        queryParameters(
                                parameterWithName("refreshToken").description("refreshToken")
                        )
                ))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.accessToken())
                .queryParam("refreshToken", token.refreshToken())
                .when().get(url)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 저장된_refresh_token이_존재하지_않을_경우_예외발생() {
        // given
        var url = "/auth/reissue";
        var accessToken = "accessToken";
        var refreshToken = "refreshToken";
        when(tokenDecrypter.decryptAccessToken(accessToken)).thenReturn(1L);
        doNothing().when(tokenDecrypter).decryptRefreshToken(refreshToken);

        // when
        var response = given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        queryParameters(
                                parameterWithName("refreshToken").description("refreshToken")
                        )
                ))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .queryParam("refreshToken", refreshToken)
                .when().get(url)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 클라이언트와_토큰이_일치하지_않을_경우_예외발생() {
        // given
        var url = "/auth/reissue";
        SignInDto dtoA = new SignInDto("name", "email", "1", "kakao");
        SignInDto dtoB = new SignInDto("name", "email", "2", "naver");
        Token tokenA = authService.signIn(dtoA);
        Token tokenB = authService.signIn(dtoB);

        // when
        var response = given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        queryParameters(
                                parameterWithName("refreshToken").description("refreshToken")
                        )
                ))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenA.accessToken())
                .queryParam("refreshToken", tokenB.refreshToken())
                .when().get(url)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 올바르지_않은_리프레시_토큰의_경우_예외발생() {
        // given
        var url = "/auth/reissue";
        SignInDto dto = new SignInDto("name", "email", "1", "kakao");
        Token token = authService.signIn(dto);

        // when
        var response = given(requestSpec).log().all()
                .filter(document(DEFAULT_REST_DOCS_PATH,
                        queryParameters(
                                parameterWithName("refreshToken").description("refreshToken")
                        )
                ))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.accessToken())
                .queryParam("refreshToken", "invalidRefreshToken")
                .when().get(url)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
