package com.baro.member.application.dto;

public record DeleteMemberCommand(
        Long memberId,
        String reason
) {
}
