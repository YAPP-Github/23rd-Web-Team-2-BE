package com.baro.auth.application;

import com.baro.auth.application.dto.SignInDto;
import com.baro.auth.domain.Token;
import com.baro.auth.exception.AuthException;
import com.baro.auth.exception.AuthExceptionType;
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

    public Token reissue(Long memberId, String refreshToken, String requestIpAddress) {
        String ipAddress = tokenTranslator.decodeRefreshToken(refreshToken);

        validateRefreshToken(memberId, refreshToken);
        validateIpAddress(ipAddress, requestIpAddress);

        Token newToken = tokenTranslator.encode(memberId, requestIpAddress);

        tokenStorage.saveRefreshToken(String.valueOf(memberId), newToken.refreshToken());
        return newToken;
    }

    private void validateRefreshToken(Long memberId, String refreshToken) {
        String storedRefreshToken = tokenStorage.findRefreshToken(String.valueOf(memberId));
        if(refreshToken == null)
            throw new AuthException(AuthExceptionType.REFRESH_TOKEN_DOES_NOT_EXIST);

        if (!storedRefreshToken.equals(refreshToken))
            throw new AuthException(AuthExceptionType.CLIENT_AND_TOKEN_IS_NOT_MATCH);
    }

    private void validateIpAddress(String ipAddress, String requestIpAddress) {
        if (!ipAddress.equals(requestIpAddress))
            throw new AuthException(AuthExceptionType.CLIENT_AND_TOKEN_IS_NOT_MATCH);
    }
}
