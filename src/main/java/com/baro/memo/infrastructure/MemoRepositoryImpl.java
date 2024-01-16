package com.baro.memo.infrastructure;

import com.baro.memo.domain.Memo;
import com.baro.memo.domain.MemoRepository;
import com.baro.memo.exception.MemoException;
import com.baro.memo.exception.MemoExceptionType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemoRepositoryImpl implements MemoRepository {

    private final MemoJpaRepository memoJpaRepository;

    @Override
    public Memo save(Memo memo) {
        return memoJpaRepository.save(memo);
    }

    @Override
    public List<Memo> findAll() {
        return memoJpaRepository.findAll();
    }

    @Override
    public Memo getById(Long id) {
        return memoJpaRepository.findById(id)
                .orElseThrow(() -> new MemoException(MemoExceptionType.NOT_EXIST_MEMO));
    }
}
