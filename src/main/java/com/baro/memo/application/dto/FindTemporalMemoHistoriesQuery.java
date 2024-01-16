package com.baro.memo.application.dto;

import java.time.LocalDateTime;

public record FindTemporalMemoHistoriesQuery(
        Long memberId,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
