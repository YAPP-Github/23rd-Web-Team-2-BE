package com.baro.template.application.dto;

import com.baro.template.domain.Category;
import org.springframework.data.domain.Sort;

public record FindTemplateQuery(
        Category category,
        Sort sort
) {
}
