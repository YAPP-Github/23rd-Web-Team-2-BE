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

        Token token = tokenTranslator.encode(member.getId());

        tokenStorage.saveRefreshToken(String.valueOf(member.getId()), token.refreshToken());
        return token;
    }

    public Token reissue(Long memberId, String refreshToken) {
        tokenTranslator.validateRefreshToken(refreshToken);

        validateRefreshToken(memberId, refreshToken);

        Token newToken = tokenTranslator.encode(memberId);

        tokenStorage.saveRefreshToken(String.valueOf(memberId), newToken.refreshToken());
        return newToken;
    }

    private void validateRefreshToken(Long memberId, String refreshToken) {
        String storedRefreshToken = tokenStorage.findRefreshToken(String.valueOf(memberId));
        if (refreshToken == null)
            throw new AuthException(AuthExceptionType.REFRESH_TOKEN_DOES_NOT_EXIST);

        if (!storedRefreshToken.equals(refreshToken))
            throw new AuthException(AuthExceptionType.CLIENT_AND_TOKEN_IS_NOT_MATCH);
    }
}
