package com.baro.auth.application;

import com.baro.auth.domain.Token;
import com.baro.common.auth.AuthMember;
import com.baro.member.domain.Member;

public interface TokenTranslator {
    Token encode(Member member);
    AuthMember decode(String token);
}
