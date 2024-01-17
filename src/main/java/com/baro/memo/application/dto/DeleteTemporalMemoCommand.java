package com.baro.memo.application.dto;

public record DeleteTemporalMemoCommand(
        Long memberId,
        Long temporalMemoId
) {
}
