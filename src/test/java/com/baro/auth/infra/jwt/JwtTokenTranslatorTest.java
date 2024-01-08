package com.baro.auth.infra.jwt;

import com.baro.auth.application.TokenCreator;
import com.baro.auth.application.TokenDecrypter;
import com.baro.auth.domain.Token;
import com.baro.auth.fake.jwt.FakeTokenCreator;
import com.baro.auth.fake.jwt.FakeTokenDecrypter;
import com.baro.common.time.TimeServer;
import com.baro.common.time.fake.FakeTimeServer;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenTranslatorTest {

    TimeServer timeServer = new FakeTimeServer(Instant.parse("2024-01-01T13:00:00.00Z"));
    TokenCreator tokenCreator = new FakeTokenCreator("accessToken", "refreshToken");
    TokenDecrypter tokenDecrypter = new FakeTokenDecrypter(1L);
    JwtTokenTranslator translator = new JwtTokenTranslator(timeServer, tokenCreator, tokenDecrypter);

    @Test
    void id가_주어지면_해당하는_토큰을_반환한다() {
        // when
        Token token = translator.encode(1L);

        // then
        assertEquals("accessToken", token.accessToken());
    }

    @Test
    void 토큰이_주어지면_복호화한다() {
        // when
        Long id = translator.decode("token");

        // then
        assertEquals(1L, id);
    }
}
