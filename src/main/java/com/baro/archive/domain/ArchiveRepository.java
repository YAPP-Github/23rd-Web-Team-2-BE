package com.baro.archive.domain;

import com.baro.member.domain.Member;
import java.util.List;

public interface ArchiveRepository {

    Archive getById(Long id);

    boolean existsByMemberIdAndTemplateId(Long memberId, Long templateId);

    Archive getByMemberIdAndTemplateId(Long memberId, Long templateId);

    List<Archive> findAll();

    Archive save(Archive archive);

    void delete(Archive archive);

    List<Archive> findAllArchives(Long memberId, Long folderId);

    List<Archive> findAllArchivedMemos(Long memberId, Long folderId);

    List<Archive> findAllArchivedTemplates(Long memberId, Long folderId);

    void deleteAllByMemberIdAndMemoFolderId(Long memberId, Long memoFolderId);

    List<Archive> findAllByMemberIdAndMemoFolderId(Long memberId, Long memoFolderId);

    void deleteAllByMember(Member member);

    List<Archive> findAllTemplates(Long memberId);
}
