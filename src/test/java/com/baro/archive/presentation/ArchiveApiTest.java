package com.baro.archive.presentation;

import static com.baro.auth.fixture.OAuthMemberInfoFixture.아현;
import static com.baro.auth.fixture.OAuthMemberInfoFixture.태연;
import static com.baro.common.acceptance.AcceptanceSteps.권한_없음;
import static com.baro.common.acceptance.AcceptanceSteps.성공;
import static com.baro.common.acceptance.AcceptanceSteps.응답값_없음;
import static com.baro.common.acceptance.AcceptanceSteps.응답값을_검증한다;
import static com.baro.common.acceptance.AcceptanceSteps.응답의_개수를_검증한다;
import static com.baro.common.acceptance.AcceptanceSteps.잘못된_요청;
import static com.baro.common.acceptance.AcceptanceSteps.존재하지_않음;
import static com.baro.common.acceptance.archive.ArchiveAcceptanceSteps.아카이브_수정_요청_성공;
import static com.baro.common.acceptance.archive.ArchiveAcceptanceSteps.아카이브_수정_요청_실패;
import static com.baro.common.acceptance.archive.ArchiveAcceptanceSteps.아카이브_탭_조회_요청_성공;
import static com.baro.common.acceptance.archive.ArchiveAcceptanceSteps.아카이브_탭_조회_요청_실패;
import static com.baro.common.acceptance.memo.TemporalMemoAcceptanceSteps.끄적이는_메모_바디;
import static com.baro.common.acceptance.memo.TemporalMemoAcceptanceSteps.끄적이는메모_아카이빙_요청;
import static com.baro.common.acceptance.memo.TemporalMemoAcceptanceSteps.끄적이는메모_아카이빙_요청후_생성된_ID를_반환한다;
import static com.baro.common.acceptance.memo.TemporalMemoAcceptanceSteps.끄적이는메모를_생성하고_ID를_반환한다;
import static com.baro.common.acceptance.memo.TemporalMemoAcceptanceSteps.메모_아카이브_요청_바디;
import static com.baro.common.acceptance.memofolder.MemoFolderAcceptanceSteps.메모_폴더를_생성_하고_ID를_반환한다;
import static com.baro.common.acceptance.template.TemplateAcceptanceSteps.참고하는메모_아카이빙_요청후_생성된_ID를_반환한다;
import static com.baro.common.acceptance.template.TemplateAcceptanceSteps.템플릿_아카이브_요청_성공;
import static com.baro.template.fixture.TemplateFixture.감사전하기;

import com.baro.archive.presentation.dto.ModifyArchiveRequest;
import com.baro.auth.domain.Token;
import com.baro.common.RestApiTest;
import com.baro.memo.presentation.dto.ArchiveTemporalMemoRequest;
import com.baro.memofolder.presentation.dto.SaveMemoFolderRequest;
import com.baro.template.domain.Template;
import com.baro.template.domain.TemplateRepository;
import com.baro.template.presentation.dto.ArchiveTemplateRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class ArchiveApiTest extends RestApiTest {

    @Autowired
    private TemplateRepository templateRepository;

    @Test
    void 전체_아카이브를_조회한다() {
        // given
        var 토큰 = 로그인(아현());
        var 폴더 = 메모_폴더를_생성_하고_ID를_반환한다(토큰, new SaveMemoFolderRequest("폴더"));
        끄적이는을_아카이빙한다(토큰, 폴더);
        참고하는을_아카이빙한다(토큰, 폴더);
        var 탭이름 = "all";

        // when
        var 응답 = 아카이브_탭_조회_요청_성공(토큰, 폴더, 탭이름);

        // then
        응답값을_검증한다(응답, 성공);
        응답의_개수를_검증한다(응답, "", 2);
    }

    @Test
    void 끄적이는탭_아카이브를_조회한다() {
        // given
        var 토큰 = 로그인(아현());
        var 폴더 = 메모_폴더를_생성_하고_ID를_반환한다(토큰, new SaveMemoFolderRequest("폴더"));
        끄적이는을_아카이빙한다(토큰, 폴더);
        참고하는을_아카이빙한다(토큰, 폴더);
        var 탭이름 = "memo";

        // when
        var 응답 = 아카이브_탭_조회_요청_성공(토큰, 폴더, 탭이름);

        // then
        응답값을_검증한다(응답, 성공);
        응답의_개수를_검증한다(응답, "", 1);
    }

    @Test
    void 참고하는탭_아카이브를_조회한다() {
        // given
        var 토큰 = 로그인(아현());
        var 폴더 = 메모_폴더를_생성_하고_ID를_반환한다(토큰, new SaveMemoFolderRequest("폴더"));
        끄적이는을_아카이빙한다(토큰, 폴더);
        참고하는을_아카이빙한다(토큰, 폴더);
        var 탭이름 = "template";

        // when
        var 응답 = 아카이브_탭_조회_요청_성공(토큰, 폴더, 탭이름);

        // then
        응답값을_검증한다(응답, 성공);
        응답의_개수를_검증한다(응답, "", 1);
    }

    @Test
    void 존재하지않는_탭에대한_조회_요청의_경우_예외를_반환한다() {
        // given
        var 토큰 = 로그인(아현());
        var 폴더 = 메모_폴더를_생성_하고_ID를_반환한다(토큰, new SaveMemoFolderRequest("폴더"));
        끄적이는을_아카이빙한다(토큰, 폴더);
        참고하는을_아카이빙한다(토큰, 폴더);
        var 탭이름 = "non-exist-tab";

        // when
        var 응답 = 아카이브_탭_조회_요청_실패(토큰, 폴더, 탭이름);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    @Test
    void 존재하지않는_폴더에대한_조회_요청의_경우_예외를_반환한다() {
        // given
        var 토큰 = 로그인(아현());
        var 존재하지_않는_폴더ID = 999L;
        var 탭이름 = "all";

        // when
        var 응답 = 아카이브_탭_조회_요청_실패(토큰, 존재하지_않는_폴더ID, 탭이름);

        // then
        응답값을_검증한다(응답, 존재하지_않음);
    }

    @Test
    void 폴더의_소유자가_아닌_경우_예외를_반환한다() {
        // given
        var 아현_토큰 = 로그인(아현());
        var 태연_토큰 = 로그인(태연());
        var 아현_폴더 = 메모_폴더를_생성_하고_ID를_반환한다(아현_토큰, new SaveMemoFolderRequest("폴더"));
        var 탭이름 = "all";

        // when
        var 응답 = 아카이브_탭_조회_요청_실패(태연_토큰, 아현_폴더, 탭이름);

        // then
        응답값을_검증한다(응답, 권한_없음);
    }

    @Test
    void 아카이브를_수정한다() {
        // given
        var 토큰 = 로그인(아현());
        var 폴더 = 메모_폴더를_생성_하고_ID를_반환한다(토큰, new SaveMemoFolderRequest("폴더"));
        var 끄적이는메모_ID = 끄적이는메모를_생성하고_ID를_반환한다(토큰, 끄적이는_메모_바디);
        var 메모_아카이브_ID = 끄적이는메모_아카이빙_요청후_생성된_ID를_반환한다(토큰, 끄적이는메모_ID, new ArchiveTemporalMemoRequest(폴더));
        var 수정할내용 = new ModifyArchiveRequest("수정할 내용");

        // when
        var 응답 = 아카이브_수정_요청_성공(토큰, 메모_아카이브_ID, 수정할내용);

        // then
        응답값을_검증한다(응답, 응답값_없음);
    }

    @Test
    void 아카이브_수정시_소유자가_아닌경우_예외를_반환한다() {
        // given
        var 아현_토큰 = 로그인(아현());
        var 태연_토큰 = 로그인(태연());
        var 폴더 = 메모_폴더를_생성_하고_ID를_반환한다(아현_토큰, new SaveMemoFolderRequest("폴더"));
        var 끄적이는메모_ID = 끄적이는메모를_생성하고_ID를_반환한다(아현_토큰, 끄적이는_메모_바디);
        var 메모_아카이브_ID = 끄적이는메모_아카이빙_요청후_생성된_ID를_반환한다(아현_토큰, 끄적이는메모_ID, new ArchiveTemporalMemoRequest(폴더));
        var 수정할내용 = new ModifyArchiveRequest("수정할 내용");

        // when
        var 응답 = 아카이브_수정_요청_실패(태연_토큰, 메모_아카이브_ID, 수정할내용);

        // then
        응답값을_검증한다(응답, 권한_없음);
    }

    @Test
    void 아카이브_수정시_존재하지_않는_아카이브를_수정하려는_경우_예외를_반환한다() {
        // given
        var 토큰 = 로그인(아현());
        var 폴더 = 메모_폴더를_생성_하고_ID를_반환한다(토큰, new SaveMemoFolderRequest("폴더"));
        var 끄적이는메모_ID = 끄적이는메모를_생성하고_ID를_반환한다(토큰, 끄적이는_메모_바디);
        var 메모_아카이브_ID = 끄적이는메모_아카이빙_요청후_생성된_ID를_반환한다(토큰, 끄적이는메모_ID, new ArchiveTemporalMemoRequest(폴더));
        var 수정할내용 = new ModifyArchiveRequest("수정할 내용");
        var 존재하지않는_아카이브ID = 999L;

        // when
        var 응답 = 아카이브_수정_요청_실패(토큰, 존재하지않는_아카이브ID, 수정할내용);

        // then
        응답값을_검증한다(응답, 존재하지_않음);
    }

    @Test
    void 아카이브_수정시_500자를_초과할경우_예외를_반환한다() {
        // given
        var 토큰 = 로그인(아현());
        var 폴더 = 메모_폴더를_생성_하고_ID를_반환한다(토큰, new SaveMemoFolderRequest("폴더"));
        var 끄적이는메모_ID = 끄적이는메모를_생성하고_ID를_반환한다(토큰, 끄적이는_메모_바디);
        var 메모_아카이브_ID = 끄적이는메모_아카이빙_요청후_생성된_ID를_반환한다(토큰, 끄적이는메모_ID, new ArchiveTemporalMemoRequest(폴더));
        var 글자수를_초과하는_내용 = "가".repeat(501);
        var 수정할내용 = new ModifyArchiveRequest(글자수를_초과하는_내용);

        // when
        var 응답 = 아카이브_수정_요청_성공(토큰, 메모_아카이브_ID, 수정할내용);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    @Test
    void 템플릿_아카이브_수정시_예외를_반환한다() {
        // given
        var 토큰 = 로그인(아현());
        var 폴더 = 메모_폴더를_생성_하고_ID를_반환한다(토큰, new SaveMemoFolderRequest("폴더"));
        var 참고하는_ID = 참고하는_데이터_준비(감사전하기());
        var 메모_아카이브_ID = 참고하는메모_아카이빙_요청후_생성된_ID를_반환한다(토큰, 참고하는_ID, new ArchiveTemplateRequest(폴더));
        var 수정할내용 = new ModifyArchiveRequest("수정할 내용");

        // when
        var 응답 = 아카이브_수정_요청_실패(토큰, 메모_아카이브_ID, 수정할내용);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    private void 끄적이는을_아카이빙한다(Token 토큰, Long 폴더_ID) {
        var 끄적이는_메모_ID = 끄적이는메모를_생성하고_ID를_반환한다(토큰, 끄적이는_메모_바디);
        var 메모_아카이브_요청_바디 = 메모_아카이브_요청_바디(폴더_ID);
        끄적이는메모_아카이빙_요청(토큰, 끄적이는_메모_ID, 메모_아카이브_요청_바디);
    }

    private void 참고하는을_아카이빙한다(Token 토큰, Long 폴더_ID) {
        var template = 감사전하기();
        templateRepository.save(template);
        템플릿_아카이브_요청_성공(토큰, template.getId(), new ArchiveTemplateRequest(폴더_ID));
    }

    private Long 참고하는_데이터_준비(Template template) {
        return templateRepository.save(template).getId();
    }
}
