package com.baro.memofolder.infrastructure;

import com.baro.member.domain.Member;
import com.baro.memofolder.domain.MemoFolder;
import com.baro.memofolder.domain.MemoFolderName;
import com.baro.memofolder.domain.MemoFolderRepository;
import com.baro.memofolder.exception.MemoFolderException;
import com.baro.memofolder.exception.MemoFolderExceptionType;
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

    @Override
    public List<MemoFolder> findAllByMember(Member member) {
        return memoFolderJpaRepository.findAllByMember(member);
    }

    @Override
    public MemoFolder getById(Long id) {
        return memoFolderJpaRepository.findById(id)
                .orElseThrow(() -> new MemoFolderException(MemoFolderExceptionType.NOT_EXIST_MEMO_FOLDER));
    }

    @Override
    public void delete(MemoFolder memoFolder) {
        memoFolderJpaRepository.delete(memoFolder);
    }

    @Override
    public MemoFolder getByMemberIdAndIsDefaultTrue(Long memberId) {
        return memoFolderJpaRepository.findByMemberIdAndIsDefaultTrue(memberId)
                .orElseThrow(() -> new MemoFolderException(MemoFolderExceptionType.NOT_EXIST_MEMO_FOLDER));
    }

    @Override
    public void deleteAllByMember(Member member) {
        memoFolderJpaRepository.deleteAllByMember(member);
    }
}
