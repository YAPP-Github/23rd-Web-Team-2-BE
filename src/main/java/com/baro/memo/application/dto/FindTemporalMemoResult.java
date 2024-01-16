package com.baro.memo.application.dto;

import com.baro.memo.domain.TemporalMemo;
import java.time.LocalDateTime;

public record FindTemporalMemoResult(
        Long id,
        String content,
        String correctionContent,
        Boolean isCorrected,
        Boolean isArchived,
        LocalDateTime createdAt
) {

    public static FindTemporalMemoResult from(TemporalMemo temporalMemo) {
        return new FindTemporalMemoResult(
                temporalMemo.getId(),
                temporalMemo.getContent().value(),
                temporalMemo.getCorrectionContent() == null ? null : temporalMemo.getCorrectionContent().value(),
                temporalMemo.isCorrected(),
                temporalMemo.isArchived(),
                temporalMemo.getCreatedAt()
        );
    }
}
