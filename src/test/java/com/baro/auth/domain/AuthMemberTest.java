package com.baro.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class AuthMemberTest {

    @Test
    void AuthMember_동일성_테스트() {
        AuthMember actual = new AuthMember(1000L);
        AuthMember expected = new AuthMember(1000L);
        assertThat(actual).isEqualTo(expected);
    }
}
