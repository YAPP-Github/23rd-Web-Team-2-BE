package com.baro.auth.infra.jwt;

import com.baro.auth.application.TokenCreator;
import com.baro.auth.application.TokenDecrypter;
import com.baro.auth.domain.Token;
import com.baro.auth.fake.jwt.FakeIdentifierTranslator;
import com.baro.auth.fake.jwt.FakeTokenCreator;
import com.baro.auth.fake.jwt.FakeTokenDecrypter;
import com.baro.common.time.TimeServer;
import com.baro.common.time.fake.FakeTimeServer;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenTranslatorTest {

    TimeServer timeServer = new FakeTimeServer(Instant.parse("2024-01-01T13:00:00.00Z"));
    TokenCreator tokenCreator = new FakeTokenCreator("accessToken", "refreshToken");
    TokenDecrypter tokenDecrypter = new FakeTokenDecrypter(1L, "127.0.0.1");
    IdentifierTranslator identifierTranslator = new FakeIdentifierTranslator("encodedCode", "decodedCode");
    JwtTokenTranslator translator = new JwtTokenTranslator(timeServer, tokenCreator, tokenDecrypter, identifierTranslator);

    @Test
    void id가_주어지면_해당하는_액세스_토큰을_반환한다() {
        // when
        Token token = translator.encode(1L, "127.0.0.1");

        // then
        assertThat(token.accessToken()).isEqualTo("accessToken");
    }

    @Test
    void 액세스_토큰이_주어지면_복호화한다() {
        // when
        Long id = translator.decodeAccessToken("token");

        // then
        assertThat(id).isEqualTo(1L);
    }

    @Test
    void 리프레시_토큰이_주어지면_복호화한다() {
        // when
        String ip = translator.decodeRefreshToken("token");

        // then
        assertThat(ip).isEqualTo("decodedCode");
    }
}
