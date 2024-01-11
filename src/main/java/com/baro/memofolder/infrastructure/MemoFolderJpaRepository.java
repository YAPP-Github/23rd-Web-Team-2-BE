package com.baro.memofolder.infrastructure;

import com.baro.member.domain.Member;
import com.baro.memofolder.domain.MemoFolder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoFolderJpaRepository extends JpaRepository<MemoFolder, Long> {

    boolean existsByMemberAndName(Member member, String name);
}
