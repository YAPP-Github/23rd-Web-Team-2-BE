package com.baro.memo.application.dto;

import com.baro.memo.domain.TemporalMemo;
import java.time.LocalDateTime;

public record FindTemporalMemoResult(
        Long id,
        String content,
        String styledCorrectionContent,
        Boolean isCorrected,
        Boolean isArchived,
        LocalDateTime createdAt
) {

    public static FindTemporalMemoResult from(TemporalMemo temporalMemo) {
        return new FindTemporalMemoResult(
                temporalMemo.getId(),
                temporalMemo.getContent().value(),
                temporalMemo.getStyledCorrectionContent() == null ? null : temporalMemo.getStyledCorrectionContent(),
                temporalMemo.isCorrected(),
                temporalMemo.isArchived(),
                temporalMemo.getCreatedAt()
        );
    }
}
