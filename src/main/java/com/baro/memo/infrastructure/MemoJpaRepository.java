package com.baro.memo.infrastructure;

import com.baro.memo.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoJpaRepository extends JpaRepository<Memo, Long> {
}
