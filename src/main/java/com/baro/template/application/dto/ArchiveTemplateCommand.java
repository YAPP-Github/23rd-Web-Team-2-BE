package com.baro.template.application.dto;

public record ArchiveTemplateCommand(
        Long memberId,
        Long templateId,
        Long memoFolderId
) {
}
