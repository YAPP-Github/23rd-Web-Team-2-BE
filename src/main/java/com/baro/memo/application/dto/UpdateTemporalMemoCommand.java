package com.baro.memo.application.dto;

public record UpdateTemporalMemoCommand(
        Long memberId,
        Long temporalMemoId,
        String content
) {
}
