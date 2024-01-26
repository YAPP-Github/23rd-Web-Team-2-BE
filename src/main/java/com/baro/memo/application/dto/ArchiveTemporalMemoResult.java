package com.baro.memo.application.dto;

import com.baro.archive.domain.Archive;

public record ArchiveTemporalMemoResult(
        Long id
) {

    public static ArchiveTemporalMemoResult from(Archive archive) {
        return new ArchiveTemporalMemoResult(archive.getId());
    }
}
