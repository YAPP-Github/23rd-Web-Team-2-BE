package com.baro.archive.presentation.dto;

public record GetArchiveRequest(
        Long folderId,
        String tabName
) {
}
