package com.baro.archive.infra;

import com.baro.archive.domain.Archive;
import com.baro.archive.domain.ArchiveRepository;
import com.baro.archive.exception.ArchiveException;
import com.baro.archive.exception.ArchiveExceptionType;
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
}
