package com.baro.memo.application.dto;

import com.baro.memo.domain.Memo;

public record ArchiveTemporalMemoResult(
        Long id
) {

    public static ArchiveTemporalMemoResult from(Memo memo) {
        return new ArchiveTemporalMemoResult(memo.getId());
    }
}
