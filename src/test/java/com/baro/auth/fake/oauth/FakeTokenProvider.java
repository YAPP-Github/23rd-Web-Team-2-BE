package com.baro.auth.fake.oauth;

import com.baro.auth.application.TokenProvider;
import com.baro.auth.domain.Token;
import com.baro.member.domain.Member;

public class FakeTokenProvider implements TokenProvider {
    @Override
    public Token provide(Member member) {
        return new Token("accessToken", "refreshToken");
    }
}
