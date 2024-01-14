package com.baro.memo.presentation;

import static com.baro.auth.fixture.OAuthMemberInfoFixture.태연;
import static com.baro.common.acceptance.AcceptanceSteps.생성됨;
import static com.baro.common.acceptance.AcceptanceSteps.응답값을_검증한다;
import static com.baro.common.acceptance.AcceptanceSteps.응답의_Location_헤더가_존재한다;
import static com.baro.common.acceptance.AcceptanceSteps.잘못된_요청;
import static com.baro.common.acceptance.memo.MemoAcceptanceSteps.끄적이는메모_생성_요청;
import static com.baro.common.acceptance.memo.MemoAcceptanceSteps.잘못된_끄적이는_메모_생성_요청;

import com.baro.common.RestApiTest;
import com.baro.memo.presentation.dto.SaveTemporalMemoRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class MemoApiTest extends RestApiTest {

    private static final SaveTemporalMemoRequest 끄적이는_메모_컨텐츠 = new SaveTemporalMemoRequest("끄적이는 메모 컨텐츠");
    private static final SaveTemporalMemoRequest OVER_SIZE_MEMO_CONTENT = new SaveTemporalMemoRequest(
            "끄적이는 메모 컨텐츠".repeat(500));

    @Test
    void 끄적이는_메모를_작성_한다() {
        // given
        var 토큰 = 로그인(태연());
        var 요청_바디 = 끄적이는_메모_컨텐츠;

        // when
        var 응답 = 끄적이는메모_생성_요청(토큰, 요청_바디);

        // then
        응답값을_검증한다(응답, 생성됨);
        응답의_Location_헤더가_존재한다(응답);
    }

    @Test
    void create_temporal_memo_over_max_size_content() {
        // given
        var 토큰 = 로그인(태연());
        var 요청_바디 = OVER_SIZE_MEMO_CONTENT;

        // when
        var 응답 = 잘못된_끄적이는_메모_생성_요청(토큰, 요청_바디);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }
}
