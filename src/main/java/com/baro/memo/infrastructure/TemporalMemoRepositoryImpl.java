package com.baro.memo.infrastructure;

import com.baro.memo.domain.TemporalMemo;
import com.baro.memo.domain.TemporalMemoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TemporalMemoRepositoryImpl implements TemporalMemoRepository {

    private final TemporalMemoJpaRepository temporalMemoJpaRepository;

    @Override
    public TemporalMemo save(TemporalMemo temporalMemo) {
        return temporalMemoJpaRepository.save(temporalMemo);
    }

    @Override
    public List<TemporalMemo> findAll() {
        return temporalMemoJpaRepository.findAll();
    }
}
