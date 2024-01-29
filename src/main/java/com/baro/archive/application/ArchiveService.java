package com.baro.archive.application;

import com.baro.archive.application.dto.ArchiveUnitResult;
import com.baro.archive.application.dto.GetArchiveQuery;
import com.baro.archive.domain.ArchiveRepository;
import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import com.baro.memofolder.domain.MemoFolder;
import com.baro.memofolder.domain.MemoFolderRepository;
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

    public List<ArchiveUnitResult> getArchive(GetArchiveQuery query) {
        Member member = memberRepository.getById(query.memberId());
        MemoFolder memoFolder = memoFolderRepository.getById(query.folderId());
        memoFolder.matchOwner(member.getId());

        return switch (query.tab()) {
            case ALL -> archiveRepository.findAllArchives(query.memberId(), query.folderId())
                    .stream().map(ArchiveUnitResult::of).collect(Collectors.toList());
            case MEMO -> archiveRepository.findAllArchivedMemos(query.memberId(), query.folderId())
                    .stream().map(ArchiveUnitResult::of).collect(Collectors.toList());
            case TEMPLATE -> archiveRepository.findAllArchivedTemplates(query.memberId(), query.folderId())
                    .stream().map(ArchiveUnitResult::of).collect(Collectors.toList());
        };
    }
}
