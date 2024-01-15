package com.baro.template.application;

import static com.baro.template.fixture.TemplateFixture.감사전하기;
import static com.baro.template.fixture.TemplateFixture.보고하기;
import static org.assertj.core.api.Assertions.assertThat;

import com.baro.template.application.dto.FindTemplateQuery;
import com.baro.template.application.dto.FindTemplateResult;
import com.baro.template.domain.Template;
import com.baro.template.domain.TemplateCategory;
import com.baro.template.domain.TemplateRepository;
import com.baro.template.fake.FakeTemplateRepository;
import com.baro.template.presentation.SortType;
import java.util.Comparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TemplateServiceTest {

    private TemplateService service;
    private TemplateRepository repository;

    @BeforeEach
    void setUp() {
        repository = new FakeTemplateRepository();
        service = new TemplateService(repository);
    }

    @Test
    void 최신순_템플릿_조회() {
        // given
        Template template1 = repository.save(보고하기());
        Template template2 = repository.save(보고하기());
        Template template3 = repository.save(보고하기());
        Template template4 = repository.save(감사전하기());

        // when
        var result = service.findTemplates(new FindTemplateQuery(TemplateCategory.REPORT, SortType.NEW.getSort()));

        // then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).templateId()).isEqualTo(template3.getId());
    }

    @Test
    void 복사순_템플릿_조회() {
        // given
        Template template1 = repository.save(보고하기(0, 0));
        Template template2 = repository.save(보고하기(0, 1));
        Template template3 = repository.save(보고하기(0, 2));
        Template template4 = repository.save(감사전하기(0, 3));

        // when
        var result = service.findTemplates(new FindTemplateQuery(TemplateCategory.REPORT, SortType.COPY.getSort()));

        // then
        assertThat(result).hasSize(3);
        assertThat(result).isSortedAccordingTo(Comparator.comparing(FindTemplateResult::copiedCount).reversed());
    }

    @Test
    void 저장순_템플릿_조회() {
        // given
        Template template1 = repository.save(보고하기(0, 0));
        Template template2 = repository.save(보고하기(1, 0));
        Template template3 = repository.save(보고하기(2, 0));
        Template template4 = repository.save(감사전하기(3, 0));

        // when
        var result = service.findTemplates(new FindTemplateQuery(TemplateCategory.REPORT, SortType.SAVE.getSort()));

        // then
        assertThat(result.get(0).templateId()).isEqualTo(template3.getId());
        assertThat(result).isSortedAccordingTo(Comparator.comparing(FindTemplateResult::savedCount).reversed());
    }

    @Test
    void 빈_템플릿_조회() {
        // when
        var result = service.findTemplates(new FindTemplateQuery(TemplateCategory.REPORT, SortType.NEW.getSort()));

        // then
        assertThat(result).isEmpty();
    }
}
