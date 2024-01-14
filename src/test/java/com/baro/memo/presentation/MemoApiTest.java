package com.baro.memo.presentation;

import static com.baro.auth.fixture.OAuthMemberInfoFixture.유빈;
import static com.baro.auth.fixture.OAuthMemberInfoFixture.태연;
import static com.baro.common.acceptance.AcceptanceSteps.권한_없음;
import static com.baro.common.acceptance.AcceptanceSteps.생성됨;
import static com.baro.common.acceptance.AcceptanceSteps.응답값_없음;
import static com.baro.common.acceptance.AcceptanceSteps.응답값을_검증한다;
import static com.baro.common.acceptance.AcceptanceSteps.응답의_Location_헤더가_존재한다;
import static com.baro.common.acceptance.AcceptanceSteps.잘못된_요청;
import static com.baro.common.acceptance.memo.MemoAcceptanceSteps.끄적이는메모_생성_요청;
import static com.baro.common.acceptance.memo.MemoAcceptanceSteps.끄적이는메모_수정_요청;
import static com.baro.common.acceptance.memo.MemoAcceptanceSteps.끄적이는메모를_생성하고_ID를_반환한다;
import static com.baro.common.acceptance.memo.MemoAcceptanceSteps.잘못된_끄적이는_메모_생성_요청;
import static com.baro.common.acceptance.memo.MemoAcceptanceSteps.잘못된_끄적이는메모_수정_요청;

import com.baro.common.RestApiTest;
import com.baro.memo.presentation.dto.SaveTemporalMemoRequest;
import com.baro.memo.presentation.dto.UpdateTemporalMemoRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class MemoApiTest extends RestApiTest {

    private static final String 크기_초과_컨텐츠 = "끄적이는 메모 컨텐츠".repeat(500);
    private static final SaveTemporalMemoRequest 끄적이는_메모_바디 = new SaveTemporalMemoRequest("끄적이는 메모 컨텐츠");
    private static final SaveTemporalMemoRequest 크기_초과_끄적이는_메모_작성_바디 = new SaveTemporalMemoRequest(크기_초과_컨텐츠);
    private static final UpdateTemporalMemoRequest 끄적이는_메모_수정_바디 = new UpdateTemporalMemoRequest("끄적이는 메모 컨텐츠");
    private static final UpdateTemporalMemoRequest 크기_초과_끄적이는_메모_수정_바디 = new UpdateTemporalMemoRequest(크기_초과_컨텐츠);

    @Test
    void 끄적이는_메모를_작성_한다() {
        // given
        var 토큰 = 로그인(태연());
        var 요청_바디 = 끄적이는_메모_바디;

        // when
        var 응답 = 끄적이는메모_생성_요청(토큰, 요청_바디);

        // then
        응답값을_검증한다(응답, 생성됨);
        응답의_Location_헤더가_존재한다(응답);
    }

    @Test
    void 최대치_이름_길이를_초과하는_끄적이는_메모_생성하는_경우_예외를_반환한다() {
        // given
        var 토큰 = 로그인(태연());
        var 요청_바디 = 크기_초과_끄적이는_메모_작성_바디;

        // when
        var 응답 = 잘못된_끄적이는_메모_생성_요청(토큰, 요청_바디);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    @Test
    void 끄적이는_메모를_수정_한다() {
        // given
        var 토큰 = 로그인(태연());
        var 끄적이는_메모_ID = 끄적이는메모를_생성하고_ID를_반환한다(토큰, 끄적이는_메모_바디);
        var 요청_바디 = 끄적이는_메모_수정_바디;

        // when
        var 응답 = 끄적이는메모_수정_요청(토큰, 끄적이는_메모_ID, 요청_바디);

        // then
        응답값을_검증한다(응답, 응답값_없음);
    }

    @Test
    void 최대치_이름_길이를_초과하는_컨텐츠로_끄적이는_메모_수정시_예외를_반환한다() {
        // given
        var 토큰 = 로그인(태연());
        var 끄적이는_메모_ID = 끄적이는메모를_생성하고_ID를_반환한다(토큰, 끄적이는_메모_바디);
        var 요청_바디 = 크기_초과_끄적이는_메모_수정_바디;

        // when
        var 응답 = 잘못된_끄적이는메모_수정_요청(토큰, 끄적이는_메모_ID, 요청_바디);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    @Test
    void 다른_사람의_끄적이는_메모_수정시_예외를_반환한다() {
        // given
        var 태연 = 로그인(태연());
        var 태연의_끄적이는_메모_ID = 끄적이는메모를_생성하고_ID를_반환한다(태연, 끄적이는_메모_바디);

        var 유빈 = 로그인(유빈());
        var 요청_바디 = 끄적이는_메모_수정_바디;

        // when
        var 응답 = 잘못된_끄적이는메모_수정_요청(유빈, 태연의_끄적이는_메모_ID, 요청_바디);

        // then
        응답값을_검증한다(응답, 권한_없음);
    }
}
