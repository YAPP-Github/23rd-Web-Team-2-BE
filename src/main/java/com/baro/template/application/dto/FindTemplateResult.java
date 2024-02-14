package com.baro.template.application.dto;

import com.baro.template.domain.Template;
import java.util.Set;

public record FindTemplateResult(
        Long templateId,
        String category,
        String subCategory,
        String content,
        int savedCount,
        int copiedCount,
        boolean isArchived
) {

    public static FindTemplateResult from(Template template, Set<Long> archives) {
        return new FindTemplateResult(
                template.getId(),
                template.getCategory().name().toLowerCase(),
                template.getSubCategory(),
                template.getContent(),
                template.getSavedCount(),
                template.getCopiedCount(),
                archives.contains(template.getId())
        );
    }
}
