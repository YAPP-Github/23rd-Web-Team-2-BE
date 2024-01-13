package com.baro.common.acceptance.auth;

import static io.restassured.RestAssured.given;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class OAuthAcceptanceSteps {

    public static ExtractableResponse<Response> 리다이렉트_URI_요청(String oauthServiceType) {
        var url = "/auth/oauth/{oauthType}";

        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .redirects().follow(false)
                .when().get(url, oauthServiceType)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인_요청() {
        var url = "/auth/oauth/sign-in/{oauthType}";

        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("authCode", "authCode")
                .when().get(url, "kakao")
                .then().log().all()
                .extract();
    }
}
