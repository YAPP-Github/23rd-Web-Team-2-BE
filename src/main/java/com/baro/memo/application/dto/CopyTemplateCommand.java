package com.baro.memo.application.dto;

public record CopyTemplateCommand(
        Long memberId,
        Long templateId
) {
}
