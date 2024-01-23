package com.baro.memo.batch;

import com.baro.memo.domain.TemporalMemoRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TemporalMemoScheduler {

    private static final LocalDateTime A_WEEK_BEFORE = LocalDate.now().minusDays(7L).atStartOfDay();

    private final TemporalMemoRepository temporalMemoRepository;

    @Async
    @Scheduled(cron = "0 0 0,12 * * ?", zone = "Asia/Seoul") // 매일 0시, 12시에 실행
    public void deleteTemporalMemo() {
        log.info("delete temporal memo job started");
        temporalMemoRepository.deleteAllByCreatedAtLessThanEqual(A_WEEK_BEFORE);
        log.info("delete temporal memo job finished");
    }
}
