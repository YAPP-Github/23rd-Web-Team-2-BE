package com.baro.auth.application;

import com.baro.auth.domain.Token;
import com.baro.member.application.MemberCreator;
import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final MemberCreator memberCreator;
    private final TokenTranslator tokenTranslator;

    public Token signIn(String name, String email, String oauthId, String oauthType) {
        Member member = memberRepository.findByOAuthIdAndOAuthServiceType(oauthId, oauthType)
                .orElseGet(() -> memberCreator.create(name, email, oauthId, oauthType));

        Token token = tokenTranslator.encode(member);
        // TODO refresh token 저장
        return token;
    }

    public void signOut(String token) {
        // TODO
    }
}
