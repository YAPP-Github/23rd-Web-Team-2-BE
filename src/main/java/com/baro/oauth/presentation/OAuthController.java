package com.baro.oauth.presentation;

import com.baro.oauth.application.OAuthClient;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/oauth")
@RestController
public class OAuthController {

    private final OAuthClient kakaoOAuthClient;

    @GetMapping("/{oauthServiceType}")
    ResponseEntity<Void> signInRequestUrl(@PathVariable String oauthServiceType) {
        String redirectUrl = kakaoOAuthClient.getSignInUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }
}
