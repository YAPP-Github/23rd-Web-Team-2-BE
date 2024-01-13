package com.baro.auth.presentation.oauth;

import static com.baro.common.acceptance.AcceptanceSteps.리디렉션;
import static com.baro.common.acceptance.AcceptanceSteps.성공;
import static com.baro.common.acceptance.AcceptanceSteps.응답값을_검증한다;
import static com.baro.common.acceptance.AcceptanceSteps.응답의_Location_헤더가_존재한다;
import static com.baro.common.acceptance.auth.OAuthAcceptanceSteps.로그인_요청;
import static com.baro.common.acceptance.auth.OAuthAcceptanceSteps.리다이렉트_URI_요청;

import com.baro.auth.fixture.OAuthMemberInfoFixture;
import com.baro.common.RestApiTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OAuthApiTest extends RestApiTest {

    @Test
    void OAuth_로그인시_필요한_url을_반환한다() {
        // when
        var 응답 = 리다이렉트_URI_요청("kakao");

        // then
        응답값을_검증한다(응답, 리디렉션);
        응답의_Location_헤더가_존재한다(응답);
    }

    @Test
    void OAuth로_로그인한다() {
        // given
        OAuth_서버로부터_멤버_정보를_불러온다(OAuthMemberInfoFixture.유빈());

        // when
        var 응답 = 로그인_요청();

        // then
        응답값을_검증한다(응답, 성공);
    }
}
