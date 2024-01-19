package com.baro.memo.application.dto;

import java.time.LocalDate;
import java.util.List;

public record FindTemporalMemoHistoriesResult(
        LocalDate createdAt,
        List<FindTemporalMemoResult> temporalMemos
) {
}
