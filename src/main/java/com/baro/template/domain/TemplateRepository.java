package com.baro.template.domain;

import java.util.List;
import org.springframework.data.domain.Pageable;

public interface TemplateRepository {

    List<Template> findAllByCategory(Category category, Pageable pageable);

    Template save(Template template);

    int count();
}
