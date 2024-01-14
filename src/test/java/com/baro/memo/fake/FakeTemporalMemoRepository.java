package com.baro.memo.fake;

import com.baro.memo.domain.TemporalMemo;
import com.baro.memo.domain.TemporalMemoRepository;
import com.baro.memo.exception.TemporalMemoException;
import com.baro.memo.exception.TemporalMemoExceptionType;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class FakeTemporalMemoRepository implements TemporalMemoRepository {

    private final AtomicLong id = new AtomicLong(1);
    private final Map<Long, TemporalMemo> temporalMemos = new ConcurrentHashMap<>();

    @Override
    public TemporalMemo save(TemporalMemo temporalMemo) {
        if (Objects.isNull(temporalMemo.getId())) {
            TemporalMemo newMemoFolder = new TemporalMemo(
                    id.getAndIncrement(),
                    temporalMemo.getMember(),
                    temporalMemo.getContent(),
                    temporalMemo.getCorrectionContent(),
                    temporalMemo.getMemo()
            );
            temporalMemos.put(newMemoFolder.getId(), newMemoFolder);
            return newMemoFolder;
        }
        temporalMemos.put(temporalMemo.getId(), temporalMemo);
        return temporalMemo;
    }

    @Override
    public List<TemporalMemo> findAll() {
        return List.copyOf(temporalMemos.values());
    }

    @Override
    public TemporalMemo getById(Long id) {
        return temporalMemos.values().stream()
                .filter(temporalMemo -> temporalMemo.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new TemporalMemoException(TemporalMemoExceptionType.NOT_EXIST_TEMPORAL_MEMO));
    }
}
