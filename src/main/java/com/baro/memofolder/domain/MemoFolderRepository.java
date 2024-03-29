package com.baro.memofolder.domain;

import com.baro.member.domain.Member;
import java.util.List;

public interface MemoFolderRepository {

    MemoFolder save(MemoFolder memoFolder);

    List<MemoFolder> findAll();

    boolean existByMemberAndName(Member member, String name);

    List<MemoFolder> findAllByMember(Member member);

    MemoFolder getById(Long id);

    void delete(MemoFolder memoFolder);

    MemoFolder getByMemberIdAndIsDefaultTrue(Long memberId);

    void deleteAllByMember(Member member);
}
