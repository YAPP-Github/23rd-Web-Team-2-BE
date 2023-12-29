package com.baro.oauth.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import com.baro.member.fake.FakeMemberRepository;
import com.baro.oauth.fake.FakeKakaoOAuthClient;
import java.util.List;
import java.util.Set;

import com.baro.oauth.fake.FakeNaverOAuthClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OAuthServiceTest {

    private OAuthService oAuthService;

    private final MemberRepository memberRepository = new FakeMemberRepository();

    @BeforeEach
    void setUpOAuthClientRequest() {
        OAuthClient fakeKakaoOAuthClient = new FakeKakaoOAuthClient();
        OAuthClient fakeNaverOAuthClient = new FakeNaverOAuthClient();
        OAuthClientComponents components = new OAuthClientComponents(Set.of(fakeKakaoOAuthClient, fakeNaverOAuthClient));

        oAuthService = new OAuthService(components, memberRepository);
    }

    @Test
    void Kakao로_최초_소셜_로그인시_회원을_추가_한다() {
        // given
        String oAuthServiceType = "kakao";
        String authCode = "kakaoAuthCode";

        // when
        oAuthService.signIn(oAuthServiceType, authCode);

        // then
        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(1);
    }

    @Test
    void Naver로_최초_소셜_로그인시_회원을_추가_한다() {
        // given
        String oAuthServiceType = "naver";
        String authCode = "naverAuthCode";

        // when
        oAuthService.signIn(oAuthServiceType, authCode);

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
        String oAuthServiceType = "kakao";
        String authCode = "kakaoAuthCode";

        // when
        oAuthService.signIn(oAuthServiceType, authCode);

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
        String oAuthServiceType = "naver";
        String authCode = "naverAuthCode";

        // when
        oAuthService.signIn(oAuthServiceType, authCode);

        // then
        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(1);
    }
}
