package com.baro.memo.application.dto;

import java.time.LocalDate;

public record FindTemporalMemoHistoriesQuery(
        Long memberId,
        LocalDate startDate,
        LocalDate endDate
) {
}
