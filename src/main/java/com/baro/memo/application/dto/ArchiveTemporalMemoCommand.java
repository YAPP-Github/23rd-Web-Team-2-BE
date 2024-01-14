package com.baro.memo.application.dto;

public record ArchiveTemporalMemoCommand(
        Long memberId,
        Long temporalMemoId,
        Long memoFolderId
) {
}
