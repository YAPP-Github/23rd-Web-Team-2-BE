package com.baro.auth.infra.jwt;

import com.baro.auth.application.TokenCreator;
import com.baro.auth.application.TokenDecrypter;
import com.baro.auth.domain.Token;
import com.baro.auth.exception.jwt.JwtTokenException;
import com.baro.common.time.TimeServer;
import com.baro.common.time.fake.FakeTimeServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtTokenDecrypterTest {

    JwtProperty jwtProperty;
    TimeServer timeServer;
    JwtTokenDecrypter decrypter;
    JwtTokenCreator creator;
    String accessTokenSecretKey = "testAccessTokenSecretKeytestAccessTokenSecretKey";
    String refreshTokenSecretKey = "testRefreshTokenSecrettestRefreshTokenSecret";
    String ipSecretKey = "testIpScretKeytestIpScretKeytest";

    @BeforeEach
    void setUp() {
        jwtProperty = new JwtProperty("Bearer", accessTokenSecretKey, refreshTokenSecretKey, ipSecretKey,
                86400000L, 86400000L);
        timeServer = new FakeTimeServer(Instant.parse("2024-01-01T13:00:00.00Z"));
        decrypter = new JwtTokenDecrypter(jwtProperty);
        creator = new JwtTokenCreator(jwtProperty);
    }

    @Nested
    class AccessToken {
        @Test
        void 정상적인_액세스토큰에_대해_복호화한다() {
            // given
            String ipAddress = "127.0.0.1";
            Token token = creator.createToken(1L, ipAddress, timeServer.now());

            // when
            Long result = decrypter.decryptAccessToken("Bearer " + token.accessToken());

            // then
            assertThat(result).isEqualTo(1L);
        }

        @Test
        void 액세스_토큰_타입이_올바르지_않은경우_예외를_반환한다() {
            // given
            String ipAddress = "127.0.0.1";
            Token token = creator.createToken(1L, ipAddress, timeServer.now());

            // then
            assertThrows(JwtTokenException.class, () -> decrypter.decryptAccessToken("BearBear " + token.accessToken()));
        }

        @Test
        void 액세스_토큰이_만료된_경우_예외를_반환한다() {
            // given
            String ipAddress = "127.0.0.1";
            jwtProperty = new JwtProperty("Bearer", accessTokenSecretKey, refreshTokenSecretKey, ipSecretKey,
                    1L, 1L);
            TokenDecrypter tokenDecrypter = new JwtTokenDecrypter(jwtProperty);
            TokenCreator tokenCreator = new JwtTokenCreator(jwtProperty);
            Token token = tokenCreator.createToken(1L, ipAddress, timeServer.now());

            // then
            assertThrows(JwtTokenException.class, () -> tokenDecrypter.decryptAccessToken("Bearer " + token.accessToken()));
        }

        @Test
        void 올바르지않은_액세스_토큰의_경우_예외를_반환한다() {
            // given
            Token token = new Token("invalidAccessToken", "invalidRefreshToken");

            // then
            assertThrows(JwtTokenException.class, () -> decrypter.decryptAccessToken("Bearer " + token.accessToken()));
        }

        @Test
        void Authorization_헤더가_없는_경우_예외를_반환한다() {
            // then
            assertThrows(JwtTokenException.class, () -> decrypter.decryptAccessToken(null));
        }

        @Test
        void Authorization_헤더가_비어있는_경우_예외를_반환한다() {
            // then
            assertThrows(JwtTokenException.class, () -> decrypter.decryptAccessToken(" "));
        }
    }

    @Nested
    class RefreshToken {
        @Test
        void 정상적인_리프레시토큰에_대해_복호화한다() {
            // given
            String ipAddress = "127.0.0.1";
            Token token = creator.createToken(1L, ipAddress, timeServer.now());

            // when
            String result = decrypter.decryptRefreshToken(token.refreshToken());

            // then
            assertThat(result).isEqualTo(ipAddress);
        }

        @Test
        void 리프레시_토큰이_만료된_경우_예외를_반환한다() {
            // given
            String ipAddress = "127.0.0.1";
            jwtProperty = new JwtProperty("Bearer", accessTokenSecretKey, refreshTokenSecretKey, ipSecretKey,
                    1L, 1L);
            TokenDecrypter tokenDecrypter = new JwtTokenDecrypter(jwtProperty);
            TokenCreator tokenCreator = new JwtTokenCreator(jwtProperty);
            Token token = tokenCreator.createToken(1L, ipAddress, timeServer.now());

            // then
            assertThrows(JwtTokenException.class, () -> tokenDecrypter.decryptRefreshToken(token.refreshToken()));
        }

        @Test
        void 올바르지않은_리프레시_토큰의_경우_예외를_반환한다() {
            // given
            Token token = new Token("invalidAccessToken", "invalidRefreshToken");

            // then
            assertThrows(JwtTokenException.class, () -> decrypter.decryptRefreshToken(token.refreshToken()));
        }
    }
}
