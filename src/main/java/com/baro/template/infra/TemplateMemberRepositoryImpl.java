package com.baro.template.infra;

import com.baro.template.domain.TemplateMember;
import com.baro.template.domain.TemplateMemberRepository;
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
}
