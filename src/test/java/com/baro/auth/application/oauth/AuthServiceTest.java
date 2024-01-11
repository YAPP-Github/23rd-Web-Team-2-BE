package com.baro.auth.application.oauth;

import static org.assertj.core.api.Assertions.assertThat;

import com.baro.auth.application.AuthService;
import com.baro.auth.application.TokenTranslator;
import com.baro.auth.application.dto.SignInDto;
import com.baro.auth.domain.Token;
import com.baro.auth.fake.jwt.FakeTokenTranslator;
import com.baro.member.application.MemberCreator;
import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import com.baro.member.fake.FakeMemberRepository;
import com.baro.member.fake.FakeNicknameCreator;
import com.baro.memofolder.domain.MemoFolder;
import com.baro.memofolder.domain.MemoFolderRepository;
import com.baro.memofolder.fake.FakeMemoFolderRepository;
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
    private MemoFolderRepository memoFolderRepository;

    @BeforeEach
    void setUpOAuthClientRequest() {
        memberRepository = new FakeMemberRepository();
        TokenTranslator tokenTranslator = new FakeTokenTranslator();
        FakeNicknameCreator fakeNicknameCreator = new FakeNicknameCreator(List.of("nickname1", "nickname2"));
        memoFolderRepository = new FakeMemoFolderRepository();
        MemberCreator memberCreator = new MemberCreator(memberRepository, fakeNicknameCreator);

        authService = new AuthService(memberRepository, memberCreator, tokenTranslator, memoFolderRepository);
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
        assertThat(token.accessToken()).isEqualTo("accessToken");
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
        assertThat(token.refreshToken()).isEqualTo("refreshToken");
    }

    @Test
    void 최초_로그인시_기본_폴더를_생성한다() {
        // given
        String name = "kakaoName";
        String email = "kakaoEmail@test.com";
        String oauthId = "kakaoId";
        String oauthType = "kakao";
        SignInDto dto = new SignInDto(name, email, oauthId, oauthType);

        // when
        authService.signIn(dto);

        // then
        List<MemoFolder> all = memoFolderRepository.findAll();
        assertThat(all).hasSize(1);
        assertThat(all.get(0).getMember().getOAuthId()).isEqualTo(oauthId);
    }
}
