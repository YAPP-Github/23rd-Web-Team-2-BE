package com.baro.template.infra;

import com.baro.template.domain.TemplateMember;
import com.baro.template.domain.TemplateMemberRepository;
import com.baro.template.exception.TemplateException;
import com.baro.template.exception.TemplateExceptionType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TemplateMemberRepositoryImpl implements TemplateMemberRepository {

    private final TemplateMemberJpaRepository templateMemberJpaRepository;

    @Override
    public boolean existsByMemberIdAndTemplateId(Long memberId, Long templateId) {
        return templateMemberJpaRepository.existsByMemberIdAndTemplateId(memberId, templateId);
    }

    @Override
    public TemplateMember save(TemplateMember templateMember) {
        return templateMemberJpaRepository.save(templateMember);
    }

    @Override
    public List<TemplateMember> findAll() {
        return templateMemberJpaRepository.findAll();
    }

    @Override
    public TemplateMember getByMemberIdAndTemplateId(Long memberId, Long templateId) {
        return templateMemberJpaRepository.findByMemberIdAndTemplateId(memberId, templateId)
                .orElseThrow(() -> new TemplateException(TemplateExceptionType.NOT_ARCHIVED_TEMPLATE));
    }

    @Override
    public void delete(TemplateMember templateMember) {
        templateMemberJpaRepository.delete(templateMember);
    }
}
