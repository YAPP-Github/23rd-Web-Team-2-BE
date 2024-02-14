package com.baro.template.application;

import com.baro.archive.domain.Archive;
import com.baro.archive.domain.ArchiveRepository;
import com.baro.archive.exception.ArchiveException;
import com.baro.archive.exception.ArchiveExceptionType;
import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import com.baro.memo.domain.MemoContent;
import com.baro.memofolder.domain.MemoFolder;
import com.baro.memofolder.domain.MemoFolderRepository;
import com.baro.template.application.dto.ArchiveTemplateCommand;
import com.baro.template.application.dto.ArchiveTemplateResult;
import com.baro.template.application.dto.CopyTemplateCommand;
import com.baro.template.application.dto.FindTemplateQuery;
import com.baro.template.application.dto.FindTemplateResult;
import com.baro.template.application.dto.UnArchiveTemplateCommand;
import com.baro.template.domain.Template;
import com.baro.template.domain.TemplateRepository;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final MemberRepository memberRepository;
    private final MemoFolderRepository memoFolderRepository;
    private final ArchiveRepository archiveRepository;

    @Transactional(readOnly = true)
    public Slice<FindTemplateResult> findTemplates(FindTemplateQuery query) {
        PageRequest pageRequest = PageRequest.of(0, Integer.MAX_VALUE - 1, query.sort());

        Set<Long> archivedTemplates = new HashSet<>(archiveRepository.findAllTemplates(query.memberId()).stream()
                .map(archive -> archive.getTemplate().getId()).toList());
        return templateRepository.findAllByCategory(query.templateCategory(), pageRequest)
                .map(template -> FindTemplateResult.from(template, archivedTemplates));
    }

    public void copyTemplate(CopyTemplateCommand command) {
        Template template = templateRepository.getById(command.templateId());
        template.increaseCopiedCount();
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
