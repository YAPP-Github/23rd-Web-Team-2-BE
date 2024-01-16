package com.baro.memo.infrastructure;

import com.baro.memo.domain.TemporalMemo;
import com.baro.memo.domain.TemporalMemoRepository;
import com.baro.memo.exception.TemporalMemoException;
import com.baro.memo.exception.TemporalMemoExceptionType;
import java.time.LocalDateTime;
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

    @Override
    public TemporalMemo getById(Long id) {
        return temporalMemoJpaRepository.findById(id)
                .orElseThrow(() -> new TemporalMemoException(TemporalMemoExceptionType.NOT_EXIST_TEMPORAL_MEMO));
    }

    @Override
    public void delete(TemporalMemo temporalMemo) {
        temporalMemoJpaRepository.delete(temporalMemo);
    }

    @Override
    public List<TemporalMemo> findAllByMemberIdAndCreatedAtBetween(Long memberId, LocalDateTime start,
                                                                   LocalDateTime end) {
        return temporalMemoJpaRepository.findAllByMemberIdAndCreatedAtBetween(memberId, start, end);
    }
}
