package com.baro.memo.fake;

import com.baro.memo.domain.TemporalMemo;
import com.baro.memo.domain.TemporalMemoRepository;
import com.baro.memo.exception.TemporalMemoException;
import com.baro.memo.exception.TemporalMemoExceptionType;
import java.time.LocalDateTime;
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
            TemporalMemo newTemporalMemo = new TemporalMemo(
                    id.getAndIncrement(),
                    temporalMemo.getMember(),
                    temporalMemo.getContent(),
                    temporalMemo.getCorrectionContent(),
                    temporalMemo.getMemo()
            );
            if (Objects.nonNull(temporalMemo.getCreatedAt())) {
                newTemporalMemo.setCreatedAtForTest(temporalMemo.getCreatedAt());
            }
            temporalMemos.put(newTemporalMemo.getId(), newTemporalMemo);
            return newTemporalMemo;
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

    @Override
    public void delete(TemporalMemo temporalMemo) {
        this.getById(temporalMemo.getId());
        temporalMemos.remove(temporalMemo.getId());
    }

    @Override
    public List<TemporalMemo> findAllByMemberIdAndCreatedAtBetween(Long memberId, LocalDateTime start,
                                                                   LocalDateTime end) {
        return temporalMemos.values().stream()
                .filter(temporalMemo -> temporalMemo.getMember().getId().equals(memberId))
                .filter(temporalMemo ->
                        temporalMemo.getCreatedAt().isAfter(start) || temporalMemo.getCreatedAt().isEqual(start))
                .filter(temporalMemo ->
                        temporalMemo.getCreatedAt().isBefore(end) || temporalMemo.getCreatedAt().isEqual(end))
                .toList();
    }

    @Override
    public void deleteAllByCreatedAtLessThanEqual(LocalDateTime weekBefore) {
        temporalMemos.values().stream()
                .filter(temporalMemo -> temporalMemo.getCreatedAt().isBefore(weekBefore) || temporalMemo.getCreatedAt()
                        .isEqual(weekBefore))
                .map(TemporalMemo::getId)
                .forEach(temporalMemos::remove);
    }
}
