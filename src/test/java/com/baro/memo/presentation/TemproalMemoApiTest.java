package com.baro.memo.presentation;

import static com.baro.auth.fixture.OAuthMemberInfoFixture.아현;
import static com.baro.auth.fixture.OAuthMemberInfoFixture.유빈;
import static com.baro.auth.fixture.OAuthMemberInfoFixture.준희;
import static com.baro.auth.fixture.OAuthMemberInfoFixture.태연;
import static com.baro.common.acceptance.AcceptanceSteps.권한_없음;
import static com.baro.common.acceptance.AcceptanceSteps.생성됨;
import static com.baro.common.acceptance.AcceptanceSteps.응답값_없음;
import static com.baro.common.acceptance.AcceptanceSteps.응답값을_검증한다;
import static com.baro.common.acceptance.AcceptanceSteps.응답의_Location_헤더가_존재한다;
import static com.baro.common.acceptance.AcceptanceSteps.잘못된_요청;
import static com.baro.common.acceptance.AcceptanceSteps.존재하지_않음;
import static com.baro.common.acceptance.memo.TemporalMemoAcceptanceSteps.끄적이는_메모_맞춤법_검사_결과_반영_요청;
import static com.baro.common.acceptance.memo.TemporalMemoAcceptanceSteps.끄적이는_메모_바디;
import static com.baro.common.acceptance.memo.TemporalMemoAcceptanceSteps.끄적이는_메모_수정_바디;
import static com.baro.common.acceptance.memo.TemporalMemoAcceptanceSteps.끄적이는메모_삭제_요청;
import static com.baro.common.acceptance.memo.TemporalMemoAcceptanceSteps.끄적이는메모_생성_요청;
import static com.baro.common.acceptance.memo.TemporalMemoAcceptanceSteps.끄적이는메모_수정_요청;
import static com.baro.common.acceptance.memo.TemporalMemoAcceptanceSteps.끄적이는메모_아카이빙_요청;
import static com.baro.common.acceptance.memo.TemporalMemoAcceptanceSteps.끄적이는메모를_생성하고_ID를_반환한다;
import static com.baro.common.acceptance.memo.TemporalMemoAcceptanceSteps.맞춤법_검사_결과_반영_바디;
import static com.baro.common.acceptance.memo.TemporalMemoAcceptanceSteps.메모_아카이브_요청_바디;
import static com.baro.common.acceptance.memo.TemporalMemoAcceptanceSteps.잘못된_끄적이는_메모_맞춤법_검사_결과_반영_요청;
import static com.baro.common.acceptance.memo.TemporalMemoAcceptanceSteps.잘못된_끄적이는_메모_생성_요청;
import static com.baro.common.acceptance.memo.TemporalMemoAcceptanceSteps.잘못된_끄적이는메모_삭제_요청;
import static com.baro.common.acceptance.memo.TemporalMemoAcceptanceSteps.잘못된_끄적이는메모_수정_요청;
import static com.baro.common.acceptance.memo.TemporalMemoAcceptanceSteps.잘못된_끄적이는메모_아카이빙_요청;
import static com.baro.common.acceptance.memo.TemporalMemoAcceptanceSteps.크기_초과_끄적이는_메모_수정_바디;
import static com.baro.common.acceptance.memo.TemporalMemoAcceptanceSteps.크기_초과_끄적이는_메모_작성_바디;
import static com.baro.common.acceptance.memo.TemporalMemoAcceptanceSteps.크기_초과_맞춤법_검사_결과_반영_바디;
import static com.baro.common.acceptance.memofolder.MemoFolderAcceptanceSteps.메모_폴더를_생성_하고_ID를_반환한다;
import static com.baro.common.acceptance.memofolder.MemoFolderAcceptanceSteps.폴더_이름_바디;

import com.baro.common.RestApiTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class TemproalMemoApiTest extends RestApiTest {

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
    void 끄적이는_메모를_삭제_한다() {
        // given
        var 아현 = 로그인(아현());
        var 아현의_끄적이는_메모_ID = 끄적이는메모를_생성하고_ID를_반환한다(아현, 끄적이는_메모_바디);

        // when
        var 응답 = 끄적이는메모_삭제_요청(아현, 아현의_끄적이는_메모_ID);

        // then
        응답값을_검증한다(응답, 응답값_없음);
    }

    @Test
    void 다른_사람의_끄적이는_메모_삭제시_예외를_반환한다() {
        // given
        var 아현 = 로그인(아현());
        var 아현의_끄적이는_메모_ID = 끄적이는메모를_생성하고_ID를_반환한다(아현, 끄적이는_메모_바디);

        var 태연 = 로그인(태연());

        // when
        var 응답 = 잘못된_끄적이는메모_삭제_요청(태연, 아현의_끄적이는_메모_ID);

        // then
        응답값을_검증한다(응답, 권한_없음);
    }

    @Test
    void 존재_하지_않는_끄적이는_메모_삭제시_예외를_반환한다() {
        // given
        var 아현 = 로그인(아현());
        var 존재_하지_않는_끄적이는_메모_ID = 999L;

        // when
        var 응답 = 잘못된_끄적이는메모_삭제_요청(아현, 존재_하지_않는_끄적이는_메모_ID);

        // then
        응답값을_검증한다(응답, 존재하지_않음);
    }

    @Test
    void 끄적이는_메모의_맞춤법_검사_결과를_반영_한다() {
        // given
        var 준희 = 로그인(준희());
        var 준희의_끄적이는_메모_ID = 끄적이는메모를_생성하고_ID를_반환한다(준희, 끄적이는_메모_바디);

        // when
        var 응답 = 끄적이는_메모_맞춤법_검사_결과_반영_요청(준희, 준희의_끄적이는_메모_ID, 맞춤법_검사_결과_반영_바디);

        // then
        응답값을_검증한다(응답, 응답값_없음);
    }

    @Test
    void 다른_사람의_끄적이는_메모의_맞춤법_검사_결과를_반영_시_예외를_반환한다() {
        // given
        var 준희 = 로그인(준희());
        var 준희의_끄적이는_메모_ID = 끄적이는메모를_생성하고_ID를_반환한다(준희, 끄적이는_메모_바디);

        var 태연 = 로그인(태연());

        // when
        var 응답 = 잘못된_끄적이는_메모_맞춤법_검사_결과_반영_요청(태연, 준희의_끄적이는_메모_ID, 맞춤법_검사_결과_반영_바디);

        // then
        응답값을_검증한다(응답, 권한_없음);
    }

    @Test
    void 존재_하지_않는_끄적이는_메모의_맞춤법_검사_결과를_반영_시_예외를_반환한다() {
        // given
        var 준희 = 로그인(준희());
        var 존재_하지_않는_끄적이는_메모_ID = 999L;

        // when
        var 응답 = 잘못된_끄적이는_메모_맞춤법_검사_결과_반영_요청(준희, 존재_하지_않는_끄적이는_메모_ID, 맞춤법_검사_결과_반영_바디);

        // then
        응답값을_검증한다(응답, 존재하지_않음);
    }

    @Test
    void 최대_길이를_초과하는_맞춤법_검사_결과를_반영_시_예외를_반환한다() {
        // given
        var 준희 = 로그인(준희());
        var 준희의_끄적이는_메모_ID = 끄적이는메모를_생성하고_ID를_반환한다(준희, 끄적이는_메모_바디);

        // when
        var 응답 = 잘못된_끄적이는_메모_맞춤법_검사_결과_반영_요청(준희, 준희의_끄적이는_메모_ID, 크기_초과_맞춤법_검사_결과_반영_바디);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    @Test
    void 이미_맞춤법_검사_결과가_존재하는_끄적이는_메모에_결과_반영_시_예외를_반환한다() {
        // given
        var 준희 = 로그인(준희());
        var 준희의_끄적이는_메모_ID = 끄적이는메모를_생성하고_ID를_반환한다(준희, 끄적이는_메모_바디);
        끄적이는_메모_맞춤법_검사_결과_반영_요청(준희, 준희의_끄적이는_메모_ID, 맞춤법_검사_결과_반영_바디);

        // when
        var 응답 = 잘못된_끄적이는_메모_맞춤법_검사_결과_반영_요청(준희, 준희의_끄적이는_메모_ID, 맞춤법_검사_결과_반영_바디);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }
}
