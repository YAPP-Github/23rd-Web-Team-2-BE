package com.baro.common.acceptance.check;

import static io.restassured.RestAssured.given;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class ActuatorAcceptanceSteps {

    public static ExtractableResponse<Response> 액추에이터_헬스체크_요청() {
        var url = "/actuator/health";

        return given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Content-type", "application/json")
                .when().get(url)
                .then().log().all()
                .extract();
    }
}
