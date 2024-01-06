package com.baro.auth.fake.jwt;

import com.baro.auth.application.TokenTranslator;
import com.baro.auth.domain.Token;
import com.baro.auth.domain.AuthMember;
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
