package com.baro.archive.application.dto;

public record DeleteArchiveCommand(
        Long memberId,
        Long archiveId
) {
}
