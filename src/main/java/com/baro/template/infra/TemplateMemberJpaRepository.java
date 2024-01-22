package com.baro.template.infra;

import com.baro.template.domain.TemplateMember;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateMemberJpaRepository extends JpaRepository<TemplateMember, Long> {

    boolean existsByMemberIdAndTemplateId(Long memberId, Long templateId);

    Optional<TemplateMember> findByMemberIdAndTemplateId(Long memberId, Long templateId);
}
