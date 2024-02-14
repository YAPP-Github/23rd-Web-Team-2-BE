package com.baro.memofolder.infrastructure;

import com.baro.member.domain.Member;
import com.baro.memofolder.domain.MemoFolder;
import com.baro.memofolder.domain.MemoFolderName;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoFolderJpaRepository extends JpaRepository<MemoFolder, Long> {

    boolean existsByMemberAndName(Member member, MemoFolderName name);

    List<MemoFolder> findAllByMember(Member member);

    Optional<MemoFolder> findByMemberIdAndIsDefaultTrue(Long memberId);

    void deleteAllByMember(Member member);
}
