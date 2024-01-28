package com.baro.member.application.dto;

public record UpdateMemberProfileCommand(
        Long id,
        String name,
        String nickname
) {
}
