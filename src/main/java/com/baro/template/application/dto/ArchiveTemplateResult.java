package com.baro.template.application.dto;

import com.baro.template.domain.TemplateMember;

public record ArchiveTemplateResult(
        Long id
) {

    public static ArchiveTemplateResult from(TemplateMember templateMember) {
        return new ArchiveTemplateResult(templateMember.getId());
    }
}
