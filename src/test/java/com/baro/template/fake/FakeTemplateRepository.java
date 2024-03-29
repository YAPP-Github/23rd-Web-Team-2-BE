package com.baro.template.fake;

import static com.baro.template.domain.Template.instanceForTest;

import com.baro.template.domain.Template;
import com.baro.template.domain.TemplateCategory;
import com.baro.template.domain.TemplateRepository;
import com.baro.template.exception.SortException;
import com.baro.template.exception.SortExceptionType;
import com.baro.template.exception.TemplateException;
import com.baro.template.exception.TemplateExceptionType;
import com.baro.template.presentation.SortType;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

public class FakeTemplateRepository implements TemplateRepository {

    private final AtomicLong id = new AtomicLong(1);
    private final Map<Long, Template> templates = new ConcurrentHashMap<>();

    @Override
    public Slice<Template> findAllByCategory(TemplateCategory templateCategory, Pageable pageable) {
        List<Template> categorizedTemplates = templates.values().stream()
                .filter(template -> template.getCategory().equals(templateCategory))
                .toList();
        Sort sortType = pageable.getSort();

        categorizedTemplates = sort(sortType, categorizedTemplates);

        return new SliceImpl<>(categorizedTemplates, pageable, false);
    }

    @Override
    public Template save(Template template) {
        if (Objects.isNull(template.getId())) {
            Long templateId = id.getAndIncrement();
            Template newTemplate = instanceForTest(templateId, template.getCategory(),
                    template.getSubCategory(),
                    template.getContent(), template.getSavedCount(), template.getCopiedCount());
            templates.put(templateId, newTemplate);
            return newTemplate;
        }
        templates.put(template.getId(), template);
        return template;
    }

    @Override
    public Template getById(Long id) {
        if (templates.containsKey(id)) {
            return templates.get(id);
        }
        throw new TemplateException(TemplateExceptionType.INVALID_TEMPLATE);
    }

    private List<Template> sort(Sort sortType, List<Template> categorizedTemplates) {
        if (sortType.equals(SortType.NEW.getSort())) {
            categorizedTemplates = categorizedTemplates.stream()
                    .sorted(Comparator.comparing(Template::getCreatedAt).reversed())
                    .toList();
        } else if (sortType.equals(SortType.COPY.getSort())) {
            categorizedTemplates = categorizedTemplates.stream()
                    .sorted(Comparator.comparing(Template::getCopiedCount).reversed())
                    .toList();
        } else if (sortType.equals(SortType.SAVE.getSort())) {
            categorizedTemplates = categorizedTemplates.stream()
                    .sorted(Comparator.comparing(Template::getSavedCount).reversed())
                    .toList();
        } else {
            throw new SortException(SortExceptionType.INVALID_SORT_TYPE);
        }
        return categorizedTemplates;
    }
}
