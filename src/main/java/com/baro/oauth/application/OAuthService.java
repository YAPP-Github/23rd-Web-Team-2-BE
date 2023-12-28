package com.baro.oauth.application;

import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import com.baro.oauth.application.dto.OAuthMemberInfo;
import com.baro.oauth.application.dto.OAuthTokenInfo;
import com.baro.oauth.domain.OAuthServiceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class OAuthService {

    private final OAuthClientComponents clientComponents;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public String getSignInUrl(String oAuthService) {
        OAuthClient client = getClientByOAuthService(oAuthService);
        return client.getSignInUrl();
    }

    public void signIn(String oAuthService, String authCode) {
        OAuthClient client = getClientByOAuthService(oAuthService);
        OAuthTokenInfo oAuthTokenInfo = client.requestAccessToken(authCode);
        OAuthMemberInfo oAuthMemberInfo = client.requestMemberInfo(oAuthTokenInfo);
        Member member = memberRepository.findByOAuthIdAndOAuthServiceType(oAuthMemberInfo.oAuthId(), oAuthService)
                .orElseGet(() -> createMember(oAuthService, oAuthMemberInfo));
        //TODO 인증 토큰 추가
    }

    private Member createMember(String oAuthService, OAuthMemberInfo oAuthMemberInfo) {
        Member member = Member.builder()
                .name(oAuthMemberInfo.name())
                .email(oAuthMemberInfo.email())
                .oAuthId(oAuthMemberInfo.oAuthId())
                .oAuthServiceType(oAuthService)
                .build();
        return memberRepository.save(member);
    }

    private OAuthClient getClientByOAuthService(String oAuthService) {
        OAuthServiceType oAuthServiceType = OAuthServiceType.from(oAuthService);
        return clientComponents.getClient(oAuthServiceType);
    }
}
