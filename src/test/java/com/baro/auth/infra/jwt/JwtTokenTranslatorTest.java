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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class JwtTokenTranslatorTest {

    TimeServer timeServer = new FakeTimeServer(Instant.parse("2024-01-01T13:00:00.00Z"));
    TokenCreator tokenCreator = new FakeTokenCreator("accessToken", "refreshToken");
    TokenDecrypter tokenDecrypter = new FakeTokenDecrypter();
    JwtTokenTranslator translator = new JwtTokenTranslator(timeServer, tokenCreator, tokenDecrypter);

    @Test
    void id가_주어지면_해당하는_액세스_토큰을_반환한다() {
        // when
        Token token = translator.encode(1L);

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
    void 정상적인_리프레시_토큰_복호화중_토큰만료_등의_예외가_발생하지_않는다() {
        assertThatCode(() -> translator.decodeRefreshToken("token"))
                .doesNotThrowAnyException();
    }
}
