package com.baro.archive.application.dto;

import com.baro.archive.domain.ArchiveTab;

public record GetArchiveQuery(
        Long memberId,
        Long folderId,
        ArchiveTab tab
) {
}
