package com.baro.auth.presentation;

import com.baro.auth.application.AuthService;
import com.baro.auth.application.dto.SignInDto;
import com.baro.auth.application.oauth.OAuthInfoProvider;
import com.baro.auth.application.oauth.dto.OAuthMemberInfo;
import com.baro.auth.domain.AuthMember;
import com.baro.auth.domain.Token;
import com.baro.auth.presentation.dto.OAuthServiceUrlResponse;
import com.baro.common.presentation.RequestHost;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final AuthService authService;
    private final OAuthInfoProvider oAuthInfoProvider;

    @GetMapping("/oauth/{oauthType}")
    ResponseEntity<OAuthServiceUrlResponse> signInRequestUrl(@PathVariable String oauthType, @RequestHost String host) {
        String signInUrl = oAuthInfoProvider.getSignInUrl(oauthType, host);
        return ResponseEntity.ok(new OAuthServiceUrlResponse(signInUrl));
    }

    @GetMapping("/oauth/sign-in/{oauthType}")
    ResponseEntity<Token> signIn(@PathVariable String oauthType, @RequestParam String authCode) {
        OAuthMemberInfo memberInfo = oAuthInfoProvider.getMemberInfo(oauthType, authCode);
        Token token = authService.signIn(
                new SignInDto(memberInfo.name(), memberInfo.email(), memberInfo.oAuthId(), oauthType));
        return ResponseEntity.ok().body(token);
    }

    @GetMapping("/reissue")
    ResponseEntity<Token> reissue(@RequestParam String refreshToken, AuthMember authMember) {
        Token token = authService.reissue(authMember.id(), refreshToken);
        return ResponseEntity.ok().body(token);
    }
}
