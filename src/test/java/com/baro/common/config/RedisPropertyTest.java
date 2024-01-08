package com.baro.common.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class RedisPropertyTest {

    @Autowired
    private RedisProperty redisProperty;

    @Test
    void Redis_프로퍼티값_바인딩_테스트() {
        assertThat(redisProperty)
                .usingRecursiveAssertion()
                .allFieldsSatisfy(Objects::nonNull);
    }
}
