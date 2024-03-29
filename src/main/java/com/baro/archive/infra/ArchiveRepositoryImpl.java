package com.baro.archive.infra;

import com.baro.archive.domain.Archive;
import com.baro.archive.domain.ArchiveRepository;
import com.baro.archive.exception.ArchiveException;
import com.baro.archive.exception.ArchiveExceptionType;
import com.baro.member.domain.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ArchiveRepositoryImpl implements ArchiveRepository {

    private final ArchiveJpaRepository archiveJpaRepository;

    @Override
    public Archive getById(Long id) {
        return archiveJpaRepository.findById(id)
                .orElseThrow(() -> new ArchiveException(ArchiveExceptionType.NOT_EXIST_ARCHIVE));
    }

    @Override
    public boolean existsByMemberIdAndTemplateId(Long memberId, Long templateId) {
        return archiveJpaRepository.existsByMemberIdAndTemplateId(memberId, templateId);
    }

    @Override
    public Archive getByMemberIdAndTemplateId(Long memberId, Long templateId) {
        return archiveJpaRepository.findByMemberIdAndTemplateId(memberId, templateId)
                .orElseThrow(() -> new ArchiveException(ArchiveExceptionType.NOT_ARCHIVED_TEMPLATE));
    }

    @Override
    public List<Archive> findAll() {
        return archiveJpaRepository.findAll();
    }

    @Override
    public Archive save(Archive archive) {
        return archiveJpaRepository.save(archive);
    }

    @Override
    public void delete(Archive archive) {
        archiveJpaRepository.delete(archive);
    }

    @Override
    public void deleteAllByMemberIdAndMemoFolderId(Long memberId, Long memoFolderId) {
        archiveJpaRepository.deleteAllByMemberIdAndMemoFolderId(memberId, memoFolderId);
    }

    @Override
    public List<Archive> findAllByMemberIdAndMemoFolderId(Long memberId, Long memoFolderId) {
        return archiveJpaRepository.findAllByMemberIdAndMemoFolderId(memberId, memoFolderId);
    }

    @Override
    public void deleteAllByMember(Member member) {
        archiveJpaRepository.deleteAllByMember(member);
    }

    @Override
    public List<Archive> findAllTemplates(Long memberId) {
        return archiveJpaRepository.findAllByMemberIdAndTemplateIdIsNotNull(memberId);
    }

    @Override
    public List<Archive> findAllArchives(Long memberId, Long folderId) {
        return archiveJpaRepository.findAllByMemberIdAndMemoFolderId(memberId, folderId);
    }

    @Override
    public List<Archive> findAllArchivedMemos(Long memberId, Long folderId) {
        return archiveJpaRepository.findAllByMemberIdAndMemoFolderIdAndTemplateIdIsNull(memberId, folderId);
    }

    @Override
    public List<Archive> findAllArchivedTemplates(Long memberId, Long folderId) {
        return archiveJpaRepository.findAllByMemberIdAndMemoFolderIdAndTemplateIdIsNotNull(memberId, folderId);
    }
}
