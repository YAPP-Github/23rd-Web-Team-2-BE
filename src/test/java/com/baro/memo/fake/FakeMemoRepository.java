package com.baro.memo.fake;

import com.baro.memo.domain.Memo;
import com.baro.memo.domain.MemoRepository;
import com.baro.memo.exception.MemoException;
import com.baro.memo.exception.MemoExceptionType;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class FakeMemoRepository implements MemoRepository {

    private final AtomicLong id = new AtomicLong(1);
    private final Map<Long, Memo> memos = new ConcurrentHashMap<>();

    @Override
    public Memo save(Memo memo) {
        if (Objects.isNull(memo.getId())) {
            Memo newMemoFolder = new Memo(
                    id.getAndIncrement(),
                    memo.getMember(),
                    memo.getMemoFolder(),
                    memo.getContent()
            );
            memos.put(newMemoFolder.getId(), newMemoFolder);
            return newMemoFolder;
        }
        memos.put(memo.getId(), memo);
        return memo;
    }

    @Override
    public List<Memo> findAll() {
        return List.copyOf(memos.values());
    }

    @Override
    public Memo getById(Long id) {
        return memos.values().stream()
                .filter(memo -> memo.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new MemoException(MemoExceptionType.NOT_EXIST_MEMO));
    }
}
