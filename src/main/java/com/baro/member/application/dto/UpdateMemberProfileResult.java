package com.baro.member.application.dto;

import com.baro.member.domain.Member;

public record UpdateMemberProfileResult(
        Long id,
        String name,
        String nickname
) {

    public static UpdateMemberProfileResult from(Member member) {
        return new UpdateMemberProfileResult(
                member.getId(),
                member.getName(),
                member.getNickname().value()
        );
    }
}
