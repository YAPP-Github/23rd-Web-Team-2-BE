package com.baro.template.infra;

import com.baro.template.domain.Template;
import com.baro.template.domain.TemplateCategory;
import com.baro.template.domain.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TemplateRepositoryImpl implements TemplateRepository {

    private final TemplateJpaRepository templateJpaRepository;

    @Override
    public Slice<Template> findAllByCategory(TemplateCategory templateCategory, Pageable pageable) {
        return templateJpaRepository.findAllByCategory(templateCategory, pageable);
    }

    @Override
    public Template save(Template template) {
        return templateJpaRepository.save(template);
    }
}
