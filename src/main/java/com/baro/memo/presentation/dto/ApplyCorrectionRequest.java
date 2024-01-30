package com.baro.memo.presentation.dto;

public record ApplyCorrectionRequest(
        String correctionContent,
        String styledCorrectionContent
) {
}
