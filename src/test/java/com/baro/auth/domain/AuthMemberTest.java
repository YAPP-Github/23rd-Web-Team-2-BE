package com.baro.auth.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
class AuthMemberTest {

    @Test
    void AuthMember_동일성_테스트() {
        assertEquals(new AuthMember(1000L), new AuthMember(1000L));
    }
}
