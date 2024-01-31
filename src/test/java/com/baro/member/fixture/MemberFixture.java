package com.baro.member.fixture;

import com.baro.member.domain.Member;

public class MemberFixture {

    public static Member memberWithNickname(String nickname) {
        return Member.builder()
                .name("name")
                .email("email")
                .nickname(nickname)
                .oAuthId("oAuthId" + nickname)
                .oAuthServiceType("oAuthServiceType")
                .build();
    }

    public static Member memberWithNicknameAndProfileImage(String nickname, String profileImageUrl) {
        return Member.builder()
                .name("name")
                .email("email")
                .nickname(nickname)
                .oAuthId("oAuthId" + nickname)
                .profileImageUrl(profileImageUrl)
                .oAuthServiceType("oAuthServiceType")
                .build();
    }
}
