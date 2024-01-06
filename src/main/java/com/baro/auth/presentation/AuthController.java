package com.baro.auth.presentation;

import com.baro.auth.application.AuthService;
import com.baro.auth.application.dto.SignInDto;
import com.baro.auth.application.oauth.OAuthInfoProvider;

import java.net.URI;

import com.baro.auth.application.oauth.dto.OAuthMemberInfo;
import com.baro.auth.domain.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    ResponseEntity<Void> signInRequestUrl(@PathVariable String oauthType) {
        String signInUrl = oAuthInfoProvider.getSignInUrl(oauthType);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(signInUrl));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping("/oauth/sign-in/{oauthType}")
    ResponseEntity<Token> signIn(@PathVariable String oauthType, @RequestParam String authCode) {
        OAuthMemberInfo memberInfo = oAuthInfoProvider.getMemberInfo(oauthType, authCode);
        Token token = authService.signIn(new SignInDto(memberInfo.name(), memberInfo.email(), memberInfo.oAuthId(), oauthType));
        return ResponseEntity.ok().body(token);
    }

    @GetMapping("/sign-out")
    ResponseEntity<Void> signOut() {
        return ResponseEntity.ok().build(); //TODO: 로그아웃 처리 후 응답 변경
    }
}
