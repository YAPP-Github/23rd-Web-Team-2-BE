package com.baro.memo.application.dto;

public record SaveTemporalMemoCommand(
        Long memberId,
        String content
) {
}
