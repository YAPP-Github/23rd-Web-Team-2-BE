package com.baro.template.infra;

import com.baro.template.domain.Category;
import com.baro.template.domain.Template;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateJpaRepository extends JpaRepository<Template, Long> {

    List<Template> findAllByCategory(Category category, Pageable pageable);
}