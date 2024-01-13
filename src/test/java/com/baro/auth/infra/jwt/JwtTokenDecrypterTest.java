package com.baro.auth.infra.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.baro.auth.application.TokenCreator;
import com.baro.auth.application.TokenDecrypter;
import com.baro.auth.domain.Token;
import com.baro.auth.exception.jwt.JwtTokenException;
import com.baro.auth.exception.jwt.JwtTokenExceptionType;
import com.baro.common.time.TimeServer;
import com.baro.common.time.fake.FakeTimeServer;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JwtTokenDecrypterTest {

    JwtProperty jwtProperty;
    TimeServer timeServer;
    JwtTokenDecrypter decrypter;
    JwtTokenCreator creator;
    String accessTokenSecretKey = "testAccessTokenSecretKeytestAccessTokenSecretKey";
    String refreshTokenSecretKey = "testRefreshTokenSecrettestRefreshTokenSecret";

    @BeforeEach
    void setUp() {
        jwtProperty = new JwtProperty("Bearer", accessTokenSecretKey, refreshTokenSecretKey, 86400000L, 86400000L);
        timeServer = new FakeTimeServer(Instant.parse("2024-01-01T13:00:00.00Z"));
        decrypter = new JwtTokenDecrypter(jwtProperty);
        creator = new JwtTokenCreator(jwtProperty);
    }

    @Nested
    class AccessToken {
        @Test
        void 정상적인_액세스토큰에_대해_복호화한다() {
            // given
            Token token = creator.createToken(1L, timeServer.now());

            // when
            Long result = decrypter.decryptAccessToken("Bearer " + token.accessToken());

            // then
            assertThat(result).isEqualTo(1L);
        }

        @Test
        void 액세스_토큰_타입이_올바르지_않은경우_예외를_반환한다() {
            // given
            Token token = creator.createToken(1L, timeServer.now());

            // then
            assertThatCode(() -> decrypter.decryptAccessToken("BearBear " + token.accessToken()))
                    .isInstanceOf(JwtTokenException.class)
                    .extracting("exceptionType")
                    .isEqualTo(JwtTokenExceptionType.NOT_BEARER_SCHEME);
        }

        @Test
        void 액세스_토큰이_만료된_경우_예외를_반환한다() {
            // given
            jwtProperty = new JwtProperty("Bearer", accessTokenSecretKey, refreshTokenSecretKey, 1L, 1L);
            TokenDecrypter tokenDecrypter = new JwtTokenDecrypter(jwtProperty);
            TokenCreator tokenCreator = new JwtTokenCreator(jwtProperty);
            Token token = tokenCreator.createToken(1L, timeServer.now());

            // then
            assertThatCode(() -> tokenDecrypter.decryptAccessToken("Bearer " + token.accessToken()))
                    .isInstanceOf(JwtTokenException.class)
                    .extracting("exceptionType")
                    .isEqualTo(JwtTokenExceptionType.EXPIRED_JWT_TOKEN);
        }

        @Test
        void 올바르지않은_액세스_토큰의_경우_예외를_반환한다() {
            // given
            Token token = new Token("invalidAccessToken", "invalidRefreshToken");

            // then
            assertThatCode(() -> decrypter.decryptAccessToken("Bearer " + token.accessToken()))
                    .isInstanceOf(JwtTokenException.class)
                    .extracting("exceptionType")
                    .isEqualTo(JwtTokenExceptionType.INVALID_JWT_TOKEN);
        }

        @Test
        void Authorization_헤더가_없는_경우_예외를_반환한다() {
            // then
            assertThatCode(() -> decrypter.decryptAccessToken(null))
                    .isInstanceOf(JwtTokenException.class)
                    .extracting("exceptionType")
                    .isEqualTo(JwtTokenExceptionType.AUTHORIZATION_NULL);
        }

        @Test
        void Authorization_헤더가_비어있는_경우_예외를_반환한다() {
            // then
            assertThatCode(() -> decrypter.decryptAccessToken(" "))
                    .isInstanceOf(JwtTokenException.class)
                    .extracting("exceptionType")
                    .isEqualTo(JwtTokenExceptionType.NOT_BEARER_SCHEME);
        }
    }

    @Nested
    class RefreshToken {

        @Test
        void 정상적인_리프레시토큰에_대해_복호화한다() {
            // given
            Token token = creator.createToken(1L, timeServer.now());

            // then
            assertThatCode(() -> decrypter.decryptRefreshToken("Bearer " + token.refreshToken()))
                    .doesNotThrowAnyException();
        }

        @Test
        void 리프레시_토큰이_만료된_경우_예외를_반환한다() {
            // given
            jwtProperty = new JwtProperty("Bearer", accessTokenSecretKey, refreshTokenSecretKey, 1L, 1L);
            TokenDecrypter tokenDecrypter = new JwtTokenDecrypter(jwtProperty);
            TokenCreator tokenCreator = new JwtTokenCreator(jwtProperty);
            Token token = tokenCreator.createToken(1L, timeServer.now());

            // then
            assertThatCode(() -> tokenDecrypter.decryptRefreshToken("Bearer " + token.refreshToken()))
                    .isInstanceOf(JwtTokenException.class)
                    .extracting("exceptionType")
                    .isEqualTo(JwtTokenExceptionType.EXPIRED_JWT_TOKEN);
        }

        @Test
        void 올바르지않은_리프레시_토큰의_경우_예외를_반환한다() {
            // given
            Token token = new Token("invalidAccessToken", "invalidRefreshToken");

            // then
            assertThatCode(() -> decrypter.decryptRefreshToken("Bearer " + token.refreshToken()))
                    .isInstanceOf(JwtTokenException.class)
                    .extracting("exceptionType")
                    .isEqualTo(JwtTokenExceptionType.INVALID_JWT_TOKEN);
        }

        @Test
        void 리프레시_토큰이_Bearer_타입이_아닌경우_예외를_반환한다() {
            // given
            Token token = creator.createToken(1L, timeServer.now());

            // then
            assertThatCode(() -> decrypter.decryptRefreshToken(token.refreshToken()))
                    .isInstanceOf(JwtTokenException.class)
                    .extracting("exceptionType")
                    .isEqualTo(JwtTokenExceptionType.NOT_BEARER_SCHEME);
        }
    }
}
