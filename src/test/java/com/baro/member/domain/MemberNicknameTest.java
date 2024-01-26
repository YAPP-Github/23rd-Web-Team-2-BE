package com.baro.member.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.baro.member.exception.MemberException;
import com.baro.member.exception.MemberExceptionType;
import org.junit.jupiter.api.Test;

class MemberNicknameTest {

    @Test
    void 닉네임_생성() {
        // given
        String nickname = "바로".repeat(15);

        // when & then
        assertThatCode(() -> MemberNickname.from(nickname))
                .doesNotThrowAnyException();
    }

    @Test
    void 닉네임_생성_최대_길이_초과() {
        // given
        String nickname = "바".repeat(31);

        // when & then
        assertThatThrownBy(() -> MemberNickname.from(nickname))
                .isInstanceOf(MemberException.class)
                .extracting("exceptionType")
                .isEqualTo(MemberExceptionType.OVER_MAX_SIZE_NICKNAME);
    }
}
