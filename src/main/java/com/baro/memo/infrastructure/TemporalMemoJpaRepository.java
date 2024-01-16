package com.baro.memo.infrastructure;

import com.baro.memo.domain.TemporalMemo;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporalMemoJpaRepository extends JpaRepository<TemporalMemo, Long> {

    List<TemporalMemo> findAllByMemberIdAndCreatedAtBetween(Long memberId, LocalDateTime start, LocalDateTime end);
}
