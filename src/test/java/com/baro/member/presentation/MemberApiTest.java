package com.baro.member.presentation;

import static com.baro.auth.fixture.OAuthMemberInfoFixture.태연;
import static com.baro.common.acceptance.AcceptanceSteps.성공;
import static com.baro.common.acceptance.AcceptanceSteps.응답값을_검증한다;
import static com.baro.common.acceptance.member.MemberAcceptanceSteps.내_프로필_조회_요청;

import com.baro.common.RestApiTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class MemberApiTest extends RestApiTest {

    @Test
    void 내_프로필_정보를_조회_한다() {
        // given
        var 토큰 = 로그인(태연());

        // when
        var 응답 = 내_프로필_조회_요청(토큰);

        // then
        응답값을_검증한다(응답, 성공);
    }
}
