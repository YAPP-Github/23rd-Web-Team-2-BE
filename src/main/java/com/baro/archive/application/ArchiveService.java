package com.baro.archive.application;

import com.baro.archive.domain.Archive;
import com.baro.archive.domain.ArchiveRepository;
import com.baro.archive.exception.ArchiveException;
import com.baro.archive.exception.ArchiveExceptionType;
import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import com.baro.memo.application.dto.ArchiveTemporalMemoCommand;
import com.baro.memo.application.dto.ArchiveTemporalMemoResult;
import com.baro.memo.domain.MemoContent;
import com.baro.memo.domain.TemporalMemo;
import com.baro.memo.domain.TemporalMemoRepository;
import com.baro.memofolder.domain.MemoFolder;
import com.baro.memofolder.domain.MemoFolderRepository;
import com.baro.template.application.dto.ArchiveTemplateCommand;
import com.baro.template.application.dto.ArchiveTemplateResult;
import com.baro.template.application.dto.UnArchiveTemplateCommand;
import com.baro.template.domain.Template;
import com.baro.template.domain.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ArchiveService {

    private final ArchiveRepository archiveRepository;
    private final MemberRepository memberRepository;
    private final MemoFolderRepository memoFolderRepository;
    private final TemporalMemoRepository temporalMemoRepository;
    private final TemplateRepository templateRepository;

    public ArchiveTemporalMemoResult archiveTemporalMemo(ArchiveTemporalMemoCommand command) {
        Member member = memberRepository.getById(command.memberId());
        TemporalMemo temporalMemo = temporalMemoRepository.getById(command.temporalMemoId());
        temporalMemo.matchOwner(member.getId());
        MemoFolder memoFolder = memoFolderRepository.getById(command.memoFolderId());
        memoFolder.matchOwner(member.getId());

        Archive archive = archiveRepository.save(new Archive(member, memoFolder, temporalMemo.getContent()));
        temporalMemo.archived(archive);
        return ArchiveTemporalMemoResult.from(archive);
    }

    public ArchiveTemplateResult archiveTemplate(ArchiveTemplateCommand command) {
        Member member = memberRepository.getById(command.memberId());
        Template template = templateRepository.getById(command.templateId());

        if (archiveRepository.existsByMemberIdAndTemplateId(member.getId(), template.getId())) {
            throw new ArchiveException(ArchiveExceptionType.ARCHIVED_TEMPLATE);
        }

        MemoFolder folder = memoFolderRepository.getById(command.memoFolderId());
        folder.matchOwner(member.getId());

        Archive archive = new Archive(member, folder, MemoContent.from(template.getContent()), template);
        archiveRepository.save(archive);
        template.increaseSavedCount();

        return ArchiveTemplateResult.from(archive);
    }

    public void unarchiveTemplate(UnArchiveTemplateCommand command) {
        Member member = memberRepository.getById(command.memberId());
        Template template = templateRepository.getById(command.templateId());

        Archive archive = archiveRepository.getByMemberIdAndTemplateId(member.getId(), template.getId());

        archiveRepository.delete(archive);
        template.decreaseSavedCount();
    }
}
