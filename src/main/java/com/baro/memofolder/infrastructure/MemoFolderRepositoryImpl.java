package com.baro.memofolder.infrastructure;

import com.baro.member.domain.Member;
import com.baro.memofolder.domain.MemoFolder;
import com.baro.memofolder.domain.MemoFolderName;
import com.baro.memofolder.domain.MemoFolderRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MemoFolderRepositoryImpl implements MemoFolderRepository {

    private final MemoFolderJpaRepository memoFolderJpaRepository;

    @Override
    public MemoFolder save(MemoFolder memoFolder) {
        return memoFolderJpaRepository.save(memoFolder);
    }

    @Override
    public List<MemoFolder> findAll() {
        return memoFolderJpaRepository.findAll();
    }

    @Override
    public boolean existByMemberAndName(Member member, String name) {
        return memoFolderJpaRepository.existsByMemberAndName(member, MemoFolderName.from(name));
    }
}
