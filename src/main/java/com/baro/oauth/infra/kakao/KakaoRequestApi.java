package com.baro.oauth.infra.kakao;


import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

import com.baro.oauth.infra.kakao.dto.KakaoTokenResponse;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PostExchange;

public interface KakaoRequestApi {

    @PostExchange(url = "https://kauth.kakao.com/oauth/token", contentType = APPLICATION_FORM_URLENCODED_VALUE)
    KakaoTokenResponse requestToken(@RequestParam Map<String, String> params);
}
