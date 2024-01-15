package com.baro.template.application.dto;

import com.baro.template.domain.Template;

public record FindTemplateResult(
        Long templateId,
        String category,
        String subCategory,
        String content,
        int savedCount,
        int copiedCount
) {

    public static FindTemplateResult from(Template template) {
        return new FindTemplateResult(
                template.getId(),
                template.getTemplateCategory().name().toLowerCase(),
                template.getSubCategory(),
                template.getContent(),
                template.getSavedCount(),
                template.getCopiedCount()
        );
    }
}
