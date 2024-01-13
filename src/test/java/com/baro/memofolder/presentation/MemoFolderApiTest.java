package com.baro.memofolder.presentation;

import static com.baro.auth.fixture.OAuthMemberInfoFixture.동균;
import static com.baro.auth.fixture.OAuthMemberInfoFixture.원진;
import static com.baro.auth.fixture.OAuthMemberInfoFixture.유빈;
import static com.baro.auth.fixture.OAuthMemberInfoFixture.은지;
import static com.baro.auth.fixture.OAuthMemberInfoFixture.태연;
import static com.baro.common.acceptance.AcceptanceSteps.생성됨;
import static com.baro.common.acceptance.AcceptanceSteps.성공;
import static com.baro.common.acceptance.AcceptanceSteps.응답값을_검증한다;
import static com.baro.common.acceptance.AcceptanceSteps.응답의_Location_헤더가_존재한다;
import static com.baro.common.acceptance.AcceptanceSteps.잘못된_요청;
import static com.baro.common.acceptance.memofolder.MemoFolderAcceptanceSteps.메모_폴더_불러오기_요청;
import static com.baro.common.acceptance.memofolder.MemoFolderAcceptanceSteps.메모_폴더_생성_요청;
import static com.baro.common.acceptance.memofolder.MemoFolderAcceptanceSteps.잘못된_메모_폴더_생성_요청;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willThrow;

import com.baro.auth.domain.Token;
import com.baro.common.RestApiTest;
import com.baro.member.domain.MemberRepository;
import com.baro.member.exception.MemberException;
import com.baro.member.exception.MemberExceptionType;
import com.baro.memofolder.presentation.dto.SaveMemoFolderRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class MemoFolderApiTest extends RestApiTest {

    private final SaveMemoFolderRequest 정상_바디 = new SaveMemoFolderRequest("회사생활👔");
    private final SaveMemoFolderRequest 폴더_이름_길이_초과_바디 = new SaveMemoFolderRequest("회사생활은재미없겠지만해야겠지👔👔👔");
    @SpyBean
    MemberRepository memberRepository;

    @Test
    void 메모_폴더를_생성한다() {
        // given
        var 요청_바디 = 정상_바디;
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
        var 요청_바디 = 정상_바디;
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
        var 요청_바디 = 정상_바디;
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
        Token 토큰 = 로그인(원진());
        메모_폴더_생성_요청(토큰, 정상_바디);

        // when
        var 응답 = 메모_폴더_불러오기_요청(토큰);

        // then
        응답값을_검증한다(응답, 성공);
    }

    void 멤버가_존재하지_않는다() {
        willThrow(new MemberException(MemberExceptionType.NOT_EXIST_MEMBER)).given(memberRepository).getById(anyLong());
    }
}
