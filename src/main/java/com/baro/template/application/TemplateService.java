package com.baro.template.application;

import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import com.baro.memofolder.domain.MemoFolder;
import com.baro.memofolder.domain.MemoFolderRepository;
import com.baro.template.application.dto.ArchiveTemplateCommand;
import com.baro.template.application.dto.ArchiveTemplateResult;
import com.baro.template.application.dto.CopyTemplateCommand;
import com.baro.template.application.dto.FindTemplateQuery;
import com.baro.template.application.dto.FindTemplateResult;
import com.baro.template.domain.Template;
import com.baro.template.domain.TemplateMember;
import com.baro.template.domain.TemplateMemberRepository;
import com.baro.template.domain.TemplateRepository;
import com.baro.template.exception.TemplateException;
import com.baro.template.exception.TemplateExceptionType;
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
    private final TemplateMemberRepository templateMemberRepository;

    @Transactional(readOnly = true)
    public Slice<FindTemplateResult> findTemplates(FindTemplateQuery query) {
        PageRequest pageRequest = PageRequest.of(0, Integer.MAX_VALUE - 1, query.sort());

        return templateRepository.findAllByCategory(query.templateCategory(), pageRequest)
                .map(FindTemplateResult::from);
    }

    public ArchiveTemplateResult archiveTemplate(ArchiveTemplateCommand command) {
        Member member = memberRepository.getById(command.memberId());
        Template template = templateRepository.getById(command.templateId());

        if (templateMemberRepository.existsByMemberIdAndTemplateId(member.getId(), template.getId())) {
            throw new TemplateException(TemplateExceptionType.ARCHIVED_TEMPLATE);
        }

        MemoFolder folder = memoFolderRepository.getById(command.memoFolderId());
        folder.matchOwner(member.getId());

        TemplateMember templateMember = templateMemberRepository.save(TemplateMember.of(member, folder, template));
        template.increaseSavedCount();

        return ArchiveTemplateResult.from(templateMember);
    }

    public void copyTemplate(CopyTemplateCommand command) {
        Template template = templateRepository.getById(command.templateId());
        template.increaseCopiedCount();
    }
}
