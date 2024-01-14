package com.baro.template.infra;

import com.baro.template.domain.Category;
import com.baro.template.domain.Template;
import com.baro.template.domain.TemplateRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TemplateRepositoryImpl implements TemplateRepository {

    private final TemplateJpaRepository templateJpaRepository;

    @Override
    public List<Template> findAllByCategory(Category category, Pageable pageable) {
        return templateJpaRepository.findAllByCategory(category, pageable);
    }

    @Override
    public int count() {
        return (int) templateJpaRepository.count();
    }
}
