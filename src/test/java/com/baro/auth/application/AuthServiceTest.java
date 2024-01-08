package com.baro.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.baro.auth.application.dto.SignInDto;
import com.baro.auth.domain.Token;
import com.baro.auth.fake.jwt.FakeTokenStorage;
import com.baro.auth.fake.jwt.FakeTokenTranslator;
import com.baro.member.application.MemberCreator;
import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import com.baro.member.fake.FakeMemberRepository;
import com.baro.member.fake.FakeNicknameCreator;
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
        TokenTranslator tokenTranslator = new FakeTokenTranslator();
        FakeNicknameCreator fakeNicknameCreator = new FakeNicknameCreator(List.of("nickname1", "nickname2"));
        MemberCreator memberCreator = new MemberCreator(memberRepository, fakeNicknameCreator);
        TokenStorage tokenStorage = new FakeTokenStorage();
        authService = new AuthService(memberRepository, memberCreator, tokenTranslator, tokenStorage);
    }

    @Test
    void Kakao로_최초_소셜_로그인시_회원을_추가_한다() {
        // given
        String name = "kakaoName";
        String email = "kakaoEmail@test.com";
        String oauthId = "kakaoId";
        String oauthType = "kakao";
        String ipAddress = "127.0.0.1";
        SignInDto dto = new SignInDto(name, email, oauthId, oauthType, ipAddress);

        // when
        authService.signIn(dto);

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
        String ipAddress = "127.0.0.1";
        SignInDto dto = new SignInDto(name, email, oauthId, oauthType, ipAddress);

        // when
        authService.signIn(dto);

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
        String ipAddress = "127.0.0.1";
        SignInDto dto = new SignInDto(name, email, oauthId, oauthType, ipAddress);

        // when
        authService.signIn(dto);

        // then
        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(1);
    }

    @Test
    void Kakao_로그인시_이미_존재_하는_회원인_경우_추가_절차_없이_로그인_처리_한다() {
        // given
        String name = "kakaoName";
        String email = "kakaoEmail@test.com";
        String oauthId = "kakaoId";
        String oauthType = "kakao";
        String ipAddress = "127.0.0.1";
        memberRepository.save(Member.builder()
                .name(name)
                .email(email)
                .oAuthId(oauthId)
                .oAuthServiceType(oauthType)
                .build());
        SignInDto dto = new SignInDto(name, email, oauthId, oauthType, ipAddress);

        // when
        authService.signIn(dto);

        // then
        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(1);
    }

    @Test
    void Naver_로그인시_이미_존재_하는_회원인_경우_추가_절차_없이_로그인_처리_한다() {
        // given
        String name = "naverName";
        String email = "naverEmail@test.com";
        String oauthId = "naverId";
        String oauthType = "naver";
        String ipAddress = "127.0.0.1";
        memberRepository.save(Member.builder()
                .name(name)
                .email(email)
                .oAuthId(oauthId)
                .oAuthServiceType(oauthType)
                .build());
        SignInDto dto = new SignInDto(name, email, oauthId, oauthType, ipAddress);

        // when
        authService.signIn(dto);

        // then
        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(1);
    }

    @Test
    void Google_로그인시_이미_존재_하는_회원인_경우_추가_절차_없이_로그인_처리_한다() {
        // given
        String name = "googleName";
        String email = "googleEmail@test.com";
        String oauthId = "googleId";
        String oauthType = "google";
        String ipAddress = "127.0.0.1";
        memberRepository.save(Member.builder()
                .name(name)
                .email(email)
                .oAuthId(oauthId)
                .oAuthServiceType(oauthType)
                .build());
        SignInDto dto = new SignInDto(name, email, oauthId, oauthType, ipAddress);

        // when
        authService.signIn(dto);

        // then
        List<Member> members = memberRepository.findAll();
        assertThat(members).hasSize(1);
    }

    @Test
    void 로그인시_액세스_토큰을_반환한다() {
        // given
        String name = "kakaoName";
        String email = "kakaoEmail@test.com";
        String oauthId = "kakaoId";
        String oauthType = "kakao";
        String ipAddress = "127.0.0.1";
        SignInDto dto = new SignInDto(name, email, oauthId, oauthType, ipAddress);

        // when
        Token token = authService.signIn(dto);

        // then
        assertEquals(token.accessToken(), "accessToken");
    }

    @Test
    void 로그인시_리프레쉬_토큰을_반환한다() {
        // given
        String name = "kakaoName";
        String email = "kakaoEmail@test.com";
        String oauthId = "kakaoId";
        String oauthType = "kakao";
        String ipAddress = "127.0.0.1";
        SignInDto dto = new SignInDto(name, email, oauthId, oauthType, ipAddress);

        // when
        Token token = authService.signIn(dto);

        // then
        assertEquals(token.refreshToken(), "refreshToken");
    }
}
