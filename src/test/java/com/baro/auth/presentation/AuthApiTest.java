package com.baro.auth.presentation;

import static com.baro.auth.fixture.OAuthMemberInfoFixture.동균;
import static com.baro.auth.fixture.OAuthMemberInfoFixture.원진;
import static com.baro.auth.fixture.OAuthMemberInfoFixture.은지;
import static com.baro.common.acceptance.AcceptanceSteps.성공;
import static com.baro.common.acceptance.AcceptanceSteps.응답값을_검증한다;
import static com.baro.common.acceptance.AcceptanceSteps.잘못된_요청;
import static com.baro.common.acceptance.auth.AuthAcceptanceSteps.Bearer_타입이_아닌_토큰_재발급_요청;
import static com.baro.common.acceptance.auth.AuthAcceptanceSteps.잘못된_토큰_재발급_요청;
import static com.baro.common.acceptance.auth.AuthAcceptanceSteps.토큰_재발급_요청;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

import com.baro.auth.application.TokenDecrypter;
import com.baro.auth.application.TokenStorage;
import com.baro.auth.domain.Token;
import com.baro.auth.exception.jwt.JwtTokenException;
import com.baro.auth.exception.jwt.JwtTokenExceptionType;
import com.baro.common.RestApiTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class AuthApiTest extends RestApiTest {

    @SpyBean
    TokenDecrypter tokenDecrypter;
    @SpyBean
    TokenStorage tokenStorage;

    @Test
    void reissue_성공() {
        // given
        var 토큰 = 로그인(동균());

        // when
        var 응답 = 토큰_재발급_요청(토큰);

        // then
        응답값을_검증한다(응답, 성공);
    }

    @Test
    void refresh토큰이_null일경우_예외발생() {
        // given
        var 토큰 = 로그인(동균());
        var 리프레시_토큰이_없는_토큰 = new Token(토큰.accessToken(), null);

        // when
        var 응답 = 잘못된_토큰_재발급_요청(리프레시_토큰이_없는_토큰);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    @Test
    void refresh토큰이_만료된_경우_예외발생() {
        // given
        var 토큰 = 로그인(은지());
        리프레시_토큰_만료();

        // when
        var 응답 = 잘못된_토큰_재발급_요청(토큰);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    @Test
    void 저장된_refresh_token이_존재하지_않을_경우_예외발생() {
        // given
        var 토큰 = 로그인(원진());
        리프레시_토큰이_서버에_존재하지_않는다();

        // when
        var 응답 = 잘못된_토큰_재발급_요청(토큰);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    @Test
    void 클라이언트와_토큰이_일치하지_않을_경우_예외발생() {
        // given
        var 토큰 = 로그인(은지());
        var 뒤섞인_토큰 = new Token(토큰.accessToken(), 토큰.refreshToken() + "a");

        // when
        var 응답 = 잘못된_토큰_재발급_요청(뒤섞인_토큰);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    @Test
    void 올바르지_않은_리프레시_토큰의_경우_예외발생() {
        // given
        var 토큰 = 로그인(원진());
        var 올바르지_않은_리프레시_토큰이_담긴_토큰 = new Token(토큰.accessToken(), "올바르지 않은 리프레시 토큰");

        // when
        var 응답 = 잘못된_토큰_재발급_요청(올바르지_않은_리프레시_토큰이_담긴_토큰);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    @Test
    void 리프레시_토큰이_Bearer_타입이_아닌경우_예외발생() {
        // given
        var 토큰 = 로그인(은지());

        // when
        var 응답 = Bearer_타입이_아닌_토큰_재발급_요청(토큰);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    private void 리프레시_토큰_만료() {
        willThrow(new JwtTokenException(JwtTokenExceptionType.EXPIRED_JWT_TOKEN)).given(tokenDecrypter)
                .decryptRefreshToken(anyString());
    }

    private void 리프레시_토큰이_서버에_존재하지_않는다() {
        given(tokenStorage.findRefreshToken(anyString())).willReturn(null);
    }
}
