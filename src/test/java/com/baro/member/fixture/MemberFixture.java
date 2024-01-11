package com.baro.member.fixture;

import com.baro.member.domain.Member;

public class MemberFixture {

    public static Member memberWithNickname(String nickname) {
        return Member.builder()
                .name("name")
                .email("email")
                .nickname(nickname)
                .oAuthId("oAuthId")
                .oAuthServiceType("oAuthServiceType")
                .build();
    }
}
