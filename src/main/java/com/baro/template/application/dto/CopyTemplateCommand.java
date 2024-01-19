package com.baro.template.application.dto;

public record CopyTemplateCommand(
        Long memberId,
        Long templateId
) {
}
