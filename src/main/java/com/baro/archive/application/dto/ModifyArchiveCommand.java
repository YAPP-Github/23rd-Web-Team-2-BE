package com.baro.archive.application.dto;

public record ModifyArchiveCommand(
        Long memberId,
        Long archiveId,
        String content
) {
}
