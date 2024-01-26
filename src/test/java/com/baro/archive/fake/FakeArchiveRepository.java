package com.baro.archive.fake;

import com.baro.archive.domain.Archive;
import com.baro.archive.domain.ArchiveRepository;
import com.baro.archive.exception.ArchiveException;
import com.baro.archive.exception.ArchiveExceptionType;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class FakeArchiveRepository implements ArchiveRepository {

    private final AtomicLong id = new AtomicLong(1);
    private final Map<Long, Archive> archives = new ConcurrentHashMap<>();

    @Override
    public Archive getById(Long id) {
        if (archives.containsKey(id)) {
            return archives.get(id);
        }
        throw new ArchiveException(ArchiveExceptionType.NOT_EXIST_ARCHIVE);
    }

    @Override
    public boolean existsByMemberIdAndTemplateId(Long memberId, Long templateId) {
        return archives.values().stream()
                .anyMatch(archive -> archive.getMember().getId().equals(memberId)
                        && archive.getTemplate().getId().equals(templateId));
    }

    @Override
    public Archive getByMemberIdAndTemplateId(Long memberId, Long templateId) {
        return archives.values().stream()
                .filter(archive -> archive.getMember().getId().equals(memberId)
                        && archive.getTemplate().getId().equals(templateId))
                .findFirst()
                .orElseThrow(() -> new ArchiveException(ArchiveExceptionType.NOT_ARCHIVED_TEMPLATE));
    }

    @Override
    public List<Archive> findAll() {
        return List.copyOf(archives.values());
    }

    @Override
    public Archive save(Archive archive) {
        if (Objects.isNull(archive.getId())) {
            Long archiveId = id.getAndIncrement();
            Archive newArchive = new Archive(archiveId, archive.getMember(), archive.getMemoFolder(),
                    archive.getContent(), archive.getTemplate());
            archives.put(archiveId, newArchive);
            return newArchive;
        }
        archives.put(archive.getId(), archive);
        return archive;
    }

    @Override
    public void delete(Archive archive) {
        archives.remove(archive.getId());
    }

    @Override
    public void deleteAllByMemberIdAndMemoFolderId(Long memberId, Long memoFolderId) {
        archives.values().removeIf(archive -> archive.getMember().getId().equals(memberId)
                && archive.getMemoFolder().getId().equals(memoFolderId));
    }

    @Override
    public List<Archive> findAllByMemberIdAndMemoFolderId(Long memberId, Long memoFolderId) {
        return archives.values().stream()
                .filter(archive -> archive.getMember().getId().equals(memberId)
                        && archive.getMemoFolder().getId().equals(memoFolderId))
                .toList();
    }
}
