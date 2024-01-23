package com.baro.template.fake;

import com.baro.template.domain.TemplateMember;
import com.baro.template.domain.TemplateMemberRepository;
import com.baro.template.exception.TemplateException;
import com.baro.template.exception.TemplateExceptionType;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class FakeTemplateMemberRepository implements TemplateMemberRepository {

    private final AtomicLong id = new AtomicLong(1);
    private final Map<Long, TemplateMember> templateMembers = new ConcurrentHashMap<>();

    @Override
    public boolean existsByMemberIdAndTemplateId(Long memberId, Long templateId) {
        return templateMembers.values().stream()
                .anyMatch(templateMember -> templateMember.getMember().getId().equals(memberId)
                        && templateMember.getTemplate().getId().equals(templateId));
    }

    @Override
    public TemplateMember save(TemplateMember templateMember) {
        if (Objects.isNull(templateMember.getId())) {
            Long templateMemberId = id.getAndIncrement();
            TemplateMember newTemplateMember = TemplateMember.instanceForTest(
                    templateMemberId,
                    templateMember.getMember(),
                    templateMember.getMemoFolder(),
                    templateMember.getTemplate()
            );
            templateMembers.put(templateMemberId, newTemplateMember);
            return newTemplateMember;
        }
        templateMembers.put(templateMember.getId(), templateMember);
        return templateMember;
    }

    @Override
    public List<TemplateMember> findAll() {
        return List.copyOf(templateMembers.values());
    }

    @Override
    public TemplateMember getByMemberIdAndTemplateId(Long memberId, Long templateId) {
        return templateMembers.values().stream()
                .filter(templateMember -> templateMember.getMember().getId().equals(memberId)
                        && templateMember.getTemplate().getId().equals(templateId))
                .findFirst()
                .orElseThrow(() -> new TemplateException(TemplateExceptionType.NOT_ARCHIVED_TEMPLATE));
    }

    @Override
    public void delete(TemplateMember templateMember) {
        templateMembers.remove(templateMember.getId());
    }
}
