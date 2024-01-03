package com.baro.auth.application.oauth;

import static org.assertj.core.api.Assertions.assertThat;

import com.baro.auth.application.AuthService;
import com.baro.auth.application.TokenTranslator;
import com.baro.auth.fake.oauth.*;
import com.baro.member.application.MemberCreator;
import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import com.baro.member.fake.FakeMemberRepository;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuthServiceTest {

    private AuthService authService;
    private MemberRepository memberRepository;

    @BeforeEach
    void setUpOAuthClientRequest() {
        memberRepository = new FakeMemberRepository();
        MemberCreator memberCreator = new MemberCreator(memberRepository, new FakeNicknameCreator());
        TokenTranslator tokenTranslator = new FakeTokenTranslator();
        authService = new AuthService(memberRepository, memberCreator, tokenTranslator);
    }

    @Test
    void Kakao로_최초_소셜_로그인시_회원을_추가_한다() {
        // given
        String name = "kakaoName";
        String email = "kakaoEmail@test.com";
        String oauthId = "kakaoId";
        String oauthType = "kakao";

        // when
        authService.signIn(name, email, oauthId, oauthType);

        // then
        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(1);
    }

    @Test
    void Google로_최초_소셜_로그인시_회원을_추가_한다() {
        // given
        String name = "googleName";
        String email = "googleEmail@test.com";
        String oauthId = "googleId";
        String oauthType = "google";

        // when
        authService.signIn(name, email, oauthId, oauthType);

        // then
        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(1);
    }

    @Test
    void Naver로_최초_소셜_로그인시_회원을_추가_한다() {
        // given
        String name = "naverName";
        String email = "naverEmail@test.com";
        String oauthId = "naverId";
        String oauthType = "naver";

        // when
        authService.signIn(name, email, oauthId, oauthType);

        // then
        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(1);
    }

    @Test
    void Kakao_로그인시_이미_존재_하는_회원인_경우_추가_절차_없이_로그인_처리_한다() {
        // given
        memberRepository.save(Member.builder()
                .name("kakaoName")
                .email("kakaoEmail")
                .oAuthId("kakaoId")
                .oAuthServiceType("kakao")
                .build());
        String name = "kakaoName";
        String email = "kakaoEmail@test.com";
        String oauthId = "kakaoId";
        String oauthType = "kakao";

        // when
        authService.signIn(name, email, oauthId, oauthType);

        // then
        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(1);
    }

    @Test
    void Naver_로그인시_이미_존재_하는_회원인_경우_추가_절차_없이_로그인_처리_한다() {
        // given
        memberRepository.save(Member.builder()
                .name("naverName")
                .email("naverEmail")
                .oAuthId("naverId")
                .oAuthServiceType("naver")
                .build());
        String name = "naverName";
        String email = "naverEmail@test.com";
        String oauthId = "naverId";
        String oauthType = "naver";

        // when
        authService.signIn(name, email, oauthId, oauthType);

        // then
        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(1);
    }

    @Test
    void Google_로그인시_이미_존재_하는_회원인_경우_추가_절차_없이_로그인_처리_한다() {
        // given
        memberRepository.save(Member.builder()
                .name("googleName")
                .email("googleEmail")
                .oAuthId("googleId")
                .oAuthServiceType("google")
                .build());
        String name = "googleName";
        String email = "googleEmail@test.com";
        String oauthId = "googleId";
        String oauthType = "google";

        // when
        authService.signIn(name, email, oauthId, oauthType);

        // then
        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(1);
    }
}
