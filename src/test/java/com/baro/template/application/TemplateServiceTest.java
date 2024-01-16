package com.baro.template.application;

import static com.baro.template.fixture.TemplateFixture.감사전하기;
import static com.baro.template.fixture.TemplateFixture.보고하기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.baro.template.application.dto.FindTemplateQuery;
import com.baro.template.application.dto.FindTemplateResult;
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
        repository.save(보고하기());
        repository.save(보고하기());
        repository.save(보고하기());
        repository.save(감사전하기());

        // when
        var result = service.findTemplates(new FindTemplateQuery(TemplateCategory.REPORT, SortType.NEW.getSort()));

        // then
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getContent()).isSortedAccordingTo(
                Comparator.comparing(FindTemplateResult::templateId).reversed());
    }

    @Test
    void 복사순_템플릿_조회() {
        // given
        repository.save(보고하기(0, 0));
        repository.save(보고하기(0, 1));
        repository.save(보고하기(0, 2));
        repository.save(감사전하기(0, 3));

        // when
        var result = service.findTemplates(new FindTemplateQuery(TemplateCategory.REPORT, SortType.COPY.getSort()));

        // then
        assertAll(
                () -> assertThat(result.getNumberOfElements()).isEqualTo(3),
                () -> assertThat(result.getContent()).isSortedAccordingTo(
                        Comparator.comparing(FindTemplateResult::copiedCount).reversed())
        );
    }

    @Test
    void 저장순_템플릿_조회() {
        // given
        repository.save(보고하기(0, 0));
        repository.save(보고하기(1, 0));
        repository.save(보고하기(2, 0));
        repository.save(감사전하기(3, 0));

        // when
        var result = service.findTemplates(new FindTemplateQuery(TemplateCategory.REPORT, SortType.SAVE.getSort()));

        // then
        assertAll(
                () -> assertThat(result.getNumberOfElements()).isEqualTo(3),
                () -> assertThat(result.getContent()).isSortedAccordingTo(
                        Comparator.comparing(FindTemplateResult::savedCount).reversed())
        );
    }

    @Test
    void 빈_템플릿_조회() {
        // when
        var result = service.findTemplates(new FindTemplateQuery(TemplateCategory.REPORT, SortType.NEW.getSort()));

        // then
        assertAll(
                () -> assertThat(result.getContent()).isEmpty(),
                () -> assertThat(result.getNumberOfElements()).isEqualTo(0),
                () -> assertThat(result.isFirst()).isEqualTo(true),
                () -> assertThat(result.isLast()).isEqualTo(true)
        );
    }
}
