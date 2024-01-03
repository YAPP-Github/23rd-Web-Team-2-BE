package com.baro.auth.fake.oauth;

import com.baro.auth.application.TokenTranslator;
import com.baro.auth.domain.Token;
import com.baro.common.auth.AuthMember;
import com.baro.member.domain.Member;

public class FakeTokenTranslator implements TokenTranslator {

    @Override
    public Token encode(Member member) {
        return new Token("accessToken", "refreshToken");
    }

    @Override
    public AuthMember decode(String token) {
        return new AuthMember(1L);
    }
}
