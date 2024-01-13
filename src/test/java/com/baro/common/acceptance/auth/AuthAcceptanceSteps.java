package com.baro.common.acceptance.auth;

import static io.restassured.RestAssured.given;

import com.baro.auth.domain.Token;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class AuthAcceptanceSteps {

    public static ExtractableResponse<Response> 토큰_재발급_요청(Token token) {
        var url = "/auth/reissue";

        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.accessToken())
                .queryParam("refreshToken", "Bearer " + token.refreshToken())
                .when().get(url)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> Bearer_타입이_아닌_토큰_재발급_요청(Token token) {
        var url = "/auth/reissue";

        return given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.accessToken())
                .queryParam("refreshToken", "Basic " + token.refreshToken())
                .when().get(url)
                .then().log().all()
                .extract();
    }
}
