package com.baro.template.application.dto;

import com.baro.archive.domain.Archive;

public record ArchiveTemplateResult(
        Long id
) {

    public static ArchiveTemplateResult from(Archive archive) {
        return new ArchiveTemplateResult(archive.getId());
    }
}
