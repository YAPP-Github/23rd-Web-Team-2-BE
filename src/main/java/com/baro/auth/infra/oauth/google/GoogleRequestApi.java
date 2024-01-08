package com.baro.auth.infra.oauth.google;

import com.baro.auth.infra.oauth.google.dto.GoogleMemberResponse;
import com.baro.auth.infra.oauth.google.dto.GoogleTokenResponse;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

public interface GoogleRequestApi {

    @PostExchange(url = "https://oauth2.googleapis.com/token", contentType = APPLICATION_FORM_URLENCODED_VALUE)
    GoogleTokenResponse requestToken(@RequestParam Map<String, String> params);

    @PostExchange(url = "https://www.googleapis.com/oauth2/v2/userinfo", contentType = APPLICATION_FORM_URLENCODED_VALUE)
    GoogleMemberResponse requestMemberInfo(@RequestHeader(name = AUTHORIZATION) String accessToken);
}
