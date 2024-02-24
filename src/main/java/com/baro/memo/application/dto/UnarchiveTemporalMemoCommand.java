package com.baro.memo.application.dto;

public record UnarchiveTemporalMemoCommand(
        Long memberId,
        Long temporalMemoId
) {

}
