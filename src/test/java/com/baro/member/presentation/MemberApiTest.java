package com.baro.member.presentation;

import static com.baro.auth.fixture.OAuthMemberInfoFixture.태연;
import static com.baro.common.acceptance.AcceptanceSteps.성공;
import static com.baro.common.acceptance.AcceptanceSteps.응답값을_검증한다;
import static com.baro.common.acceptance.AcceptanceSteps.잘못된_요청;
import static com.baro.common.acceptance.member.MemberAcceptanceSteps.내_프로필_조회_요청;
import static com.baro.common.acceptance.member.MemberAcceptanceSteps.잘못된_프로필_조회_요청;
import static org.mockito.BDDMockito.given;

import com.baro.auth.application.TokenTranslator;
import com.baro.auth.domain.Token;
import com.baro.common.RestApiTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class MemberApiTest extends RestApiTest {

    @SpyBean
    private TokenTranslator tokenTranslator;

    @Test
    void 내_프로필_정보를_조회_한다() {
        // given
        var 토큰 = 로그인(태연());

        // when
        var 응답 = 내_프로필_조회_요청(토큰);

        // then
        응답값을_검증한다(응답, 성공);
    }

    @Test
    void 존재하지_않는_멤버가_프로필을_조회할시_예외를_반환_한다() {
        // given
        var 토큰 = 로그인(태연());
        멤버가_존재하지_않는다(토큰);

        // when
        var 응답 = 잘못된_프로필_조회_요청(토큰);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    private void 멤버가_존재하지_않는다(Token 토큰) {
        given(tokenTranslator.decodeAccessToken("Bearer " + 토큰.accessToken())).willReturn(999L);
    }
}
