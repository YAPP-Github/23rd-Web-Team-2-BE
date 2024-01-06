package com.baro.auth.infra.jwt;

import com.baro.auth.domain.Token;
import com.baro.auth.exception.jwt.JwtTokenException;
import com.baro.common.time.TimeServer;
import com.baro.common.time.fake.FakeTimeServer;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void setUp() {
        jwtProperty = new JwtProperty("Bearer", accessTokenSecretKey, refreshTokenSecretKey, 86400000L, 86400000L);
        timeServer = new FakeTimeServer(Instant.parse("2024-01-01T13:00:00.00Z"));
        decrypter = new JwtTokenDecrypter(jwtProperty);
        creator = new JwtTokenCreator(jwtProperty);
    }

    @Test
    void 정상적인_액세스토큰에_대해_복호화한다() {

        // given
        Token token = creator.createToken(1L, timeServer.now());

        // when
        Long result = decrypter.decrypt("Bearer " + token.accessToken());

        // then
        assertThat(result).isEqualTo(1L);
    }

    @Test
    void 토큰타입이_올바르지_않은경우_예외를_반환한다() {

        // given
        Token token = creator.createToken(1L, timeServer.now());

        // then
        assertThrows(JwtTokenException.class, () -> decrypter.decrypt("BearBear " + token.accessToken()));
    }

    @Test
    void 토큰이_만료된_경우_예외를_반환한다() {

        // given
        jwtProperty = new JwtProperty("Bearer", accessTokenSecretKey, refreshTokenSecretKey, 1L, 1L);
        Token token = creator.createToken(1L, timeServer.now());

        // then
        assertThrows(JwtTokenException.class, () -> decrypter.decrypt("BearBear " + token.accessToken()));
    }

    @Test
    void 올바르지않은_토큰의_경우_예외를_반환한다() {

        // given
        Token token = new Token("invalidAccessToken", "invalidRefreshToken");

        // then
        assertThrows(JwtTokenException.class, () -> decrypter.decrypt("BearBear " + token.accessToken()));
    }
}
