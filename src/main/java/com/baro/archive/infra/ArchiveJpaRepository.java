package com.baro.archive.infra;

import com.baro.archive.domain.Archive;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArchiveJpaRepository extends JpaRepository<Archive, Long> {

    boolean existsByMemberIdAndTemplateId(Long memberId, Long templateId);

    Optional<Archive> findByMemberIdAndTemplateId(Long memberId, Long templateId);

    void deleteAllByMemberIdAndMemoFolderId(Long memberId, Long memoFolderId);

    List<Archive> findAllByMemberIdAndMemoFolderId(Long memberId, Long memoFolderId);

    List<Archive> findAllByMemberIdAndMemoFolderIdAndTemplateIdIsNull(Long memberId, Long folderId);

    List<Archive> findAllByMemberIdAndMemoFolderIdAndTemplateIdIsNotNull(Long memberId, Long folderId);

    List<Archive> findAllByMemberIdAndTemplateIdIsNotNull(Long memberId);
}
