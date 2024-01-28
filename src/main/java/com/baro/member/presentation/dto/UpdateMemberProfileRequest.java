package com.baro.member.presentation.dto;

public record UpdateMemberProfileRequest(
        String name,
        String nickname
) {
}
