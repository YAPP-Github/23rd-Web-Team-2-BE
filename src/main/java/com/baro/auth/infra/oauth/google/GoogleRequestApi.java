package com.baro.auth.infra.oauth.google;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

import com.baro.auth.infra.oauth.google.dto.GoogleMemberResponse;
import com.baro.auth.infra.oauth.google.dto.GoogleTokenResponse;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface GoogleRequestApi {

    @PostExchange(url = "https://oauth2.googleapis.com/token", contentType = APPLICATION_FORM_URLENCODED_VALUE)
    GoogleTokenResponse requestToken(@RequestParam Map<String, String> params);

    @GetExchange(url = "https://www.googleapis.com/oauth2/v2/userinfo")
    GoogleMemberResponse requestMemberInfo(@RequestHeader(name = AUTHORIZATION) String accessToken);
}
