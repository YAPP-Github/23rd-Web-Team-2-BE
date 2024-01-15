package com.baro.template.application;

import com.baro.template.application.dto.FindTemplateQuery;
import com.baro.template.application.dto.FindTemplateResult;
import com.baro.template.domain.TemplateRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class TemplateService {

    private final TemplateRepository repository;

    @Transactional(readOnly = true)
    public List<FindTemplateResult> findTemplates(FindTemplateQuery query) {
        int pageSize = repository.count();
        if (pageSize < 1) {
            return List.of();
        }

        PageRequest pageRequest = PageRequest.of(0, pageSize, query.sort());
        return repository.findAllByCategory(query.templateCategory(), pageRequest)
                .stream().map(FindTemplateResult::from).toList();
    }
}
