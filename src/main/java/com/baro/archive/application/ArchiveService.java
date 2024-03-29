package com.baro.archive.application;

import com.baro.archive.application.dto.ArchiveUnitResult;
import com.baro.archive.application.dto.DeleteArchiveCommand;
import com.baro.archive.application.dto.GetArchiveQuery;
import com.baro.archive.application.dto.ModifyArchiveCommand;
import com.baro.archive.domain.Archive;
import com.baro.archive.domain.ArchiveRepository;
import com.baro.archive.exception.ArchiveException;
import com.baro.archive.exception.ArchiveExceptionType;
import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import com.baro.memo.domain.MemoContent;
import com.baro.memo.domain.TemporalMemo;
import com.baro.memo.domain.TemporalMemoRepository;
import com.baro.memofolder.domain.MemoFolder;
import com.baro.memofolder.domain.MemoFolderRepository;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ArchiveService {

    private final MemberRepository memberRepository;
    private final MemoFolderRepository memoFolderRepository;
    private final ArchiveRepository archiveRepository;
    private final TemporalMemoRepository temporalMemoRepository;

    public List<ArchiveUnitResult> getArchive(GetArchiveQuery query) {
        Member member = memberRepository.getById(query.memberId());
        MemoFolder memoFolder = memoFolderRepository.getById(query.folderId());
        memoFolder.matchOwner(member.getId());

        Comparator<Archive> comparator = Comparator.comparing(Archive::getCreatedAt).reversed();

        return switch (query.tab()) {
            case ALL -> archiveRepository.findAllArchives(query.memberId(), query.folderId())
                    .stream().sorted(comparator).map(ArchiveUnitResult::of).collect(Collectors.toList());
            case MEMO -> archiveRepository.findAllArchivedMemos(query.memberId(), query.folderId())
                    .stream().sorted(comparator).map(ArchiveUnitResult::of).collect(Collectors.toList());
            case TEMPLATE -> archiveRepository.findAllArchivedTemplates(query.memberId(), query.folderId())
                    .stream().sorted(comparator).map(ArchiveUnitResult::of).collect(Collectors.toList());
        };
    }

    public void modifyArchive(ModifyArchiveCommand command) {
        Archive archive = archiveRepository.getById(command.archiveId());
        if (!archive.isMemo()) {
            throw new ArchiveException(ArchiveExceptionType.CANT_MODIFY_TEMPLATE);
        }
        archive.matchOwner(command.memberId());
        archive.modifyContent(MemoContent.from(command.content()));
    }

    public void deleteArchive(DeleteArchiveCommand command) {
        Archive archive = archiveRepository.getById(command.archiveId());
        archive.matchOwner(command.memberId());
        archiveRepository.delete(archive);
        if (archive.isMemo()) {
            TemporalMemo temporalMemo = temporalMemoRepository.getByArchiveId(archive.getId());
            temporalMemo.unarchive();
        }
    }
}
