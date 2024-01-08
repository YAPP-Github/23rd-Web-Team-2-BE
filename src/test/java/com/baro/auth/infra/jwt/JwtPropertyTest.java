package com.baro.auth.infra.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class JwtPropertyTest {

    @Autowired
    private JwtProperty jwtProperty;

    @Test
    void Jwt_프로퍼티_값_바인딩_테스트() {
        assertNotNull(jwtProperty.bearerType());
        assertNotNull(jwtProperty.accessSecretKey());
        assertNotNull(jwtProperty.refreshSecretKey());
        assertNotNull(jwtProperty.accessTokenExpireTime());
        assertNotNull(jwtProperty.refreshTokenExpireTime());
    }
}
