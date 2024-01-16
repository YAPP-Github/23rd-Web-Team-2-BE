package com.baro.template.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface TemplateRepository {

    Slice<Template> findAllByCategory(TemplateCategory templateCategory, Pageable pageable);

    Template save(Template template);

    int count();
}
