package com.baro.auth.infra.oauth.naver;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

import com.baro.auth.infra.oauth.naver.dto.NaverMemberResponse;
import com.baro.auth.infra.oauth.naver.dto.NaverTokenResponse;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface NaverRequestApi {

    @PostExchange(url = "https://nid.naver.com/oauth2.0/token", contentType = APPLICATION_FORM_URLENCODED_VALUE)
    NaverTokenResponse requestToken(@RequestParam Map<String, String> params);

    @GetExchange(url = "https://openapi.naver.com/v1/nid/me")
    NaverMemberResponse requestMemberInfo(@RequestHeader(name = AUTHORIZATION) String accessToken);
}
