package com.baro.member.application.dto;

import com.baro.member.domain.Member;

public record GetMemberProfileResult(
        Long id,
        String name,
        String nickname,
        String profileImageUrl,
        String email,
        String oAuthServiceType
) {

    public static GetMemberProfileResult from(Member member) {
        return new GetMemberProfileResult(
                member.getId(),
                member.getName(),
                member.getNickname(),
                member.getProfileImageUrl(),
                member.getEmail(),
                member.getOAuthServiceType()
        );
    }
}
