package com.baro.memofolder.fake;

import com.baro.member.domain.Member;
import com.baro.memofolder.domain.MemoFolder;
import com.baro.memofolder.domain.MemoFolderName;
import com.baro.memofolder.domain.MemoFolderRepository;
import com.baro.memofolder.exception.MemoFolderException;
import com.baro.memofolder.exception.MemoFolderExceptionType;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class FakeMemoFolderRepository implements MemoFolderRepository {

    private final AtomicLong id = new AtomicLong(1);
    private final Map<Long, MemoFolder> memoFolders = new ConcurrentHashMap<>();

    @Override
    public MemoFolder save(MemoFolder memoFolder) {
        if (Objects.isNull(memoFolder.getId())) {
            MemoFolder newMemoFolder = new MemoFolder(
                    id.getAndIncrement(),
                    memoFolder.getMember(),
                    memoFolder.getName()
            );
            memoFolders.put(newMemoFolder.getId(), newMemoFolder);
            return newMemoFolder;
        }
        memoFolders.put(memoFolder.getId(), memoFolder);
        return memoFolder;
    }

    @Override
    public List<MemoFolder> findAll() {
        return List.copyOf(memoFolders.values());
    }

    @Override
    public boolean existByMemberAndName(Member member, String name) {
        return memoFolders.values().stream()
                .anyMatch(memoFolder -> memoFolder.getMember().getId().equals(member.getId())
                        && memoFolder.getName().equals(MemoFolderName.from(name)));
    }

    @Override
    public List<MemoFolder> findAllByMember(Member member) {
        return memoFolders.values().stream()
                .filter(memoFolder -> memoFolder.getMember().getId().equals(member.getId()))
                .toList();
    }

    @Override
    public MemoFolder getById(Long id) {
        return memoFolders.values().stream()
                .filter(memoFolder -> memoFolder.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new MemoFolderException(MemoFolderExceptionType.NOT_EXIST_MEMO_FOLDER));
    }
}
