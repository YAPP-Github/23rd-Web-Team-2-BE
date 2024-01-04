package com.baro.oauth.presentation;

import com.baro.oauth.application.OAuthService;
import java.net.URI;
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
@RequestMapping("/oauth")
@RestController
public class OAuthController {

    private final OAuthService oAuthService;

    /**
     * p3. oauthService 파라미터를 Controller 때 부터 Enum으로 관리하는 건 어떨까요?
     * Service에서 변환하고 있긴 하지만 더 빠른 시점에서 변환 하면 좋을 것 같아요.
     */
    @GetMapping("/{oauthService}")
    ResponseEntity<Void> signInRequestUrl(@PathVariable String oauthService) {
        String signInUrl = oAuthService.getSignInUrl(oauthService);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(signInUrl));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping("/sign-in/{oauthService}")
    ResponseEntity<Void> signIn(@PathVariable String oauthService, @RequestParam String authCode) {
        oAuthService.signIn(oauthService, authCode);
        return ResponseEntity.ok().build(); //TODO: 로그인 처리 후 응답 변경
    }
}
