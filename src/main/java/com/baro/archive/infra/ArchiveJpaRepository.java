package com.baro.archive.infra;

import com.baro.archive.domain.Archive;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchiveJpaRepository extends JpaRepository<Archive, Long> {

    boolean existsByMemberIdAndTemplateId(Long memberId, Long templateId);

    Optional<Archive> findByMemberIdAndTemplateId(Long memberId, Long templateId);
}
