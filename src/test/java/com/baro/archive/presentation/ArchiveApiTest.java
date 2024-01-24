package com.baro.archive.presentation;

import static com.baro.auth.fixture.OAuthMemberInfoFixture.아현;
import static com.baro.auth.fixture.OAuthMemberInfoFixture.유빈;
import static com.baro.auth.fixture.OAuthMemberInfoFixture.태연;
import static com.baro.common.acceptance.AcceptanceSteps.권한_없음;
import static com.baro.common.acceptance.AcceptanceSteps.생성됨;
import static com.baro.common.acceptance.AcceptanceSteps.응답값_없음;
import static com.baro.common.acceptance.AcceptanceSteps.응답값을_검증한다;
import static com.baro.common.acceptance.AcceptanceSteps.잘못된_요청;
import static com.baro.common.acceptance.AcceptanceSteps.존재하지_않음;
import static com.baro.common.acceptance.archive.ArchiveAcceptanceSteps.끄적이는메모_아카이빙_요청;
import static com.baro.common.acceptance.archive.ArchiveAcceptanceSteps.잘못된_끄적이는메모_아카이빙_요청;
import static com.baro.common.acceptance.archive.ArchiveAcceptanceSteps.템플릿_아카이브_요청_성공;
import static com.baro.common.acceptance.archive.ArchiveAcceptanceSteps.템플릿_아카이브_요청_실패;
import static com.baro.common.acceptance.archive.ArchiveAcceptanceSteps.템플릿_아카이브_취소_요청_성공;
import static com.baro.common.acceptance.archive.ArchiveAcceptanceSteps.템플릿_아카이브_취소_요청_실패;
import static com.baro.common.acceptance.memo.TemporalMemoAcceptanceSteps.끄적이는_메모_바디;
import static com.baro.common.acceptance.memo.TemporalMemoAcceptanceSteps.끄적이는메모를_생성하고_ID를_반환한다;
import static com.baro.common.acceptance.memo.TemporalMemoAcceptanceSteps.메모_아카이브_요청_바디;
import static com.baro.common.acceptance.memofolder.MemoFolderAcceptanceSteps.메모_폴더를_생성_하고_ID를_반환한다;
import static com.baro.common.acceptance.memofolder.MemoFolderAcceptanceSteps.폴더_이름_바디;
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
public class ArchiveApiTest extends RestApiTest {

    @Autowired
    private TemplateRepository templateRepository;
    @SpyBean
    private TokenTranslator tokenTranslator;
    @SpyBean
    private MemoFolderRepository memoFolderRepository;

    @Test
    void 끄적이는_메모를_아카이브_한다() {
        // given
        var 유빈 = 로그인(유빈());
        var 유빈의_끄적이는_메모_ID = 끄적이는메모를_생성하고_ID를_반환한다(유빈, 끄적이는_메모_바디);
        var 메모_폴더_ID = 메모_폴더를_생성_하고_ID를_반환한다(유빈, 폴더_이름_바디);
        var 메모_아카이브_요청_바디 = 메모_아카이브_요청_바디(메모_폴더_ID);

        // when
        var 응답 = 끄적이는메모_아카이빙_요청(유빈, 유빈의_끄적이는_메모_ID, 메모_아카이브_요청_바디);

        // then
        응답값을_검증한다(응답, 생성됨);
    }

    @Test
    void 다른사람의_끄적이는_메모_아카이브_시_예외_발생() {
        // given
        var 유빈 = 로그인(유빈());
        var 유빈의_끄적이는_메모_ID = 끄적이는메모를_생성하고_ID를_반환한다(유빈, 끄적이는_메모_바디);
        var 메모_폴더_ID = 메모_폴더를_생성_하고_ID를_반환한다(유빈, 폴더_이름_바디);
        var 메모_아카이브_요청_바디 = 메모_아카이브_요청_바디(메모_폴더_ID);
        var 태연 = 로그인(태연());

        // when
        var 응답 = 잘못된_끄적이는메모_아카이빙_요청(태연, 유빈의_끄적이는_메모_ID, 메모_아카이브_요청_바디);

        // then
        응답값을_검증한다(응답, 권한_없음);
    }

    @Test
    void 다른사람의_메모_폴더에_아카이브_시_예외_발생() {
        // given
        var 유빈 = 로그인(유빈());
        var 유빈의_끄적이는_메모_ID = 끄적이는메모를_생성하고_ID를_반환한다(유빈, 끄적이는_메모_바디);

        var 태연 = 로그인(태연());
        var 메모_폴더_ID = 메모_폴더를_생성_하고_ID를_반환한다(태연, 폴더_이름_바디);
        var 메모_아카이브_요청_바디 = 메모_아카이브_요청_바디(메모_폴더_ID);

        // when
        var 응답 = 잘못된_끄적이는메모_아카이빙_요청(유빈, 유빈의_끄적이는_메모_ID, 메모_아카이브_요청_바디);

        // then
        응답값을_검증한다(응답, 권한_없음);
    }

    @Test
    void 존재_하지_않는_끄적이는메모_아카이브_시_예외_발생() {
        // given
        var 유빈 = 로그인(유빈());
        var 존재_하지_않는_끄적이는메모 = 999L;

        var 메모_폴더_ID = 메모_폴더를_생성_하고_ID를_반환한다(유빈, 폴더_이름_바디);
        var 메모_아카이브_요청_바디 = 메모_아카이브_요청_바디(메모_폴더_ID);

        // when
        var 응답 = 잘못된_끄적이는메모_아카이빙_요청(유빈, 존재_하지_않는_끄적이는메모, 메모_아카이브_요청_바디);

        // then
        응답값을_검증한다(응답, 존재하지_않음);
    }

    @Test
    void 존재_하지_않는_메모_폴더에_아카이브_시_예외_발생() {
        // given
        var 유빈 = 로그인(유빈());
        var 유빈의_끄적이는_메모_ID = 끄적이는메모를_생성하고_ID를_반환한다(유빈, 끄적이는_메모_바디);

        var 존재_하지_않는_메모_폴더_ID = 999L;
        var 메모_아카이브_요청_바디 = 메모_아카이브_요청_바디(존재_하지_않는_메모_폴더_ID);

        // when
        var 응답 = 잘못된_끄적이는메모_아카이빙_요청(유빈, 유빈의_끄적이는_메모_ID, 메모_아카이브_요청_바디);

        // then
        응답값을_검증한다(응답, 존재하지_않음);
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
    void 템플릿_아카이브를_취소한다() {
        // given
        var 토큰 = 로그인(아현());
        var 폴더 = 메모_폴더를_생성_하고_ID를_반환한다(토큰, new SaveMemoFolderRequest("폴더"));
        var 바디 = new ArchiveTemplateRequest(폴더);
        var 템플릿 = 보고하기();
        템플릿_데이터_준비(List.of(템플릿));
        템플릿_아카이브_요청_성공(토큰, 템플릿.getId(), 바디);

        // when
        var 응답 = 템플릿_아카이브_취소_요청_성공(토큰, 템플릿.getId());

        // then
        응답값을_검증한다(응답, 응답값_없음);
    }

    @Test
    void 존재하지_않는_멤버가_템플릿_아카이브를_취소할시_예외를_반환한다() {
        // given
        var 토큰 = 로그인(아현());
        var 폴더 = 메모_폴더를_생성_하고_ID를_반환한다(토큰, new SaveMemoFolderRequest("폴더"));
        var 바디 = new ArchiveTemplateRequest(폴더);
        var 템플릿 = 보고하기();
        템플릿_데이터_준비(List.of(템플릿));
        템플릿_아카이브_요청_성공(토큰, 템플릿.getId(), 바디);
        멤버는_존재하지_않는다(토큰);

        // when
        var 응답 = 템플릿_아카이브_취소_요청_실패(토큰, 템플릿.getId());

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    @Test
    void 존재하지_않는_템플릿을_아카이브_취소할시_예외를_반환한다() {
        // given
        var 토큰 = 로그인(아현());

        // when
        var 응답 = 템플릿_아카이브_취소_요청_실패(토큰, 999L);

        // then
        응답값을_검증한다(응답, 존재하지_않음);
    }

    @Test
    void 아카이브하지_않은_템플릿을_아카이브_취소할시_예외를_반환한다() {
        // given
        var 토큰 = 로그인(아현());
        var 템플릿 = 보고하기();
        템플릿_데이터_준비(List.of(템플릿));

        // when
        var 응답 = 템플릿_아카이브_취소_요청_실패(토큰, 템플릿.getId());

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
