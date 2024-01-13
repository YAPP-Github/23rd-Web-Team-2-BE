package com.baro.memofolder.presentation;

import static com.baro.common.acceptance.AcceptanceSteps.생성됨;
import static com.baro.common.acceptance.AcceptanceSteps.성공;
import static com.baro.common.acceptance.AcceptanceSteps.응답값을_검증한다;
import static com.baro.common.acceptance.AcceptanceSteps.응답의_Location_헤더가_존재한다;
import static com.baro.common.acceptance.AcceptanceSteps.잘못된_요청;
import static com.baro.common.acceptance.memofolder.MemoFolderAcceptanceSteps.메모_폴더_불러오기_요청;
import static com.baro.common.acceptance.memofolder.MemoFolderAcceptanceSteps.메모_폴더_생성_요청;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;

import com.baro.auth.domain.Token;
import com.baro.auth.fixture.OAuthMemberInfoFixture;
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

    @SpyBean
    MemberRepository memberRepository;

    @Test
    void 메모_폴더를_생성한다() {
        // given
        var 요청_바디 = new SaveMemoFolderRequest("회사생활👔");
        var 토큰 = 로그인(OAuthMemberInfoFixture.태연());

        // when
        var 응답 = 메모_폴더_생성_요청(토큰, 요청_바디);

        // then
        응답값을_검증한다(응답, 생성됨);
        응답의_Location_헤더가_존재한다(응답);
    }

    @Test
    void 중복되는_이름의_폴더를_생성하는_경우_예외를_반환한다() {
        // given
        var 요청_바디 = new SaveMemoFolderRequest("회사생활👔");
        var 토큰 = 로그인(OAuthMemberInfoFixture.유빈());
        메모_폴더_생성_요청(토큰, 요청_바디);

        // when
        var 응답 = 메모_폴더_생성_요청(토큰, 요청_바디);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    @Test
    void 존재하지_않는_멤버가_폴더를_생성하는_경우_예외를_반환한다() {
        // given
        var 요청_바디 = new SaveMemoFolderRequest("회사생활👔");
        var 동균 = OAuthMemberInfoFixture.동균();
        var 토큰 = 로그인(동균);
        멤버가_존재하지_않는다();

        // when
        var 응답 = 메모_폴더_생성_요청(토큰, 요청_바디);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    @Test
    void 최대치_이름_길이를_초과하는_폴더를_생성하는_경우_예외를_반환한다() {
        // given
        var 요청_바디 = new SaveMemoFolderRequest("회사생활은재미없겠지만해야겠지👔👔👔");
        var 토큰 = 로그인(OAuthMemberInfoFixture.은지());

        // when
        var 응답 = 메모_폴더_생성_요청(토큰, 요청_바디);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    @Test
    void 메모_폴더를_불러온다() {
        // given
        Token 토큰 = 로그인(OAuthMemberInfoFixture.원진());
        메모_폴더_생성_요청(토큰, new SaveMemoFolderRequest("회사생활👔"));

        // when
        var 응답 = 메모_폴더_불러오기_요청(토큰);

        // then
        응답값을_검증한다(응답, 성공);
    }

    void 멤버가_존재하지_않는다() {
        doThrow(new MemberException(MemberExceptionType.NOT_EXIST_MEMBER)).when(memberRepository).getById(anyLong());
    }
}
