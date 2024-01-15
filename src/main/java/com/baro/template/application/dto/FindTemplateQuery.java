package com.baro.template.application.dto;

import com.baro.template.domain.TemplateCategory;
import org.springframework.data.domain.Sort;

public record FindTemplateQuery(
        TemplateCategory templateCategory,
        Sort sort
) {
}
