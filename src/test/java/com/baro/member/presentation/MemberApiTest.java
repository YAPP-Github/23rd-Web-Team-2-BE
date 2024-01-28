package com.baro.member.presentation;

import static com.baro.auth.fixture.OAuthMemberInfoFixture.유빈;
import static com.baro.auth.fixture.OAuthMemberInfoFixture.태연;
import static com.baro.common.acceptance.AcceptanceSteps.성공;
import static com.baro.common.acceptance.AcceptanceSteps.응답값_없음;
import static com.baro.common.acceptance.AcceptanceSteps.응답값을_검증한다;
import static com.baro.common.acceptance.AcceptanceSteps.잘못된_요청;
import static com.baro.common.acceptance.member.MemberAcceptanceSteps.길이가_초과된_프로필_수정_바디;
import static com.baro.common.acceptance.member.MemberAcceptanceSteps.내_프로필_수정_요청;
import static com.baro.common.acceptance.member.MemberAcceptanceSteps.내_프로필_조회_요청;
import static com.baro.common.acceptance.member.MemberAcceptanceSteps.빈_닉네임_프로필_수정_바디;
import static com.baro.common.acceptance.member.MemberAcceptanceSteps.잘못된_내_프로필_수정_요청;
import static com.baro.common.acceptance.member.MemberAcceptanceSteps.잘못된_프로필_조회_요청;
import static com.baro.common.acceptance.member.MemberAcceptanceSteps.프로필_수정_바디;
import static com.baro.common.acceptance.member.MemberAcceptanceSteps.프로필_이미지;
import static com.baro.common.acceptance.member.MemberAcceptanceSteps.프로필_이미지_삭제_요청;
import static com.baro.common.acceptance.member.MemberAcceptanceSteps.프로필_이미지_수정_요청;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.baro.auth.application.TokenTranslator;
import com.baro.auth.domain.Token;
import com.baro.common.RestApiTest;
import com.baro.common.image.ImageStorageClient;
import com.baro.common.image.dto.ImageUploadResult;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.web.multipart.MultipartFile;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class MemberApiTest extends RestApiTest {

    @SpyBean
    private TokenTranslator tokenTranslator;

    @MockBean
    private ImageStorageClient imageStorageClient;

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

    @Test
    void 내_프로필_정보를_수정_한다() {
        // given
        var 토큰 = 로그인(태연());

        // when
        var 응답 = 내_프로필_수정_요청(토큰, 프로필_수정_바디);

        // then
        응답값을_검증한다(응답, 응답값_없음);
    }

    @Test
    void 존재하지_않는_멤버가_프로필을_수정할시_예외를_반환_한다() {
        // given
        var 토큰 = 로그인(태연());
        멤버가_존재하지_않는다(토큰);

        // when
        var 응답 = 잘못된_내_프로필_수정_요청(토큰, 프로필_수정_바디);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    @Test
    void 중복된_닉네임으로_프로필을_수정할시_예외를_반환_한다() {
        // given
        var 유빈 = 로그인(유빈());
        내_프로필_수정_요청(유빈, 프로필_수정_바디);
        var 토큰 = 로그인(태연());

        // when
        var 응답 = 잘못된_내_프로필_수정_요청(토큰, 프로필_수정_바디);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    @Test
    void 닉네임의_길이_초과시_예외를_반환_한다() {
        // given
        var 토큰 = 로그인(태연());

        // when
        var 응답 = 잘못된_내_프로필_수정_요청(토큰, 길이가_초과된_프로필_수정_바디);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    @Test
    void 빈_닉네임으로_수정시_예외를_반환_한다() {
        // given
        var 토큰 = 로그인(태연());

        // when
        var 응답 = 잘못된_내_프로필_수정_요청(토큰, 빈_닉네임_프로필_수정_바디);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    private void 멤버가_존재하지_않는다(Token 토큰) {
        given(tokenTranslator.decodeAccessToken("Bearer " + 토큰.accessToken())).willReturn(999L);
    }

    //TODO : 프로필 이미지 등록 API 개발 후 추가

    @Test
    void 프로필_이미지가_없는_경우_이미지_삭제시_예외_발생() {
        // given
        var 토큰 = 로그인(태연());

        // when
        var 응답 = 프로필_이미지_삭제_요청(토큰);

        // then
        응답값을_검증한다(응답, 잘못된_요청);
    }

    @Test
    void 프로필_이미지를_수정한다() {
        // given
        var 토큰 = 로그인(태연());
        이미지를_등록한다(프로필_이미지);

        // when
        var 응답 = 프로필_이미지_수정_요청(토큰, 프로필_이미지);

        // then
        응답값을_검증한다(응답, 응답값_없음);
    }

    private void 이미지를_등록한다(MultipartFile 이미지) {
        given(imageStorageClient.upload(any())).willReturn(new ImageUploadResult(이미지.getOriginalFilename()));
    }
}
