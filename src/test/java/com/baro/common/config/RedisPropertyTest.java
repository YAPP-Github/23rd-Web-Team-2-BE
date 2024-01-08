package com.baro.common.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RedisPropertyTest {

    @Autowired
    private RedisProperty property;

    @Test
    void Redis_프로퍼티값_바인딩_테스트() {
        assertNotNull(property.host());
        assertNotNull(property.port());
    }
}
