package com.baro.auth.application;

import com.baro.auth.application.dto.SignInDto;
import com.baro.auth.domain.Token;
import com.baro.member.application.MemberCreator;
import com.baro.member.domain.Member;
import com.baro.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final MemberCreator memberCreator;
    private final TokenTranslator tokenTranslator;
    private final TokenStorage tokenStorage;

    public Token signIn(SignInDto dto) {
        Member member = memberRepository.findByOAuthIdAndOAuthServiceType(dto.oauthId(), dto.oauthType())
                .orElseGet(() -> memberCreator.create(dto.name(), dto.email(), dto.oauthId(), dto.oauthType()));

        Token token = tokenTranslator.encode(member.getId(), dto.ipAddress());

        tokenStorage.saveRefreshToken(String.valueOf(member.getId()), token.refreshToken());
        return token;
    }

    public Token reissue(Token token, String requestIpAddress) {
        Long memberId = tokenTranslator.decodeAccessToken(token.accessToken());
        String ipAddress = tokenTranslator.decodeRefreshToken(token.refreshToken());

        if (!tokenStorage.findRefreshToken(String.valueOf(memberId)).equals(token.refreshToken())
                || !ipAddress.equals(requestIpAddress)) {
            throw new IllegalArgumentException("cannot perform reissue. authentication info is not matching."); // TODO
        }

        Token newToken = tokenTranslator.encode(memberId, requestIpAddress);

        tokenStorage.saveRefreshToken(String.valueOf(memberId), newToken.refreshToken());
        return token;
    }
}
