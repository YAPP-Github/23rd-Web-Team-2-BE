package com.baro.template.application;

import static com.baro.template.fixture.TemplateFixture.감사전하기;
import static com.baro.template.fixture.TemplateFixture.보고하기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.baro.member.domain.MemberRepository;
import com.baro.member.fake.FakeMemberRepository;
import com.baro.member.fixture.MemberFixture;
import com.baro.template.application.dto.CopyTemplateCommand;
import com.baro.template.application.dto.FindTemplateQuery;
import com.baro.template.application.dto.FindTemplateResult;
import com.baro.template.domain.TemplateCategory;
import com.baro.template.domain.TemplateRepository;
import com.baro.template.exception.TemplateException;
import com.baro.template.exception.TemplateExceptionType;
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
    private TemplateRepository templateRepository;
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        templateRepository = new FakeTemplateRepository();
        memberRepository = new FakeMemberRepository();
        service = new TemplateService(templateRepository);
    }

    @Test
    void 최신순_템플릿_조회() {
        // given
        templateRepository.save(보고하기());
        templateRepository.save(보고하기());
        templateRepository.save(보고하기());
        templateRepository.save(감사전하기());

        // when
        var result = service.findTemplates(new FindTemplateQuery(TemplateCategory.REPORT, SortType.NEW.getSort()));

        // then
        assertAll(
                () -> assertThat(result.getContent()).hasSize(3),
                () -> assertThat(result.getContent()).isSortedAccordingTo(
                        Comparator.comparing(FindTemplateResult::templateId).reversed())
        );
    }

    @Test
    void 복사순_템플릿_조회() {
        // given
        templateRepository.save(보고하기(0, 0));
        templateRepository.save(보고하기(0, 1));
        templateRepository.save(보고하기(0, 2));
        templateRepository.save(감사전하기(0, 3));

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
        templateRepository.save(보고하기(0, 0));
        templateRepository.save(보고하기(1, 0));
        templateRepository.save(보고하기(2, 0));
        templateRepository.save(감사전하기(3, 0));

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

    @Test
    void 템플릿_복사() {
        // given
        var member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        var template = templateRepository.save(보고하기());
        var countBeforeCopy = template.getCopiedCount();
        var command = new CopyTemplateCommand(member.getId(), template.getId());

        // when
        service.copyTemplate(command);

        // then
        assertThat(template.getCopiedCount()).isEqualTo(countBeforeCopy + 1);
    }

    @Test
    void 존재하지_않는_템플릿_복사시_예외_발생() {
        // given
        var member = memberRepository.save(MemberFixture.memberWithNickname("바로"));
        var invalidTemplateId = 9999L;
        var command = new CopyTemplateCommand(member.getId(), invalidTemplateId);

        // when & then
        assertThatThrownBy(() -> service.copyTemplate(command))
                .isInstanceOf(TemplateException.class)
                .extracting("exceptionType")
                .isEqualTo(TemplateExceptionType.INVALID_TEMPLATE);
    }
}
