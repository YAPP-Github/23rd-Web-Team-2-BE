package com.baro.memo.infrastructure;

import com.baro.memo.domain.TemporalMemo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporalMemoJpaRepository extends JpaRepository<TemporalMemo, Long> {
}
