package com.baro.memo.application.dto;

import com.baro.memo.domain.TemporalMemo;

public record SaveTemporalMemoResult(
        Long id
) {

    public static SaveTemporalMemoResult from(TemporalMemo temporalMemo) {
        return new SaveTemporalMemoResult(temporalMemo.getId());
    }
}
