package com.baro.template.application;

import com.baro.template.application.dto.FindTemplateQuery;
import com.baro.template.application.dto.FindTemplateResult;
import com.baro.template.domain.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class TemplateService {

    private final TemplateRepository repository;

    @Transactional(readOnly = true)
    public Slice<FindTemplateResult> findTemplates(FindTemplateQuery query) {
        PageRequest pageRequest = PageRequest.of(0, Integer.MAX_VALUE - 1, query.sort());

        return repository.findAllByCategory(query.templateCategory(), pageRequest).map(FindTemplateResult::from);
    }
}
