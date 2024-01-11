package com.baro.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.baro.auth.application.dto.SignInDto;
import com.baro.auth.domain.Token;
import com.baro.auth.exception.AuthException;
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
    private TokenStorage tokenStorage;
    private TokenTranslator tokenTranslator;
    private MemberCreator memberCreator;

    @BeforeEach
    void setUp() {
        memberRepository = new FakeMemberRepository();
        tokenTranslator = new FakeTokenTranslator();
        FakeNicknameCreator fakeNicknameCreator = new FakeNicknameCreator(List.of("nickname1", "nickname2"));
        memberCreator = new MemberCreator(memberRepository, fakeNicknameCreator);
        tokenStorage = new FakeTokenStorage(1000 * 60 * 60 * 24);
        authService = new AuthService(memberRepository, memberCreator, tokenTranslator, tokenStorage);
    }

    @Test
    void Kakao로_최초_소셜_로그인시_회원을_추가_한다() {
        // given
        String name = "kakaoName";
        String email = "kakaoEmail@test.com";
        String oauthId = "kakaoId";
        String oauthType = "kakao";
        SignInDto dto = new SignInDto(name, email, oauthId, oauthType);

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
        SignInDto dto = new SignInDto(name, email, oauthId, oauthType);

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
        SignInDto dto = new SignInDto(name, email, oauthId, oauthType);

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
        memberRepository.save(Member.builder()
                .name(name)
                .email(email)
                .oAuthId(oauthId)
                .oAuthServiceType(oauthType)
                .build());
        SignInDto dto = new SignInDto(name, email, oauthId, oauthType);

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
        memberRepository.save(Member.builder()
                .name(name)
                .email(email)
                .oAuthId(oauthId)
                .oAuthServiceType(oauthType)
                .build());
        SignInDto dto = new SignInDto(name, email, oauthId, oauthType);

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
        memberRepository.save(Member.builder()
                .name(name)
                .email(email)
                .oAuthId(oauthId)
                .oAuthServiceType(oauthType)
                .build());
        SignInDto dto = new SignInDto(name, email, oauthId, oauthType);

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
        SignInDto dto = new SignInDto(name, email, oauthId, oauthType);

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
        SignInDto dto = new SignInDto(name, email, oauthId, oauthType);

        // when
        Token token = authService.signIn(dto);

        // then
        assertEquals(token.refreshToken(), "refreshToken");
    }

    @Test
    void 로그인시_리프레쉬_토큰을_저장한다() {
        // given
        String name = "kakaoName";
        String email = "kakaoEmail@test.com";
        String oauthId = "kakaoId";
        String oauthType = "kakao";
        SignInDto dto = new SignInDto(name, email, oauthId, oauthType);

        // when
        Token token = authService.signIn(dto);

        // then
        assertEquals(tokenStorage.findRefreshToken(String.valueOf(1)), token.refreshToken());
    }

    @Test
    void 정상적인_토큰의_재발급_요청() {
        // given
        String name = "kakaoName";
        String email = "kakaoEmail@test.com";
        String oauthId = "kakaoId";
        String oauthType = "kakao";
        SignInDto dto = new SignInDto(name, email, oauthId, oauthType);
        Token token = authService.signIn(dto);

        // when
        Token reissuedToken = authService.reissue(1L, token.refreshToken());

        // then
        assertEquals("refreshToken", reissuedToken.refreshToken());
    }

    @Test
    void Refresh_Token_만료시_예외_발생() {
        // given
        String name = "kakaoName";
        String email = "kakaoEmail@test.com";
        String oauthId = "kakaoId";
        String oauthType = "kakao";
        SignInDto dto = new SignInDto(name, email, oauthId, oauthType);
        AuthService service = new AuthService(memberRepository, memberCreator, tokenTranslator, new FakeTokenStorage(0));
        Token token = service.signIn(dto);

        // then
        assertThrows(AuthException.class, () -> authService.reissue(1L, token.refreshToken()));
    }

    @Test
    void 클라이언트가_가지고있던_Refresh_Token이_서버에_존재하지_않을경우_예외_발생() {
        // given
        Token token = new Token("accessToken", "refreshToken");

        // then
        assertThrows(AuthException.class, () -> authService.reissue(1L, token.refreshToken()));
    }
}
