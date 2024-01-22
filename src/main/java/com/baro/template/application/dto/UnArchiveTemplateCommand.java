package com.baro.template.application.dto;

public record UnArchiveTemplateCommand(
        Long memberId,
        Long templateId
) {
}
