package com.baro.template.presentation;

import static com.baro.auth.fixture.OAuthMemberInfoFixture.동균;
import static com.baro.auth.fixture.OAuthMemberInfoFixture.유빈;
import static com.baro.common.acceptance.AcceptanceSteps.성공;
import static com.baro.common.acceptance.AcceptanceSteps.응답값_없음;
import static com.baro.common.acceptance.AcceptanceSteps.응답값을_검증한다;
import static com.baro.common.acceptance.AcceptanceSteps.응답의_개수를_검증한다;
import static com.baro.common.acceptance.AcceptanceSteps.잘못된_요청;
import static com.baro.common.acceptance.template.TemplateAcceptanceSteps.템플릿_조회_요청_성공;
import static com.baro.common.acceptance.template.TemplateAcceptanceSteps.템플릿_조회_요청_실패;
import static com.baro.common.acceptance.template.TemplateAcceptanceSteps.템플릿_조회시_응답값이_없는_요청;
import static com.baro.template.fixture.TemplateFixture.감사전하기;
import static com.baro.template.fixture.TemplateFixture.보고하기;

import com.baro.common.RestApiTest;
import com.baro.template.domain.Template;
import com.baro.template.domain.TemplateRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TemplateApiTest extends RestApiTest {

    private final String rootPath = "";
    @Autowired
    private TemplateRepository templateRepository;

    @Test
    void 특정_카테고리의_템플릿을_조회한다() {
        // given
        var 카테고리 = "report";
        var 정렬 = "new";
        var 토큰 = 로그인(유빈());
        템플릿_데이터_준비(List.of(보고하기(), 보고하기(), 보고하기(), 감사전하기()));

        // when
        var 응답 = 템플릿_조회_요청_성공(토큰, 카테고리, 정렬);

        // then
        응답값을_검증한다(응답, 성공);
        응답의_개수를_검증한다(응답, rootPath, 3);
    }

    @Test
    void 존재하지_않는_카테고리_조회시_예외를_반환한다() {
        // given
        var 카테고리 = "nothing";
        var 정렬 = "new";
        var 토큰 = 로그인(동균());

        // when
        var 응답 = 템플릿_조회_요청_실패(토큰, 카테고리, 정렬);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    @Test
    void 존재하지_않는_정렬로_조회시_예외를_반환한다() {
        // given
        var 카테고리 = "report";
        var 정렬 = "view";
        var 토큰 = 로그인(동균());

        // when
        var 응답 = 템플릿_조회_요청_실패(토큰, 카테고리, 정렬);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    @Test
    void 템플릿이_존재하지_않는_경우_반환하는_응답이_없다() {
        // given
        var 카테고리 = "report";
        var 정렬 = "new";
        var 토큰 = 로그인(동균());

        // when
        var 응답 = 템플릿_조회시_응답값이_없는_요청(토큰, 카테고리, 정렬);

        // then
        응답값을_검증한다(응답, 응답값_없음);
    }

    private void 템플릿_데이터_준비(List<Template> templates) {
        templates.forEach(templateRepository::save);
    }
}
