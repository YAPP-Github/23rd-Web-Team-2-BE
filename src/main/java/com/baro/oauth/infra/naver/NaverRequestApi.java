package com.baro.oauth.infra.naver;

import com.baro.oauth.infra.naver.dto.NaverMemberResponse;
import com.baro.oauth.infra.naver.dto.NaverTokenResponse;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

public interface NaverRequestApi {

    @PostExchange(url = "https://nid.naver.com/oauth2.0/token", contentType = APPLICATION_FORM_URLENCODED_VALUE)
    NaverTokenResponse requestToken(@RequestParam Map<String, String> params);

    @PostExchange(url = "https://openapi.naver.com/v1/nid/me", contentType = APPLICATION_FORM_URLENCODED_VALUE)
    NaverMemberResponse requestMemberInfo(@RequestHeader(name = AUTHORIZATION) String accessToken);
}
