package com.baro.memo.infrastructure;

import com.baro.memo.domain.TemporalMemo;
import com.baro.memo.domain.TemporalMemoRepository;
import com.baro.memo.exception.MemoException;
import com.baro.memo.exception.MemoExceptionType;
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
                .orElseThrow(() -> new MemoException(MemoExceptionType.NOT_EXIST_MEMO));
    }
}
