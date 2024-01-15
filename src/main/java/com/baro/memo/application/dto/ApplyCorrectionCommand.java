package com.baro.memo.application.dto;

public record ApplyCorrectionCommand(
        Long memberId,
        Long temporalMemoId,
        String contents
) {
}
