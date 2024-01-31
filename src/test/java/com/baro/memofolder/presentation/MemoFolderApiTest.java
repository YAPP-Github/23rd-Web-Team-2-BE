package com.baro.memofolder.presentation;

import static com.baro.auth.fixture.OAuthMemberInfoFixture.동균;
import static com.baro.auth.fixture.OAuthMemberInfoFixture.원진;
import static com.baro.auth.fixture.OAuthMemberInfoFixture.유빈;
import static com.baro.auth.fixture.OAuthMemberInfoFixture.은지;
import static com.baro.auth.fixture.OAuthMemberInfoFixture.준희;
import static com.baro.auth.fixture.OAuthMemberInfoFixture.태연;
import static com.baro.common.acceptance.AcceptanceSteps.권한_없음;
import static com.baro.common.acceptance.AcceptanceSteps.생성됨;
import static com.baro.common.acceptance.AcceptanceSteps.성공;
import static com.baro.common.acceptance.AcceptanceSteps.응답값_없음;
import static com.baro.common.acceptance.AcceptanceSteps.응답값을_검증한다;
import static com.baro.common.acceptance.AcceptanceSteps.응답의_Location_헤더가_존재한다;
import static com.baro.common.acceptance.AcceptanceSteps.잘못된_요청;
import static com.baro.common.acceptance.AcceptanceSteps.존재하지_않음;
import static com.baro.common.acceptance.memofolder.MemoFolderAcceptanceSteps.메모_폴더_불러오기_요청;
import static com.baro.common.acceptance.memofolder.MemoFolderAcceptanceSteps.메모_폴더_생성_요청;
import static com.baro.common.acceptance.memofolder.MemoFolderAcceptanceSteps.메모_폴더_수정_요청_성공;
import static com.baro.common.acceptance.memofolder.MemoFolderAcceptanceSteps.메모_폴더_수정_요청_실패;
import static com.baro.common.acceptance.memofolder.MemoFolderAcceptanceSteps.메모_폴더를_생성_하고_ID를_반환한다;
import static com.baro.common.acceptance.memofolder.MemoFolderAcceptanceSteps.잘못된_메모_폴더_생성_요청;
import static com.baro.common.acceptance.memofolder.MemoFolderAcceptanceSteps.폴더_삭제_요청_성공;
import static com.baro.common.acceptance.memofolder.MemoFolderAcceptanceSteps.폴더_삭제_요청_실패;
import static com.baro.common.acceptance.memofolder.MemoFolderAcceptanceSteps.폴더_이름_길이_초과_바디;
import static com.baro.common.acceptance.memofolder.MemoFolderAcceptanceSteps.폴더_이름_바디;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willThrow;

import com.baro.common.RestApiTest;
import com.baro.member.domain.MemberRepository;
import com.baro.member.exception.MemberException;
import com.baro.member.exception.MemberExceptionType;
import com.baro.memofolder.domain.MemoFolderRepository;
import com.baro.memofolder.presentation.dto.DeleteMemoFolderRequest;
import com.baro.memofolder.presentation.dto.RenameMemoFolderRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemoFolderApiTest extends RestApiTest {

    @SpyBean
    MemberRepository memberRepository;
    @Autowired
    MemoFolderRepository memoFolderRepository;

    @Test
    void 메모_폴더를_생성한다() {
        // given
        var 요청_바디 = 폴더_이름_바디;
        var 토큰 = 로그인(태연());

        // when
        var 응답 = 메모_폴더_생성_요청(토큰, 요청_바디);

        // then
        응답값을_검증한다(응답, 생성됨);
        응답의_Location_헤더가_존재한다(응답);
    }

    @Test
    void 중복되는_이름의_폴더를_생성하는_경우_예외를_반환한다() {
        // given
        var 요청_바디 = 폴더_이름_바디;
        var 토큰 = 로그인(유빈());
        메모_폴더_생성_요청(토큰, 요청_바디);

        // when
        var 응답 = 잘못된_메모_폴더_생성_요청(토큰, 요청_바디);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    @Test
    void 존재하지_않는_멤버가_폴더를_생성하는_경우_예외를_반환한다() {
        // given
        var 요청_바디 = 폴더_이름_바디;
        var 토큰 = 로그인(동균());
        멤버가_존재하지_않는다();

        // when
        var 응답 = 잘못된_메모_폴더_생성_요청(토큰, 요청_바디);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    @Test
    void 최대치_이름_길이를_초과하는_폴더를_생성하는_경우_예외를_반환한다() {
        // given
        var 요청_바디 = 폴더_이름_길이_초과_바디;
        var 토큰 = 로그인(은지());

        // when
        var 응답 = 잘못된_메모_폴더_생성_요청(토큰, 요청_바디);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    @Test
    void 메모_폴더를_불러온다() {
        // given
        var 토큰 = 로그인(원진());
        메모_폴더_생성_요청(토큰, 폴더_이름_바디);

        // when
        var 응답 = 메모_폴더_불러오기_요청(토큰);

        // then
        응답값을_검증한다(응답, 성공);
    }

    @Test
    void 폴더_이름을_변경한다() {
        // given
        var 토큰 = 로그인(원진());
        var 메모폴더ID = 메모_폴더를_생성_하고_ID를_반환한다(토큰, 폴더_이름_바디);
        var 바디 = new RenameMemoFolderRequest(메모폴더ID, "변경된 이름");

        // when
        var 응답 = 메모_폴더_수정_요청_성공(토큰, 바디);

        // then
        응답값을_검증한다(응답, 응답값_없음);
    }

    @Test
    void 폴더이름_수정시_이름이_중복된_경우_예외_발생() {
        // given
        var 토큰 = 로그인(원진());
        var 메모폴더ID = 메모_폴더를_생성_하고_ID를_반환한다(토큰, 폴더_이름_바디);
        var 바디 = new RenameMemoFolderRequest(메모폴더ID, 폴더_이름_바디.folderName());

        // when
        var 응답 = 메모_폴더_수정_요청_실패(토큰, 바디);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    @Test
    void 폴더이름_수정시_제한길이가_초과된_경우_예외_발생() {
        // given
        var 토큰 = 로그인(원진());
        var 메모폴더ID = 메모_폴더를_생성_하고_ID를_반환한다(토큰, 폴더_이름_바디);
        var 바디 = new RenameMemoFolderRequest(메모폴더ID, 폴더_이름_길이_초과_바디.folderName());

        // when
        var 응답 = 메모_폴더_수정_요청_실패(토큰, 바디);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    @Test
    void 폴더이름_수정시_해당_폴더의_주인이_아닌_경우_예외_발생() {
        // given
        var 원진토큰 = 로그인(원진());
        var 태연토큰 = 로그인(태연());
        var 메모폴더ID = 메모_폴더를_생성_하고_ID를_반환한다(원진토큰, 폴더_이름_바디);
        var 바디 = new RenameMemoFolderRequest(메모폴더ID, "변경된 이름");

        // when
        var 응답 = 메모_폴더_수정_요청_실패(태연토큰, 바디);

        // then
        응답값을_검증한다(응답, 권한_없음);
    }

    @Test
    void 폴더이름_수정시_빈_문자열일_경우_예외_발생() {
        // given
        var 토큰 = 로그인(원진());
        var 메모폴더ID = 메모_폴더를_생성_하고_ID를_반환한다(토큰, 폴더_이름_바디);
        var 바디 = new RenameMemoFolderRequest(메모폴더ID, "");
        멤버가_존재하지_않는다();

        // when
        var 응답 = 메모_폴더_수정_요청_실패(토큰, 바디);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    @Test
    void 메모폴더를_삭제할때_내부에있는_모든_아카이브를_함께_삭제한다() {
        // given
        var 토큰 = 로그인(원진());
        var 메모폴더ID = 메모_폴더를_생성_하고_ID를_반환한다(토큰, 폴더_이름_바디);
        var 바디 = new DeleteMemoFolderRequest(메모폴더ID, true);

        // when
        var 응답 = 폴더_삭제_요청_성공(토큰, 바디);

        // then
        응답값을_검증한다(응답, 응답값_없음);
    }

    @Test
    void 메모폴더를_삭제할때_내부에있는_모든_아카이브_기본폴더로_이동시킨다() {
        // given
        var 토큰 = 로그인(원진());
        var 메모폴더ID = 메모_폴더를_생성_하고_ID를_반환한다(토큰, 폴더_이름_바디);
        var 바디 = new DeleteMemoFolderRequest(메모폴더ID, false);

        // when
        var 응답 = 폴더_삭제_요청_성공(토큰, 바디);

        // then
        응답값을_검증한다(응답, 응답값_없음);
    }

    @Test
    void 메모폴더_삭제시_해당하는_메모폴더가_없는경우_에러를_반환한다() {
        // given
        var 토큰 = 로그인(원진());
        var 존재하지않는_메모폴더ID = 999L;
        var 바디 = new DeleteMemoFolderRequest(존재하지않는_메모폴더ID, false);

        // when
        var 응답 = 폴더_삭제_요청_실패(토큰, 바디);

        // then
        응답값을_검증한다(응답, 존재하지_않음);
    }

    @Test
    void 폴더_삭제시_폴더의_주인이_아닌경우_에러를_반환한다() {
        // given
        var 원진토큰 = 로그인(원진());
        var 준희토큰 = 로그인(준희());
        var 메모폴더ID = 메모_폴더를_생성_하고_ID를_반환한다(원진토큰, 폴더_이름_바디);
        var 바디 = new DeleteMemoFolderRequest(메모폴더ID, false);

        // when
        var 응답 = 폴더_삭제_요청_실패(준희토큰, 바디);

        // then
        응답값을_검증한다(응답, 권한_없음);
    }

    @Test
    void 삭제하려는_폴더가_기본폴더라면_에러를_반환한다() {
        // given
        var 토큰 = 로그인(원진());
        var 기본폴더ID = 기본폴더의_ID();
        var 메모폴더ID = 메모_폴더를_생성_하고_ID를_반환한다(토큰, 폴더_이름_바디);
        var 바디 = new DeleteMemoFolderRequest(기본폴더ID, false);

        // when
        var 응답 = 폴더_삭제_요청_실패(토큰, 바디);

        // then
        응답값을_검증한다(응답, 권한_없음);
    }

    private void 멤버가_존재하지_않는다() {
        willThrow(new MemberException(MemberExceptionType.NOT_EXIST_MEMBER)).given(memberRepository).getById(anyLong());
    }

    private Long 기본폴더의_ID() {
        return memoFolderRepository.findAll().get(0).getId();
    }
}
