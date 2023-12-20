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
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/oauth")
@RestController
public class OAuthController {

    private final OAuthService oAuthService;

    @GetMapping("/{oauthService}")
    ResponseEntity<Void> signInRequestUrl(@PathVariable String oauthService) {
        String signInUrl = oAuthService.getSignInUrl(oauthService);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(signInUrl));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }
}
