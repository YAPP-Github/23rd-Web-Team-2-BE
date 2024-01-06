package com.baro.member.application;

import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class MemberCreator {

    private final MemberRepository memberRepository;
    private final NicknameCreator nicknameCreator;

    @Transactional
    public Member create(String name, String email, String oauthId, String oauthType) {
        while (true) {
            String randomNickname = nicknameCreator.create();
            if (!memberRepository.existByNickname(randomNickname)) {
                Member member = Member.builder()
                        .name(name)
                        .email(email)
                        .nickname(randomNickname)
                        .oAuthId(oauthId)
                        .oAuthServiceType(oauthType)
                        .build();
                return memberRepository.save(member);
            }
        }
    }
}
