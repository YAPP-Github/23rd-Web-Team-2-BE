package com.baro.template.infra;

import com.baro.template.domain.Template;
import com.baro.template.domain.TemplateCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateJpaRepository extends JpaRepository<Template, Long> {

    Slice<Template> findAllByCategory(TemplateCategory templateCategory, Pageable pageable);
}
