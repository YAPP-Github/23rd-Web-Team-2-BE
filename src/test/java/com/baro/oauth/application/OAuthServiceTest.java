package com.baro.oauth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.baro.member.domain.Member;
import com.baro.member.infrastructure.MemberJpaRepository;
import com.baro.oauth.application.dto.OAuthMemberInfo;
import com.baro.oauth.application.dto.OAuthTokenInfo;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class OAuthServiceTest {

    @Autowired
    private OAuthService oAuthService;
    @Autowired
    private MemberJpaRepository memberRepository;

    @MockBean
    private OAuthClientComponents components;

    @BeforeEach
    void setUpOAuthClientRequest() {
        OAuthClient kakaoOAuthClient = mock(OAuthClient.class);
        given(components.getClient(any())).willReturn(kakaoOAuthClient);
        OAuthTokenInfo oAuthTokenInfo = new OAuthTokenInfo("accessToken", "refreshToken", 1000);
        given(kakaoOAuthClient.requestAccessToken("kakaoAuthCode")).willReturn(oAuthTokenInfo);
        OAuthMemberInfo oAuthMemberInfo = new OAuthMemberInfo("kakaoId", "kakaoName", "kakaoEmail");
        given(kakaoOAuthClient.requestMemberInfo(oAuthTokenInfo)).willReturn(oAuthMemberInfo);
    }

    @Test
    void 최초_소셜_로그인시_회원을_추가_한다() {
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
    void 이미_존재_하는_회원인_경우_추가_절차_없이_로그인_처리_한다() {
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
}
