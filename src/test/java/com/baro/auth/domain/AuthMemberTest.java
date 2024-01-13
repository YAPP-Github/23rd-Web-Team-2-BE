package com.baro.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuthMemberTest {

    @Test
    void AuthMember_동일성_테스트() {
        AuthMember actual = new AuthMember(1000L);
        AuthMember expected = new AuthMember(1000L);
        assertThat(actual).isEqualTo(expected);
    }
}
