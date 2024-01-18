package com.baro.template.presentation;

import static com.baro.auth.fixture.OAuthMemberInfoFixture.동균;
import static com.baro.auth.fixture.OAuthMemberInfoFixture.유빈;
import static com.baro.auth.fixture.OAuthMemberInfoFixture.태연;
import static com.baro.common.acceptance.AcceptanceSteps.권한_없음;
import static com.baro.common.acceptance.AcceptanceSteps.생성됨;
import static com.baro.common.acceptance.AcceptanceSteps.성공;
import static com.baro.common.acceptance.AcceptanceSteps.응답값_없음;
import static com.baro.common.acceptance.AcceptanceSteps.응답값을_검증한다;
import static com.baro.common.acceptance.AcceptanceSteps.응답의_개수를_검증한다;
import static com.baro.common.acceptance.AcceptanceSteps.응답의_특정_필드값을_검증한다;
import static com.baro.common.acceptance.AcceptanceSteps.잘못된_요청;
import static com.baro.common.acceptance.AcceptanceSteps.존재하지_않음;
import static com.baro.common.acceptance.memofolder.MemoFolderAcceptanceSteps.메모_폴더를_생성_하고_ID를_반환한다;
import static com.baro.common.acceptance.template.TemplateAcceptanceSteps.템플릿_복사_요청_성공;
import static com.baro.common.acceptance.template.TemplateAcceptanceSteps.템플릿_복사_요청_실패;
import static com.baro.common.acceptance.template.TemplateAcceptanceSteps.템플릿_아카이브_요청_성공;
import static com.baro.common.acceptance.template.TemplateAcceptanceSteps.템플릿_아카이브_요청_실패;
import static com.baro.common.acceptance.template.TemplateAcceptanceSteps.템플릿_조회_요청_성공;
import static com.baro.common.acceptance.template.TemplateAcceptanceSteps.템플릿_조회_요청_실패;
import static com.baro.common.acceptance.template.TemplateAcceptanceSteps.템플릿_조회시_응답값이_없는_요청;
import static com.baro.template.fixture.TemplateFixture.감사전하기;
import static com.baro.template.fixture.TemplateFixture.보고하기;
import static org.mockito.BDDMockito.given;

import com.baro.auth.application.TokenTranslator;
import com.baro.auth.domain.Token;
import com.baro.common.RestApiTest;
import com.baro.memofolder.domain.MemoFolderRepository;
import com.baro.memofolder.exception.MemoFolderException;
import com.baro.memofolder.exception.MemoFolderExceptionType;
import com.baro.memofolder.presentation.dto.SaveMemoFolderRequest;
import com.baro.template.domain.Template;
import com.baro.template.domain.TemplateRepository;
import com.baro.template.presentation.dto.ArchiveTemplateRequest;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TemplateApiTest extends RestApiTest {

    @Autowired
    private TemplateRepository templateRepository;
    @SpyBean
    private TokenTranslator tokenTranslator;
    @SpyBean
    private MemoFolderRepository memoFolderRepository;

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
        응답의_개수를_검증한다(응답, "content", 3);
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
        응답값을_검증한다(응답, 성공);
        응답의_개수를_검증한다(응답, "content", 0);
        응답의_특정_필드값을_검증한다(응답, "empty", "true");
    }

    @Test
    void 템플릿을_아카이브한다() {
        // given
        var 토큰 = 로그인(유빈());
        var 폴더 = 메모_폴더를_생성_하고_ID를_반환한다(토큰, new SaveMemoFolderRequest("폴더"));
        var 바디 = new ArchiveTemplateRequest(폴더);
        var 템플릿 = 보고하기();
        템플릿_데이터_준비(List.of(템플릿));

        // when
        var 응답 = 템플릿_아카이브_요청_성공(토큰, 템플릿.getId(), 바디);

        // then
        응답값을_검증한다(응답, 생성됨);
    }

    @Test
    void 존재하지_않는_템플릿을_아카이브할시_예외를_반환한다() {
        // given
        var 토큰 = 로그인(유빈());
        var 폴더 = 메모_폴더를_생성_하고_ID를_반환한다(토큰, new SaveMemoFolderRequest("폴더"));
        var 바디 = new ArchiveTemplateRequest(폴더);
        var 존재하지않는_템플릿 = 9999L;

        // when
        var 응답 = 템플릿_아카이브_요청_실패(토큰, 존재하지않는_템플릿, 바디);

        // then
        응답값을_검증한다(응답, 존재하지_않음);
    }

    @Test
    void 폴더_주인이_일치하지_않는_경우_예외를_반환한다() {
        // given
        var 유빈_토큰 = 로그인(유빈());
        var 태연_토큰 = 로그인(태연());
        var 폴더 = 메모_폴더를_생성_하고_ID를_반환한다(태연_토큰, new SaveMemoFolderRequest("폴더"));
        var 바디 = new ArchiveTemplateRequest(폴더);
        var 템플릿 = 보고하기();
        템플릿_데이터_준비(List.of(템플릿));

        // when
        var 응답 = 템플릿_아카이브_요청_실패(유빈_토큰, 템플릿.getId(), 바디);

        // then
        응답값을_검증한다(응답, 권한_없음);
    }

    @Test
    void 존재하지_않는_멤버가_템플릿을_아카이브할시_예외를_반환한다() {
        // given
        var 토큰 = 로그인(유빈());
        var 폴더 = 메모_폴더를_생성_하고_ID를_반환한다(토큰, new SaveMemoFolderRequest("폴더"));
        var 바디 = new ArchiveTemplateRequest(폴더);
        var 템플릿 = 보고하기();
        템플릿_데이터_준비(List.of(템플릿));
        멤버는_존재하지_않는다(토큰);

        // when
        var 응답 = 템플릿_아카이브_요청_실패(토큰, 템플릿.getId(), 바디);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    @Test
    void 이미_아카이브한_템플릿일_경우_예외를_반환한다() {
        // given
        var 토큰 = 로그인(유빈());
        var 폴더 = 메모_폴더를_생성_하고_ID를_반환한다(토큰, new SaveMemoFolderRequest("폴더"));
        var 바디 = new ArchiveTemplateRequest(폴더);
        var 템플릿 = 보고하기();
        템플릿_데이터_준비(List.of(템플릿));
        템플릿_아카이브_요청_성공(토큰, 템플릿.getId(), 바디);

        // when
        var 응답 = 템플릿_아카이브_요청_실패(토큰, 템플릿.getId(), 바디);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    @Test
    void 존재하지_않는_폴더에_아카이브할시_예외를_반환한다() {
        // given
        var 토큰 = 로그인(유빈());
        var 폴더 = 메모_폴더를_생성_하고_ID를_반환한다(토큰, new SaveMemoFolderRequest("폴더"));
        var 바디 = new ArchiveTemplateRequest(폴더);
        var 템플릿 = 보고하기();
        템플릿_데이터_준비(List.of(템플릿));
        폴더는_존재하지_않는다(폴더);

        // when
        var 응답 = 템플릿_아카이브_요청_실패(토큰, 템플릿.getId(), 바디);

        // then
        응답값을_검증한다(응답, 존재하지_않음);
    }

    @Test
    void 템플릿을_복사한다() {
        // given
        var 토큰 = 로그인(유빈());
        var 템플릿 = 보고하기();
        템플릿_데이터_준비(List.of(템플릿));

        // when
        var 응답 = 템플릿_복사_요청_성공(토큰, 템플릿.getId());

        // then
        응답값을_검증한다(응답, 응답값_없음);
    }

    @Test
    void 존재하지_않는_템플릿_복사시_예외를_반환한다() {
        // given
        var 토큰 = 로그인(유빈());
        var 존재하지_않는_템플릿 = 999L;

        // when
        var 응답 = 템플릿_복사_요청_실패(토큰, 존재하지_않는_템플릿);

        // then
        응답값을_검증한다(응답, 존재하지_않음);
    }

    @Test
    void 존재하지_않는_멤버가_템플릿을_복사할시_예외를_반환한다() {
        // given
        var 토큰 = 로그인(유빈());
        멤버는_존재하지_않는다(토큰);
        var 템플릿 = 보고하기();
        템플릿_데이터_준비(List.of(템플릿));

        // when
        var 응답 = 템플릿_복사_요청_실패(토큰, 템플릿.getId());

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    private void 템플릿_데이터_준비(List<Template> templates) {
        templates.forEach(templateRepository::save);
    }

    private void 멤버는_존재하지_않는다(Token 토큰) {
        given(tokenTranslator.decodeAccessToken("Bearer " + 토큰.accessToken())).willReturn(999L);
    }

    private void 폴더는_존재하지_않는다(Long folderId) {
        given(memoFolderRepository.getById(folderId)).willThrow(
                new MemoFolderException(MemoFolderExceptionType.NOT_EXIST_MEMO_FOLDER));
    }
}
